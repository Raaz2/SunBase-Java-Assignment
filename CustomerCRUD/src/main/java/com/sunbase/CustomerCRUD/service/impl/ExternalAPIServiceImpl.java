package com.sunbase.CustomerCRUD.service.impl;

import com.sunbase.CustomerCRUD.model.Customer;
import com.sunbase.CustomerCRUD.service.ExternalAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExternalAPIServiceImpl implements ExternalAPIService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${external.api.login_id}")
    private String loginId;

    @Value("${external.api.password}")
    private String password;

    @Value("${external.api.auth_url}")
    private String authUrl;

    @Value("${external.api.customer_list_url}")
    private String customerListUrl;

    // Method to authenticate and get Bearer token
    public String authenticate() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> authBody = new HashMap<>();
        authBody.put("login_id", loginId);
        authBody.put("password", password);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(authBody, headers);

        ResponseEntity<Map<String, String>> response = restTemplate.exchange(authUrl, HttpMethod.POST,request, new ParameterizedTypeReference<Map<String, String>>() {});

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody().get("access_token");
        } else {
            throw new RuntimeException("Failed to authenticate with external API");
        }
    }

    // fetching customers list from external api using token
    public List<Customer> fetchCustomers(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> request = new HttpEntity<>(headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(customerListUrl)
                .queryParam("cmd", "get_customer_list");

        ResponseEntity<Customer[]> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                request,
                Customer[].class
        );

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return Arrays.asList(response.getBody());
        } else {
            throw new RuntimeException("Failed to fetch customer list");
        }
    }


}
