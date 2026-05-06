package com.affordmed.vehiclescheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/vehicle")
public class VehicleSchedulerController {

    @Autowired
    private VehicleSchedulerService service;

    @GetMapping("/schedule")
    public Map<String, Object> getSchedule() {
        LoggerUtil.Log("backend", "info", "controller", "Schedule endpoint hit");
        Map<String, Object> result = service.schedule();
        LoggerUtil.Log("backend", "info", "controller", "Schedule completed successfully");
        return result;
    }
}