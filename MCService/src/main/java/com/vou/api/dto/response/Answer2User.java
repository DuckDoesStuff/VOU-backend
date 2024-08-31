package com.vou.api.dto.response;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@ToString
public class Answer2User {
    String question;
    String answer; //answer = answers[correctAnswer]
    int order;
}
