package com.rewrite.common;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

public class CheckNodes {
    private static final String HOSTNAME = System.getenv("HOSTNAME");
    private static final String PORT = System.getenv("PORT");

    public static String getWorkloadTagName() {
        if(HOSTNAME != null && HOSTNAME.contains("backend-collab-")) {
            String[] fields = HOSTNAME.replace("backend-collab-", "").split("-");
            if(fields.length > 0){
                return fields[0];
            }
        }
        return "";
    }
    public static CompletableFuture<HttpStatus> checkNodes(String fileId, String tagName, String url) {
        RestTemplate restTemplate = new RestTemplate();
        String requestUrl = String.format("%s?port=%s&fileId=%s&backend_branch=%s", url, PORT, fileId, tagName);

        return CompletableFuture.supplyAsync(() -> {
            try {
                ResponseEntity<Void> response = restTemplate.getForEntity(requestUrl, Void.class);
                return response.getStatusCode();
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                return e.getStatusCode(); // 直接返回捕获到的异常中的状态码
            } catch (ResourceAccessException e) {
                throw new RuntimeException("Check nodes timeout", e); // 模拟超时异常
            } catch (Exception e) {
                throw new RuntimeException("Unknown error", e);
            }
        });

    }

}
