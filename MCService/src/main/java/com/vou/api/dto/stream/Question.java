package com.vou.api.dto.stream;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Question {
    String question;
    String[] answers;
    int correctAnswer;
}
