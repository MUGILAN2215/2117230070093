package com.affordmed.notificationservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService service;

    @GetMapping("/priority")
    public Map<String, Object> getTopPriority() {
        LoggerUtil.Log("backend", "info", "controller", "Priority inbox endpoint hit");
        List<Map<String, Object>> top10 = service.getTop10PriorityNotifications();
        LoggerUtil.Log("backend", "info", "controller", "Returning top 10 notifications");
        Map<String, Object> response = new HashMap<>();
        response.put("top10Notifications", top10);
        return response;
    }
}