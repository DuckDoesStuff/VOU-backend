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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        NotificationServiceType pushService = notificationFactory.getNotificationService(NotificationType.PushNotification.getNotificationType());
        Notification notification = notificationMapper.toNotification(notificationRequest);
        notification.setDate(LocalDate.now());
        notification.setNotificationType(NotificationType.PushNotification.getNotificationType());
        try {
            pushService.sendTopicNotification(notification);
        } catch(AppException e) {
            throw new AppException(ErrorCode.CANNOT_SEND_PUSHMESSAGE);
        }
        notificationRepository.save(notification);
        return "Successfully sent push notification";
    }

    public String subscribeToNewTopic(SubscribeTopicRequest subscribeTopicRequest) {
        // Thêm ngày subscribe cho người dùng
        UserSubscribe newUserSubscribe = userSubscribeMapper.toUserSubscribe(subscribeTopicRequest);
        newUserSubscribe.getSubscribeList().forEach(item -> item.setDateSubscribe(LocalDate.now()));

        Optional<UserSubscribe> currentUserSubscribe = userSubscribeRepository.findById(newUserSubscribe.getUserID());
        UserSubscribe userSubscribe =  newUserSubscribe;
        if (currentUserSubscribe.isPresent()) {
            userSubscribe = currentUserSubscribe.get();
            userSubscribe.getSubscribeList().addAll(newUserSubscribe.getSubscribeList());
        }

        try {
            userSubscribeRepository.save(userSubscribe);
        } catch(Exception e) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
        return "Successfully subscribed";
    }

    public String unSubcribeToTopic(SubscribeTopicRequest unsubcribeTopicRequest) {
        UserSubscribe newUnsubcribe = userSubscribeMapper.toUserSubscribe(unsubcribeTopicRequest);
        newUnsubcribe.getSubscribeList().forEach(item -> item.setDateUnsubscribe(LocalDate.now()));
        Optional<UserSubscribe> currentUserSubscribe = userSubscribeRepository.findById(newUnsubcribe.getUserID());
        if (currentUserSubscribe.isEmpty()) return "No subscribed topics found";

        currentUserSubscribe.get().getSubscribeList().forEach(item -> {
            int index = newUnsubcribe.getSubscribeList().indexOf(item);
            if (index!= -1) {
                currentUserSubscribe.get().getSubscribeList().get(index).setDateUnsubscribe(LocalDate.now());
            }
        });
        userSubscribeRepository.save(currentUserSubscribe.get());

        return "Successfully unsubscribed";
    }

    public List<UserSubscribe.SubscribeItem> getPushTopicOfUser(String userID) {
        Optional<UserSubscribe> currentUserSubscribe = userSubscribeRepository.findById(userID);
        if (currentUserSubscribe.isEmpty()) return new ArrayList<>();
        List<UserSubscribe.SubscribeItem> subscribeItems = currentUserSubscribe.get().getSubscribeList()
                .stream()
                .filter(item -> item.getDateUnsubscribe() == null)
                .toList();
        return  subscribeItems;
    }

    public ArrayList<Notification> getPushNotificationOfUser(String userID){
        Optional<UserSubscribe> optionalUserSubscribe = userSubscribeRepository.findByUserID(userID);
        if (optionalUserSubscribe.isEmpty()) return null;
        ArrayList<Notification> notifications = new ArrayList<>();

        List<UserSubscribe.SubscribeItem> userSubscribeList = optionalUserSubscribe.get().getSubscribeList().stream()
                    .filter(subscribeItem -> NotificationType.PushNotification.getNotificationType().equals(subscribeItem.getTypeSubscribe()))
                    .toList();

        Map<String, UserSubscribe.SubscribeItem> subscribeItem = userSubscribeList.stream()
                .collect(Collectors.toMap(UserSubscribe.SubscribeItem::getTopic, item -> item));

        //List topic thôi
        List<String> subscribedTopics = userSubscribeList.stream()
                .map(UserSubscribe.SubscribeItem::getTopic)
                .collect(Collectors.toList());



        // Truy vấn tất cả các thông báo của các topic này một lần
        List<Notification> allNotifications = notificationRepository.findByTopicIn(subscribedTopics);

        //Lọc thông báo theo ngày giờ người dùng subscribe
        notifications.addAll(allNotifications.stream()
                .filter(notification -> {
                    LocalDate notificationDate = notification.getDate();
                    LocalDate subscribeStart = subscribeItem.get(notification.getTopic()).getDateSubscribe(); // Thời gian bắt đầu đăng ký
                    LocalDate subscribeEnd = subscribeItem.get(notification.getTopic()).getDateUnsubscribe(); // Thời gian kết thúc đăng ký
                    return (subscribeStart == null || !notificationDate.isBefore(subscribeStart)) &&
                            (subscribeEnd == null || !notificationDate.isAfter(subscribeEnd));
                }).toList());

//        notifications.addAll(notificationRepository.findByReceiverContaining(userID));
        return notifications;
    }

}
