package com.example.GameService.config;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import java.io.IOException;

public class CustomErrorHandler extends DefaultResponseErrorHandler {

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        // Custom error handling logic
        System.out.println("Custom error handling: " + response.getStatusCode());
        super.handleError(response);
    }
}
