package com.example.expediadeals;

import com.example.expediadeals.service.AiChatService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ExpediaDealMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExpediaDealMonitorApplication.class, args);
    }

    // Optional: CommandLineRunner for easy testing from the console
    @Bean
    public CommandLineRunner demo(AiChatService aiChatService) {
        return args -> {
            System.out.println("App started. Asking AI for deals...");
            String prompt = "Can you find some vacation packages from Chicago on Expedia for me?";
            // String prompt = "What are some good vacation deals departing from Chicago via Expedia?";

            try {
                String response = aiChatService.getVacationDealsFromAi(prompt);
                System.out.println("\n--- AI Response ---");
                System.out.println(response);
                System.out.println("--- End of AI Response ---");
            } catch (Exception e) {
                System.err.println("Error during AI chat demo: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}
