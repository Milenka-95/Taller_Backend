package com.taller.modiesel.external;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReniecApiClient {

    @Value("https://apiperu.dev/api/ruc")
    private String reniecUrl;

    @Value("76353cda925d17e72ebff7c8ebf2431c546d743c111fd0864240976a48e31cd1")
    private String token;

    private final RestTemplate restTemplate = new RestTemplate();

    public String consultarRuc(String ruc) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("ruc", ruc);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        String url = reniecUrl + "?token=" + token;

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                String.class
        );

        return response.getBody();
    }
}
