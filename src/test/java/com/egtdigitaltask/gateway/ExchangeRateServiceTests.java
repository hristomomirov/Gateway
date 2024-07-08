package com.egtdigitaltask.gateway;

import com.egtdigitaltask.gateway.model.Currency;
import com.egtdigitaltask.gateway.model.ExchangeRate;
import com.egtdigitaltask.gateway.model.RequestData;
import com.egtdigitaltask.gateway.model.dto.CurrentExchangeRateResponse;
import com.egtdigitaltask.gateway.model.dto.ExchangeRateHistory;
import com.egtdigitaltask.gateway.model.dto.ExchangeRateHistoryResponse;
import com.egtdigitaltask.gateway.repository.CurrenciesRepository;
import com.egtdigitaltask.gateway.repository.ExchangeRateRepository;
import com.egtdigitaltask.gateway.repository.RequestRepository;
import com.egtdigitaltask.gateway.service.ExchangeRateService;
import com.egtdigitaltask.gateway.service.RabbitMqProducer;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Instant;
import java.util.*;


@SpringBootTest
public class ExchangeRateServiceTests extends BaseTestContainers
{
    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private ExchangeRateService exchangeRateService;

    @Autowired
    private CurrenciesRepository currenciesRepository;


    @MockBean
    private RabbitMqProducer producer;

    @Captor
    ArgumentCaptor<RequestData> requestDataCaptor;

    private static final long TIMESTAMP_THIRTY_MINUTES_AGO = Instant.now().minusSeconds(60 * 30).getEpochSecond();

    private static final long TIMESTAMP_THREE_HOURS_AGO = Instant.now().minusSeconds(60 * 60 * 3).getEpochSecond();

    private static final Map<String, Double> EXPECTED_EXCHANGE_RATES = new HashMap<>();

    private static final List<ExchangeRateHistory> EXPECTED_EXCHANGE_RATE_HISTORIES = new ArrayList<>();

    static
    {
        EXPECTED_EXCHANGE_RATES.put("BGN", 1.960937);
        EXPECTED_EXCHANGE_RATES.put("GBP", 0.84401223);

        EXPECTED_EXCHANGE_RATE_HISTORIES.add(new ExchangeRateHistory(TIMESTAMP_THIRTY_MINUTES_AGO, EXPECTED_EXCHANGE_RATES));
        EXPECTED_EXCHANGE_RATE_HISTORIES.add(new ExchangeRateHistory(TIMESTAMP_THREE_HOURS_AGO, EXPECTED_EXCHANGE_RATES));
    }

    @BeforeEach
    public void prepareTestData()
    {
        Currency euro = new Currency("EUR", "Euro");
        Currency bgn = new Currency("BGN", "Lev");
        Currency gbp = new Currency("GBP", "Pound");
        currenciesRepository.save(euro);
        currenciesRepository.save(bgn);
        currenciesRepository.save(gbp);

        List<ExchangeRate> rates = new LinkedList<>();
        ExchangeRate exchangeRateToBgnNow = new ExchangeRate(euro, bgn, TIMESTAMP_THIRTY_MINUTES_AGO, 1.960937);

        ExchangeRate exchangeRateToGbpNow = new ExchangeRate(euro, gbp, TIMESTAMP_THIRTY_MINUTES_AGO, 0.84401223);

        ExchangeRate exchangeRateToBgnPast = new ExchangeRate(euro, bgn, TIMESTAMP_THREE_HOURS_AGO, 1.960937);

        ExchangeRate exchangeRateToGbpPast = new ExchangeRate(euro, gbp, TIMESTAMP_THREE_HOURS_AGO, 0.84401223);

        rates.add(exchangeRateToBgnNow);
        rates.add(exchangeRateToGbpNow);
        rates.add(exchangeRateToBgnPast);
        rates.add(exchangeRateToGbpPast);
        List<ExchangeRate> insertedRates = exchangeRateRepository.saveAll(rates);

        Assertions.assertFalse(insertedRates.isEmpty());
    }

