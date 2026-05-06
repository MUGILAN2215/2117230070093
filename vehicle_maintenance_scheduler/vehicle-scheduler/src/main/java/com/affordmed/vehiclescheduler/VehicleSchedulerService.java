package com.affordmed.vehiclescheduler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

@Service
public class VehicleSchedulerService {

    @Value("${affordmed.base-url}")
    private String baseUrl;

    @Value("${affordmed.token}")
    private String token;

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public Map<String, Object> schedule() {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(getHeaders());

        // Fetch depots
        LoggerUtil.Log("backend", "info", "service", "Fetching depots from API");
        Map depotsResponse = restTemplate.exchange(
                baseUrl + "/depots", HttpMethod.GET, entity, Map.class
        ).getBody();
        List<Map<String, Object>> depots = (List<Map<String, Object>>) depotsResponse.get("depots");
        LoggerUtil.Log("backend", "info", "service", "Fetched " + depots.size() + " depots successfully");

        // Fetch vehicles
        LoggerUtil.Log("backend", "info", "service", "Fetching vehicles from API");
        Map vehiclesResponse = restTemplate.exchange(
                baseUrl + "/vehicles", HttpMethod.GET, entity, Map.class
        ).getBody();
        List<Map<String, Object>> vehicles = (List<Map<String, Object>>) vehiclesResponse.get("vehicles");
        LoggerUtil.Log("backend", "info", "service", "Fetched " + vehicles.size() + " vehicles successfully");

        // Run knapsack for each depot
        List<Map<String, Object>> results = new ArrayList<>();
        for (Map<String, Object> depot : depots) {
            int depotId = (int) depot.get("ID");
            int budget = (int) depot.get("MechanicHours");

            LoggerUtil.Log("backend", "info", "service", "Running knapsack for depot " + depotId);
            List<Map<String, Object>> selected = knapsack(vehicles, budget);

            int totalImpact = selected.stream().mapToInt(v -> (int) v.get("Impact")).sum();
            int totalDuration = selected.stream().mapToInt(v -> (int) v.get("Duration")).sum();

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("depotId", depotId);
            result.put("budget", budget);
            result.put("totalDurationUsed", totalDuration);
            result.put("totalImpact", totalImpact);
            result.put("selectedTasks", selected);
            results.add(result);

            LoggerUtil.Log("backend", "info", "service", "Depot " + depotId + " completed. Total impact: " + totalImpact);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("schedulerResults", results);
        return response;
    }

    private List<Map<String, Object>> knapsack(List<Map<String, Object>> vehicles, int capacity) {
        int n = vehicles.size();
        int[][] dp = new int[n + 1][capacity + 1];

        for (int i = 1; i <= n; i++) {
            int duration = (int) vehicles.get(i - 1).get("Duration");
            int impact = (int) vehicles.get(i - 1).get("Impact");
            for (int w = 0; w <= capacity; w++) {
                if (duration <= w) {
                    dp[i][w] = Math.max(dp[i - 1][w], dp[i - 1][w - duration] + impact);
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }

        // Backtrack to find selected tasks
        List<Map<String, Object>> selected = new ArrayList<>();
        int w = capacity;
        for (int i = n; i > 0; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                selected.add(vehicles.get(i - 1));
                w -= (int) vehicles.get(i - 1).get("Duration");
            }
        }
        return selected;
    }
}