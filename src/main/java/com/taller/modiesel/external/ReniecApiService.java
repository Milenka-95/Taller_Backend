package com.taller.modiesel.external;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ReniecApiService {

    private static final String API_URL = "https://apiperu.dev/api/ruc";
    private static final String TOKEN = "76353cda925d17e72ebff7c8ebf2431c546d743c111fd0864240976a48e31cd1";

    public Map<String, Object> consultarRuc(String ruc) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + TOKEN);

        Map<String, String> body = new HashMap<>();
        body.put("ruc", ruc);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, Map.class);

        return response.getBody();
    }
}