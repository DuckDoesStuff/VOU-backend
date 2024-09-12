package com.vou.api.service;

import com.vou.api.dto.GiveUserVoucherMessage;
import com.vou.api.dto.PushTopicNotificationRequest;
import com.vou.api.dto.UserActivityMessage;
import com.vou.api.entity.Participant;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class KafkaService<T> {

    private final KafkaTemplate<String, T> kafkaTemplate;

    public KafkaService(KafkaTemplate<String, T> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String topic, T message) {
        kafkaTemplate.send(topic, message);
    }
    public void sendUserJoinShakeGame(Participant participant) {
        UserActivityMessage message = UserActivityMessage.builder()
                .userID(participant.getUserID())
                .gameID(participant.getGameID().toString())
                .eventID(participant.getEventID())
                .activityType("join_shake_game")
                .gameType("shake_game")
                .rewardType("shake_item")
                .joinTime(LocalDateTime.now())
                .build();
        System.out.println(message);
        kafkaTemplate.send("user_join_shake_game", (T) message);
    }
    public void sendUserJoinQuizGame(Participant participant) {
        UserActivityMessage message = UserActivityMessage.builder()
                .userID(participant.getUserID())
                .gameID(participant.getGameID().toString())
                .eventID(participant.getEventID())
                .activityType("join_quiz_game")
                .gameType("quiz_game")
                .rewardType("item_type")
                .joinTime(LocalDateTime.now())
                .build();
        System.out.println(message);
        kafkaTemplate.send("user_join_quiz_game", (T) message);
    }

    public void sendUserAGiftUserBAnItem(String userA, String userB) {
        PushTopicNotificationRequest message = PushTopicNotificationRequest
                .builder()
                .title("Receive a gift from friends")
                .content("You just received a gift from user: " + userB)
                .build();
        kafkaTemplate.send("gave_a_gift", (T) message);
    }

    public void giveUserVoucher(GiveUserVoucherMessage message) {
        System.out.println("Send give-user-voucher message from GameService" + message);
        kafkaTemplate.send("give-user-voucher", (T) message);
    }
}