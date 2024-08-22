package com.example.GameService.custom;

import com.example.GameService.dto.heygen.GenerateHeyGenRequest;
import static com.example.GameService.dto.heygen.GenerateHeyGenRequest.Dimension;
import static com.example.GameService.dto.heygen.GenerateHeyGenRequest.VideoInput;
import static com.example.GameService.dto.heygen.GenerateHeyGenRequest.VideoInput.Character;
import static com.example.GameService.dto.heygen.GenerateHeyGenRequest.VideoInput.Voice;
import com.example.GameService.dto.heygen.GenerateHeyGenResponse;
import com.example.GameService.dto.heygen.RetrieveHeyGenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class HeyGenAPI {
    @Value("${heygen.api_key}")
    private String apiKey;

    private final String createURL = "https://api.heygen.com/v2/video/generate";
    private final String retrieveURL = "https://api.heygen.com/v1/video_status.get";

    private final GenerateHeyGenRequest.GenerateHeyGenRequestBuilder defaultGenerateRequest = GenerateHeyGenRequest.builder()
            .dimension(new Dimension(600, 600));

    public WebClient.Builder heyGenClientBuilder() {
        return WebClient.builder();
    }


    // Not ready
    public String generateVideo(String question, String eventID, String gameID, String questionNum) {
        WebClient webClient = heyGenClientBuilder().build();


        Character mason = new Character("avatar", "Mason_public_20240304","normal");
        GenerateHeyGenRequest requestBody = defaultGenerateRequest.build();
        requestBody.setTitle(eventID + "-" + gameID + "-" + questionNum);
        requestBody.setVideo_inputs(new ArrayList<>(List.of(
                new VideoInput(
                        mason,
                        Voice.builder()
                                .type("text")
                                .input_text(question)
                                .voice_id("3fbd2cac3ddd4c109e17296e324845ec")
                                .build()
                ),
                new VideoInput(
                        mason,
                        Voice.builder()
                                .type("silence")
                                .duration("10.0")
                                .build()
                )
        )));

        GenerateHeyGenResponse result = webClient.post()
                .uri(createURL)
                .header("X-Api-Key", apiKey)
                .body(Mono.just(requestBody), GenerateHeyGenRequest.class)
                .retrieve()
                .bodyToMono(GenerateHeyGenResponse.class)
                .block();

        assert result != null;
        return result.getData().getVideo_id();
    }

    public String getVideoIfCompleted(String videoID) {
        WebClient webClient = heyGenClientBuilder().build();

        RetrieveHeyGenResponse result = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("api.heygen.com")
                        .path("/v1/video_status.get")
                        .queryParam("video_id", videoID)
                        .build())
                .header("X-Api-Key", apiKey)
                .retrieve()
                .bodyToMono(RetrieveHeyGenResponse.class)
                .block();

        assert result != null;
        return result.getData().getVideo_url();
    }
}
