package space.ffisherr.clay.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import space.ffisherr.clay.model.ClayInstrument;
import space.ffisherr.clay.model.TransactionRequestDTO;
import space.ffisherr.clay.model.TransactionResponseDTO;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class IntegrationServiceImpl implements IntegrationService {

    @Value("${clay.integration-service.url}")
    private String integrationServiceUrl;


    @Override
    public List<ClayInstrument> readAllInstruments() {
        final ClientHttpRequestFactory factory =
                new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
        final RestTemplate restTemplate = new RestTemplate(factory);
        final String url = integrationServiceUrl + "/unique/";
        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.ALL));
        headers.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity request = new HttpEntity<>(null, headers);
        ResponseEntity<List> response = null;
        try {
            response = restTemplate.exchange(url, HttpMethod.GET, request, List.class);
        } catch (Exception e) {
            log.warn("Error while sending request to api {} : {}", url, e.getMessage());
        }
        final List<ClayInstrument> realResponse = (List<ClayInstrument>) response.getBody();
        return realResponse;
    }

    @Override
    public ClayInstrument readTicker(String tikerName, String timeVal) {
        final ClientHttpRequestFactory factory =
                new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
        final RestTemplate restTemplate = new RestTemplate(factory);
        final String param = String.format("/?tickerName=%s&timeVal=%s", tikerName, timeVal);
        final String url = integrationServiceUrl + param;
        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.ALL));
        headers.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity<ClayInstrument> request = new HttpEntity<>(null, headers);
        ResponseEntity<ClayInstrument> response = null;
        try {
            response = restTemplate.exchange(url, HttpMethod.GET, request, ClayInstrument.class);
        } catch (Exception e) {
            log.warn("Error while sending request to api {} : {}", url, e.getMessage());
        }
        if (response != null) {
            return response.getBody();
        }
        return null;
    }

    @Override
    public TransactionResponseDTO sendSell(TransactionRequestDTO request) {
        final String url = integrationServiceUrl + "/sell/";
        final ResponseEntity<TransactionResponseDTO> response = sendMessage(request, url);
        return processResult(response);
    }

    @Override
    public TransactionResponseDTO sendBuy(TransactionRequestDTO request) {
        final String url = integrationServiceUrl + "/buy/";
        final ResponseEntity<TransactionResponseDTO> response = sendMessage(request, url);
        return processResult(response);
    }

    private ResponseEntity<TransactionResponseDTO> sendMessage(TransactionRequestDTO requestDTO, String url) {
        final ClientHttpRequestFactory factory =
                new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
        final RestTemplate restTemplate = new RestTemplate(factory);
        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.ALL));
        headers.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity<TransactionRequestDTO> request = new HttpEntity<>(requestDTO, headers);
        ResponseEntity<TransactionResponseDTO> response = null;
        try {
            response = restTemplate.exchange(url, HttpMethod.POST, request, TransactionResponseDTO.class);
        } catch (Exception e) {
            log.warn("Error while sending request to ptk {} : {}", url, e.getMessage());
        }
        return response;
    }

    private TransactionResponseDTO processResult(ResponseEntity<TransactionResponseDTO> response) {
        if (response == null) {
            log.error("Отсутствует ответ");
            return null;
        }
        if (!HttpStatus.OK.equals(response.getStatusCode())) {
            log.error("Получен неверный статус код: {}", response.getStatusCode());
            return null;
        }
        log.info("Успешно получена информация");
        return response.getBody();
    }

}
