package com.vou.api.dto.stream;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

@FieldDefaults(level =AccessLevel.PRIVATE)
@Getter
public class Script {
    String intro = "Welcome to VOU - gamification marketing platform, play and earn valuable gifts with us";
    List<String> script;
}
