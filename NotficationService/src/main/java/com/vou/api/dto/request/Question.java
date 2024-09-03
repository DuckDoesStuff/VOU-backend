package com.vou.api.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Question {
    String question;
    String[] answers;
    int correctAnswer;
}
