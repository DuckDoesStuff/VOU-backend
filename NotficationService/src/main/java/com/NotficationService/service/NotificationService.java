package com.NotficationService.service;



import com.NotficationService.dto.request.PushTopicNotificationRequest;
import com.NotficationService.dto.request.SubscribeTopicRequest;
import com.NotficationService.entity.Notification;
import com.NotficationService.entity.NotificationType;
import com.NotficationService.entity.UserSubscribe;
import com.NotficationService.exception.AppException;
import com.NotficationService.exception.ErrorCode;
import com.NotficationService.mapper.NotificationMapper;
import com.NotficationService.mapper.UserSubscribeMapper;
import com.NotficationService.repository.NotificationRepository;
import com.NotficationService.repository.UserSubscribeRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationService {
    NotificationServiceFactory notificationFactory;
    //Repository
    NotificationRepository notificationRepository;
    UserSubscribeRepository userSubscribeRepository;
    // Mapper
    NotificationMapper notificationMapper;
    UserSubscribeMapper userSubscribeMapper;

    public String sendGeneralPushNotification(PushTopicNotificationRequest notificationRequest) {
        NotificationServiceType notificationService = notificationFactory.getNotificationService(NotificationType.PushNotification.getNotificationType());
        Notification notification = notificationMapper.toNotification(notificationRequest);
        notification.setDate(LocalDate.now());
        try {
            notificationService.sendTopicNotification(notification);
        } catch(AppException e) {
            throw new AppException(ErrorCode.CANNOT_SEND_PUSHMESSAGE);
        }
        notificationRepository.save(notification);
        return "Successfully sent push notification";
    }

    public void subscribeToNewTopic(SubscribeTopicRequest subscribeTopicRequest) {
        // Tìm người dùng theo userId
        UserSubscribe newUserSubscribe = userSubscribeMapper.toUserSubscribe(subscribeTopicRequest);
        UserSubscribe currentUserSubscribe = userSubscribeRepository.findById(newUserSubscribe.getUserID()).orElse(new UserSubscribe());

        // Nếu chưa có danh sách subcribeList, khởi tạo nó
        if (currentUserSubscribe.getSubscribeList() == null) {
            currentUserSubscribe.setSubscribeList(new ArrayList<>());
        }
        List<UserSubscribe.SubscribeItem> subscribeItemsCanBeAdded = newUserSubscribe.getSubscribeList().stream()
//                .filter(subscribeItem -> ())
                .collect(Collectors.toList());

        // Thêm SubscribeItem mới vào danh sách
       currentUserSubscribe.getSubscribeList().addAll(subscribeItemsCanBeAdded);

        // Lưu lại đối tượng UserSubscribe
        userSubscribeRepository.save(currentUserSubscribe);
    }


    public ArrayList<Notification> getPushNotificationOfUser(String userID){
        ArrayList<Notification> notifications = new ArrayList<>();
        Optional<UserSubscribe> optionalUserSubscribe = userSubscribeRepository.findByUserID(userID);
        if (optionalUserSubscribe.isPresent()) {
            UserSubscribe userSubscribe = optionalUserSubscribe.get();
            List<String> subscribedTopics = userSubscribe.getSubscribeList().stream()
                    .filter(subscribeItem -> NotificationType.PushNotification.getNotificationType().equals(subscribeItem.getTypeSubscribe()))
                    .map(UserSubscribe.SubscribeItem::getTopic)
                    .collect(Collectors.toList());
            notifications.addAll(notificationRepository.findByTopicIn(subscribedTopics));
        }
        notifications.addAll(notificationRepository.findByReceiverContaining(userID));
        return notifications;
    }

}
