package com.vou.api.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@ToString
public class Answer2User {
    String question;
    String answer; //answer = answers[correctAnswer]
    int order;
}
