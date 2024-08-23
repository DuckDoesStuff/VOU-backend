package com.vou.api.scheduled;

import com.vou.api.custom.HeyGenAPI;
import com.vou.api.entity.Game;
import com.vou.api.repository.GameRepository;
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
                game.setQuizState(allQuizAreReady(game.getQuestions()) ? "READY" : "PREPARING");
                gameRepository.save(game);
                updated = false;
            }
        }
        if (count != 0)
            log.info("HeyGenScheduler updated {} quizzes", count);
    }

    private boolean allQuizAreReady(List<Game.Question> questions) {
        boolean flag = true;
        for(Game.Question question: questions) {
            if(!Objects.equals(question.getVideoStatus(), "completed")) {
                flag = false;
                break;
            }
        }

        return flag;
    }
}
