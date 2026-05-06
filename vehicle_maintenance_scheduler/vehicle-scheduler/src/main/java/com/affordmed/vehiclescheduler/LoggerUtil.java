package com.affordmed.vehiclescheduler;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LoggerUtil {

    private static final String BASE_URL = "http://20.207.122.201/evaluation-service";
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJNYXBDbGFpbXMiOnsiYXVkIjoiaHR0cDovLzIwLjI0NC41Ni4xNDQvZXZhbHVhdGlvbi1zZXJ2aWNlIiwiZW1haWwiOiJtdWdpbGFuLnMuMjAyMy5haWRzQHJpdGNoZW5uYWkuZWR1LmluIiwiZXhwIjoxNzc4MDQ5MDg0LCJpYXQiOjE3NzgwNDgxODQsImlzcyI6IkFmZm9yZCBNZWRpY2FsIFRlY2hub2xvZ2llcyBQcml2YXRlIExpbWl0ZWQiLCJqdGkiOiI5MDRjYzIzYi1mZjE1LTQzN2ItYjMwMC02NDEyMzJmM2M1NTEiLCJsb2NhbGUiOiJlbi1JTiIsIm5hbWUiOiJtdWdpbGFuIHMiLCJzdWIiOiJhNmRjZDAwZi0wNDkyLTQzMGMtOGZkZS04MWY0ODY0MjZiZWMifSwiZW1haWwiOiJtdWdpbGFuLnMuMjAyMy5haWRzQHJpdGNoZW5uYWkuZWR1LmluIiwibmFtZSI6Im11Z2lsYW4gcyIsInJvbGxObyI6IjIxMTcyMzAwNzAwOTMiLCJhY2Nlc3NDb2RlIjoiQlRDRHFUIiwiY2xpZW50SUQiOiJhNmRjZDAwZi0wNDkyLTQzMGMtOGZkZS04MWY0ODY0MjZiZWMiLCJjbGllbnRTZWNyZXQiOiJQbVpteUR2RnpLeWZ4ZGVrIn0.UH-YuMEf2cNKFwJoodOSCoe6VibPMRXWes0NL-x0d4Y";

    public static void Log(String stack, String level, String pkg, String message) {
        try {
            String body = String.format(
                    "{\"stack\":\"%s\",\"level\":\"%s\",\"package\":\"%s\",\"message\":\"%s\"}",
                    stack, level, pkg, message
            );

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/logs"))
                    .header("Authorization", "Bearer " + TOKEN)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            // silent fail
        }
    }
}