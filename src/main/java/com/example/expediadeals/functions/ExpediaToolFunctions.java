package com.example.expediadeals.functions;

import com.example.expediadeals.model.VacationDeal;
import com.example.expediadeals.service.ExpediaScraperService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.List;
import java.util.function.Function;

@Configuration
public class ExpediaToolFunctions {

    // Inject the scraper service
    private final ExpediaScraperService expediaScraperService;

    public ExpediaToolFunctions(ExpediaScraperService expediaScraperService) {
        this.expediaScraperService = expediaScraperService;
    }

    @Bean
    @Description("Get current vacation deals from Expedia for a specified origin city, typically Chicago.")
    public Function<ExpediaToolFunctions.ExpediaScraperRequest, List<VacationDeal>> findExpediaVacationDeals() {
        return request -> {
            // Log the request received by the function
            System.out.println("AI requested to find Expedia deals with parameters: " + request);
            return expediaScraperService.findVacationDeals(request.originCity());
        };
    }

    // Define a record or class for the function's request parameters
    // The LLM will try to populate these fields based on the user's prompt.
    public record ExpediaScraperRequest(
        @Description("The city from which the vacation should originate. Default to Chicago if not specified by user.") String originCity
    ) {}
}
