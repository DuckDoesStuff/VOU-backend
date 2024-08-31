package com.vou.api.mapper;

import com.vou.api.dto.response.Answer2User;
import com.vou.api.dto.response.Question2User;
import com.vou.api.dto.stream.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    Question2User questionToQuestion2User(Question question);

    @Mappings({
            @Mapping(target = "question", source = "question.question"),
            @Mapping(target = "answer", expression = "java(question.getAnswers()[question.getCorrectAnswer()])")
    })
    Answer2User questionToAnswer2User(Question question);
}
