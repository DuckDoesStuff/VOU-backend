package com.example.GameService.scheduled;

import com.example.GameService.custom.HeyGenAPI;
import com.example.GameService.entity.Game;
import com.example.GameService.repository.GameRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class HeyGenScheduler {
    @Autowired
    GameRepository gameRepository;

    @Autowired
    HeyGenAPI heyGenAPI;

    @Scheduled(fixedDelay = 60 * 1000)
    public void getAllGames() {
        List<Game> quizGames = gameRepository.findGamesByVideoStatus("processing");
        boolean updated = false;
        int count = 0;
        for (Game game : quizGames) {
            for (Game.Question question : game.getQuestions()) {
                if(Objects.equals(question.getVideoStatus(), "processing")) {
                    String url = heyGenAPI.getVideoIfCompleted(question.getVideo());
                    if (url != null) {
                        question.setVideo(url);
                        question.setVideoStatus("completed");
                        updated = true;
                    }
                }
            }

            if (updated) {
                count++;
                gameRepository.save(game);
                updated = false;
            }
        }
        log.info("HeyGenScheduler scanned and updated {} videos", count);
    }

}
