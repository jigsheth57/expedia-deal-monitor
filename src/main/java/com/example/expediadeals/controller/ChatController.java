package com.example.expediadeals.controller;

import com.example.expediadeals.service.AiChatService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final AiChatService aiChatService;

    public ChatController(AiChatService aiChatService) {
        this.aiChatService = aiChatService;
    }

    @PostMapping("/deals")
    public Map<String, String> findDeals(@RequestBody Map<String, String> request) {
        String prompt = request.getOrDefault("prompt", "Find me vacation deals from Chicago on Expedia.");
        String aiResponse = aiChatService.getVacationDealsFromAi(prompt);
        return Map.of("prompt", prompt, "response", aiResponse);
    }

    // Example for a more structured response (conceptual)
    // @PostMapping("/structured-deals")
    // public List<VacationDeal> findStructuredDeals(@RequestBody Map<String, String> request) {
    //     String prompt = request.getOrDefault("prompt", "Find me vacation package deals from Chicago listed on Expedia.");
    //     return aiChatService.getStructuredVacationDeals(prompt);
    // }
}
