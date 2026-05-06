package com.affordmed.notificationservice;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LoggerUtil {

    private static final String BASE_URL = "http://20.207.122.201/evaluation-service";
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJNYXBDbGFpbXMiOnsiYXVkIjoiaHR0cDovLzIwLjI0NC41Ni4xNDQvZXZhbHVhdGlvbi1zZXJ2aWNlIiwiZW1haWwiOiJtdWdpbGFuLnMuMjAyMy5haWRzQHJpdGNoZW5uYWkuZWR1LmluIiwiZXhwIjoxNzc4MDUwMjM1LCJpYXQiOjE3NzgwNDkzMzUsImlzcyI6IkFmZm9yZCBNZWRpY2FsIFRlY2hub2xvZ2llcyBQcml2YXRlIExpbWl0ZWQiLCJqdGkiOiI3NWU0NjdlNS03MmRkLTQ2MjktYjM3Ni1hMjlmZmZiYWIxNDIiLCJsb2NhbGUiOiJlbi1JTiIsIm5hbWUiOiJtdWdpbGFuIHMiLCJzdWIiOiJhNmRjZDAwZi0wNDkyLTQzMGMtOGZkZS04MWY0ODY0MjZiZWMifSwiZW1haWwiOiJtdWdpbGFuLnMuMjAyMy5haWRzQHJpdGNoZW5uYWkuZWR1LmluIiwibmFtZSI6Im11Z2lsYW4gcyIsInJvbGxObyI6IjIxMTcyMzAwNzAwOTMiLCJhY2Nlc3NDb2RlIjoiQlRDRHFUIiwiY2xpZW50SUQiOiJhNmRjZDAwZi0wNDkyLTQzMGMtOGZkZS04MWY0ODY0MjZiZWMiLCJjbGllbnRTZWNyZXQiOiJQbVpteUR2RnpLeWZ4ZGVrIn0.l3R3UJOvvdeRZ7oCNnoJbig7x0G_lanwXVcXSTFPe8Q";

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