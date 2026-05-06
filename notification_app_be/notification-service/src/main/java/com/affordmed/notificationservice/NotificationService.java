package com.affordmed.notificationservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

@Service
public class NotificationService {

    private static final String BASE_URL = "http://20.207.122.201/evaluation-service";
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJNYXBDbGFpbXMiOnsiYXVkIjoiaHR0cDovLzIwLjI0NC41Ni4xNDQvZXZhbHVhdGlvbi1zZXJ2aWNlIiwiZW1haWwiOiJtdWdpbGFuLnMuMjAyMy5haWRzQHJpdGNoZW5uYWkuZWR1LmluIiwiZXhwIjoxNzc4MDUwMjM1LCJpYXQiOjE3NzgwNDkzMzUsImlzcyI6IkFmZm9yZCBNZWRpY2FsIFRlY2hub2xvZ2llcyBQcml2YXRlIExpbWl0ZWQiLCJqdGkiOiI3NWU0NjdlNS03MmRkLTQ2MjktYjM3Ni1hMjlmZmZiYWIxNDIiLCJsb2NhbGUiOiJlbi1JTiIsIm5hbWUiOiJtdWdpbGFuIHMiLCJzdWIiOiJhNmRjZDAwZi0wNDkyLTQzMGMtOGZkZS04MWY0ODY0MjZiZWMifSwiZW1haWwiOiJtdWdpbGFuLnMuMjAyMy5haWRzQHJpdGNoZW5uYWkuZWR1LmluIiwibmFtZSI6Im11Z2lsYW4gcyIsInJvbGxObyI6IjIxMTcyMzAwNzAwOTMiLCJhY2Nlc3NDb2RlIjoiQlRDRHFUIiwiY2xpZW50SUQiOiJhNmRjZDAwZi0wNDkyLTQzMGMtOGZkZS04MWY0ODY0MjZiZWMiLCJjbGllbnRTZWNyZXQiOiJQbVpteUR2RnpLeWZ4ZGVrIn0.l3R3UJOvvdeRZ7oCNnoJbig7x0G_lanwXVcXSTFPe8Q";

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public List<Map<String, Object>> getTop10PriorityNotifications() {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(getHeaders());

        LoggerUtil.Log("backend", "info", "service", "Fetching notifications from API");

        Map response = restTemplate.exchange(
                BASE_URL + "/notifications", HttpMethod.GET, entity, Map.class
        ).getBody();

        List<Map<String, Object>> notifications = (List<Map<String, Object>>) response.get("notifications");
        LoggerUtil.Log("backend", "info", "service", "Fetched " + notifications.size() + " notifications");

        // Calculate priority score
        for (Map<String, Object> n : notifications) {
            String type = (String) n.get("Type");
            String timestamp = (String) n.get("Timestamp");

            int typeWeight = switch (type) {
                case "Placement" -> 3;
                case "Result" -> 2;
                default -> 1;
            };

            long recencyScore = java.time.Instant.parse(timestamp.replace(" ", "T") + "Z").getEpochSecond();
            double priorityScore = typeWeight * 1_000_000_000L + recencyScore;
            n.put("priorityScore", priorityScore);
        }

        // Sort by priority score descending
        notifications.sort((a, b) -> Double.compare(
                (double) b.get("priorityScore"),
                (double) a.get("priorityScore")
        ));

        List<Map<String, Object>> top10 = notifications.subList(0, Math.min(10, notifications.size()));
        LoggerUtil.Log("backend", "info", "service", "Top 10 priority notifications selected");
        return top10;
    }
}