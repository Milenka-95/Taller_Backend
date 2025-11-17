package com.taller.modiesel.external;

import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.*;
import java.util.*;

@Service
public class ImgBBService {

    private static final String IMGBB_URL = "https://milenka-gutarra.imgbb.com/1/upload";
    private static final String API_KEY = "6f5430f05d7e872765e16e9431a33e04";

    public String subirImagen(MultipartFile imagen) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = IMGBB_URL + "?key=" + API_KEY;

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", Base64.getEncoder().encodeToString(imagen.getBytes()));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);

            Map data = (Map) response.getBody().get("data");
            return (String) data.get("url");

        } catch (Exception e) {
            throw new RuntimeException("Error al subir imagen a ImgBB", e);
        }
    }
}