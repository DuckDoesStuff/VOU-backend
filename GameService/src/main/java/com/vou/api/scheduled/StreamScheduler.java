package com.vou.api.scheduled;

import com.vou.api.dto.request.InfoForStream;
import com.vou.api.repository.GameRepository;
import com.vou.api.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;

public class StreamScheduler {
    @Autowired
    GameRepository gameRepository;

    @Autowired
    KafkaService<InfoForStream> infoForStreamKafkaService;


}
