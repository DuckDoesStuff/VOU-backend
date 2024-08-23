package com.vou.api.dto.stream;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Question {
    String question;
    String[] answer;
    int correctAnswer;
}
