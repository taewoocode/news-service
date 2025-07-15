package com.example.news_service.news.client;

import java.util.List;

import org.springframework.stereotype.Component;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.OpenAiHttpException;
import com.theokanning.openai.service.OpenAiService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class GptApiClient {

    private final OpenAiService openAiService;

    public String summarizeText(String prompt) {
        try {
            log.info("GPT 요약 요청 시작...");
            
            ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(List.of(new ChatMessage(ChatMessageRole.USER.value(), prompt)))
                .maxTokens(1024)
                .temperature(0.7)
                .build();
            
            ChatMessage responseMessage = openAiService.createChatCompletion(request).getChoices().get(0).getMessage();
            String summary = responseMessage.getContent();
            
            log.info("GPT 요약 응답 성공");
            return summary;
            
        } catch (OpenAiHttpException e) {
            log.warn("GPT API 호출 중 HTTP 오류 발생: {}", e.getMessage());
            if (e.getMessage().contains("quota")) {
                return "GPT API 사용량을 초과하여 요약을 생성할 수 없습니다. 관리자에게 문의하세요.";
            }
            return "GPT API 호출 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.";
        } catch (Exception e) {
            log.error("GPT API 요약 중 알 수 없는 오류 발생: {}", e.getMessage(), e);
            return "기사 요약 중 알 수 없는 오류가 발생했습니다.";
        }
    }
} 