    @AfterEach
    public void clearTestData()
    {
        exchangeRateRepository.deleteAll();
        requestRepository.deleteAll();
        currenciesRepository.deleteAll();
    }

    @Test
    public void testGetCurrentExchangeRates()
    {
        String requestId = "test_requestId_testGetCurrentExchangeRates";
        long timestamp = Instant.now().getEpochSecond();

        CurrentExchangeRateResponse response = exchangeRateService.getCurrentExchangeRate("TEST_SERVICE",
                                                                                          requestId,
                                                                                          "test",
                                                                                          "EUR",
                                                                                          timestamp);
        validateRequestData(requestId,timestamp);
        validateCurrentExchangeRateResponse(response);
    }

    @Test
    public void testGetCurrentExchangeRatesWithDuplicateRequestId()
    {
        String requestId = "test_requestId_testGetCurrentExchangeRatesWithDuplicateRequestId";
        long timestamp = Instant.now().getEpochSecond();

        exchangeRateService.getCurrentExchangeRate("TEST_SERVICE",
                                                   requestId,
                                                   "test",
                                                   "EUR",
                                                   timestamp);

        Exception e = Assertions.assertThrows(IllegalArgumentException.class,
                                              () -> exchangeRateService.getCurrentExchangeRate("TEST_SERVICE",
                                                                                               requestId,
                                                                                               "test",
                                                                                               "EUR",
                                                                                               timestamp));

        Assertions.assertEquals("Request with that id already exists", e.getMessage());
    }

    @Test
    public void testGetExchangeRateHistory()
    {
        String requestId = "test_requestId_testGetExchangeRateHistory";
        long timestamp = Instant.now().getEpochSecond();

        ExchangeRateHistoryResponse response = exchangeRateService.getExchangeRateHistory("TEST_SERVICE",
                                                                                          requestId,
                                                                                          "test",
                                                                                          "EUR",
                                                                                          4, timestamp);

        validateRequestData(requestId, timestamp);
        validateExchangeRateHistoryResponse(response);
    }

    private void validateRequestData(String requestId, long timestamp)
    {
        Optional<RequestData> data = requestRepository.findByRequestId(requestId);
        Assertions.assertTrue(data.isPresent());

        RequestData expectedRequestData = new RequestData("TEST_SERVICE", requestId, timestamp, "test");
        expectedRequestData.setId(data.get().getId());
        Mockito.verify(producer).sendMessage(requestDataCaptor.capture());
        RequestData actualRequestData = requestDataCaptor.getValue();
        Assertions.assertEquals(expectedRequestData, actualRequestData);
    }

    private void validateCurrentExchangeRateResponse(CurrentExchangeRateResponse response)
    {
        Assertions.assertNotNull(response);
        Map<String, Double> actualExchangeRates = response.getExchangeRates();
        Assertions.assertNotNull(actualExchangeRates);
        Assertions.assertEquals(EXPECTED_EXCHANGE_RATES.size(), actualExchangeRates.size());
        for (Map.Entry<String, Double> entry : EXPECTED_EXCHANGE_RATES.entrySet())
        {
            Assertions.assertTrue(actualExchangeRates.containsKey(entry.getKey()));
            Assertions.assertEquals(entry.getValue(), actualExchangeRates.get(entry.getKey()));
        }
    }

    private void validateExchangeRateHistoryResponse(ExchangeRateHistoryResponse response)
    {
        Assertions.assertNotNull(response);
        List<ExchangeRateHistory> actualExchangeRateHistories = response.getHistories();
        Assertions.assertNotNull(actualExchangeRateHistories);
        Assertions.assertEquals(EXPECTED_EXCHANGE_RATE_HISTORIES.size(), actualExchangeRateHistories.size());
        for (ExchangeRateHistory history : EXPECTED_EXCHANGE_RATE_HISTORIES)
        {
            Assertions.assertTrue(actualExchangeRateHistories.contains(history));
        }
    }
}
