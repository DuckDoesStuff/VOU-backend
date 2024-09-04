package com.vou.api.scheduled;

import com.vou.api.dto.request.InfoForStream;
import com.vou.api.entity.Game;
import com.vou.api.repository.GameRepository;
import com.vou.api.service.KafkaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class StreamScheduler {
    @Autowired
    GameRepository gameRepository;

    @Autowired
    KafkaService<InfoForStream> infoForStreamKafkaService;

    @Scheduled(fixedDelay = 30 * 1000)
    public void scanForStream() {
        List<Game> games = gameRepository.findGamesReadyToStream(LocalDateTime.now());

        int count = 0;
        for (Game game : games) {
            String[] videoUrl = game.getQuestions().stream()
                    .map(Game.Question::getVideo)
                    .toArray(String[]::new);

            InfoForStream info = new InfoForStream();
            info.setVideoUrl(videoUrl);
            info.setGameID(game.getGameID().toString());
            info.setGameName(game.getNameOfGame());
            info.setEventID(game.getEventID().toString());
            info.setEventName(game.getEventName());
            info.setEventBanner(game.getEventBanner());
            info.setQuestions(game.getQuestions());

            infoForStreamKafkaService.send("startStream", info);
            game.setQuizState("ENDED");
            gameRepository.save(game);
            count++;
        }

        if (count != 0)
            log.info("Stream scheduler started streaming {} games", count);
    }
}
