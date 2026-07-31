package com.avi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ConcurrencyTest {
    @Test
    void runConcurrentRequest() throws Exception{
        int numberOfThreads = 20;

        //1. Get Token
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest loginRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/auth/login"))
                .header("Authorization","Basic YWRtaW46cGFzc3dvcmQ=")
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> loginResponse = client.send(loginRequest, HttpResponse.BodyHandlers.ofString());

        String body = loginResponse.body();
        String token = body.split("\"token\":\"")[1].split("\"")[0];
        System.out.println("Token acquired:"+ token);

        // 2. Setup thread pool and latches
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch startingLine = new CountDownLatch(1);
        CountDownLatch finishingLine = new CountDownLatch(numberOfThreads);

        for (int i=0;i<numberOfThreads;i++){
            final int requestId = i;
            executor.submit(()-> {
                try {
                    startingLine.await();

                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create("http://localhost:8080/api/agents/process"))
                            .header("Content-Type","application/json")
                            .header("Authorization","Bearer "+token)
                            .POST(HttpRequest.BodyPublishers.ofString("{\"taskType\":\"HISTORY\",\"message\":\"Msg " + requestId + "\"}"))
                            .build();

                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    System.out.println("Request #" + requestId + " Response: " + response.statusCode());
                }catch (Exception e){
                    System.err.println("Request #"+ requestId + " failed:" + e.getMessage());
                }finally {
                    finishingLine.countDown(); // tell main thread we finished
                }
            });
        }

        System.out.println("Ready... Set...");
        startingLine.countDown();// Firing gun! All threads run at once.
        finishingLine.await();  // Wait for all threads to finish
        executor.shutdown();
        System.out.println("All threads finished.");
    }

}
