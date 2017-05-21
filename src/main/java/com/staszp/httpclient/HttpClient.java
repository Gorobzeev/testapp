package com.staszp.httpclient;

import com.staszp.dto.incommingmodel.IncomeRateModelDto;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;


@Service
public class HttpClient {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(HttpClient.class);

//    private static AsyncRestTemplate template = new AsyncRestTemplate();


    private final String URL_PART1 = "http://api.nbp.pl/api/exchangerates/tables/a/";
    private final String URL_PART2 = "/?format=json";
    private final String DELIMETR = "/";


//    public List<RateModelDto> getRates() {
//
//        HttpHeaders headers = new HttpHeaders();
//
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL);
//
//        HttpEntity<?> request = new HttpEntity<>(headers);
//        ListenableFuture<ResponseEntity<List<IncomeRateModelDto>>> future = template.exchange(builder.build().encode().toUri(), HttpMethod.GET, request,  IncomeRateModelDto.class);
//
//        List<RateModelDto> result = new ArrayList<>();
//
//        try {
//            ResponseEntity<List<IncomeRateModelDto>> entity = future.get();
//            HttpStatus statusCode = entity.getStatusCode();
//            if (!statusCode.is4xxClientError()) {
//                result = entity.getBody().getRates();
//            } else {
//                logger.warn("Scheduler server status code: {}", statusCode);
//            }
//        } catch (Exception e) {
//            logger.warn("Can't get answer from scheduler service");
//        }
//        logger.info("Leaving getRates with size of result = {}", result.size());
//        return result;
//    }
private static RestTemplate template = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

    public List<IncomeRateModelDto> getRates(String start, String end) {
        logger.info("Entering  getRates() with startTime = {}, endTime = {}", start, end);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);
        List<IncomeRateModelDto> result = Collections.emptyList();
        ResponseEntity<List<IncomeRateModelDto>> response;

        try {
            response = template.exchange(URL_PART1 + start + DELIMETR + end + URL_PART2,
                    HttpMethod.GET, request, new ParameterizedTypeReference<List<IncomeRateModelDto>>() {
                    });
            result = response.getBody();
        } catch (RestClientException ex){
            logger.warn("getRates() caused error: {}", ex.getLocalizedMessage());
        }
        return result;
    }

}
