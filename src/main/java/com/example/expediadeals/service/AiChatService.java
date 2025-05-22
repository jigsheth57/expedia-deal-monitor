package com.example.expediadeals.service;

import com.example.expediadeals.functions.ExpediaToolFunctions;
import com.example.expediadeals.model.VacationDeal;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AiChatService {

    private final ChatClient chatClient;

    // Autowire the ChatClient.Builder and build the ChatClient
    // specifying the function(s) to be available.
    public AiChatService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultFunctions("findExpediaVacationDeals") // Name of the bean defined in ExpediaToolFunctions
                .build();
    }

    public String getVacationDealsFromAi(String userPrompt) {
        // The user prompt will be sent to the LLM.
        // If the LLM decides the "findExpediaVacationDeals" function is appropriate,
        // Spring AI will call it.
        ChatResponse response = chatClient.prompt()
                .user(userPrompt)
                .call()
                .chatResponse();

        // The response might be a direct answer or the result of the function call processed by the AI.
        // If a function was called, the AI might summarize or present the data.
        // You might need to inspect response.getResult().getOutput().getContent()
        // and potentially response.getResults().get(0).getMetadata() for function call details if needed.

        String content = response.getResult().getOutput().getContent();

        // Check if the content is already structured (e.g., if the AI formatted the list of deals)
        // Or if it's a simple string and you need to do further processing.
        // For this example, we'll assume the AI formats it or gives a summary.

        System.out.println("AI Raw Response: " + content);
        System.out.println("AI Response Metadata: " + response.getResult().getMetadata());


        // If you want to directly access the function call result (List<VacationDeal>)
        // this is a bit more involved as the primary `content` is the AI's final textual response.
        // Typically, the function's output is sent back to the AI which then formulates the final response.
        // If the `VacationDeal` objects themselves were directly returned and you need to process them here,
        // you'd need a more complex setup or to adjust how functions are registered and handled.
        // For now, we assume the AI processes the list and returns a textual summary in `content`.

        return content;
    }


    public List<VacationDeal> getStructuredVacationDeals(String userPrompt) {
         // This is a more advanced scenario where you expect the function to be called
         // and want to retrieve the structured data directly.
         // Spring AI's `ChatClient` primarily returns the AI's textual response.
         // To get structured output directly from a function call when the AI decides to call it,
         // you often rely on the AI to format it as JSON within its response, or
         // you might need to use lower-level Spring AI APIs if the function's result
         // isn't directly embedded as the primary AI output in a structured way you can easily parse.

         // For simplicity, let's assume the function is called and the AI's response
         // *contains* information about the deals. For a truly structured return
         // from this service method, you might need to parse the AI's response if it's text,
         // or if the function call result is directly available in the metadata in a usable format.

        ChatResponse response = chatClient.prompt()
                .user(userPrompt)
                .call()
                .chatResponse();

        // This is a simplification. In a real scenario, if the function `findExpediaVacationDeals`
        // was called, its `List<VacationDeal>` result was sent back to the LLM.
        // The LLM then generates the final text response.
        // Getting that List<VacationDeal> directly here requires either:
        // 1. The LLM formatting its output as JSON that you parse into List<VacationDeal>.
        // 2. Intercepting the function call result before it goes back to the LLM (more complex).

        // For now, let's simulate that the AI response is good enough or we parse it.
        // A better approach for structured output is to use Spring AI's structured output features
        // if the LLM can be prompted to return JSON matching your VacationDeal structure.

        String aiTextResponse = response.getResult().getOutput().getContent();
        System.out.println("AI Response for structured attempt: " + aiTextResponse);

        // This is a placeholder. You would need robust parsing if the AI doesn't return clean JSON.
        // Or, ensure your function description and prompt guides the AI to return data in a way
        // that the `VacationDeal` list is clearly identifiable or formatted.
        // For now, returning null as a proper implementation would be more involved.
        System.out.println("Note: Directly getting List<VacationDeal> here is simplified. " +
                           "Typically, the AI provides a textual response incorporating the function's findings.");
        return null; // Placeholder for actual structured data retrieval logic
    }
}
