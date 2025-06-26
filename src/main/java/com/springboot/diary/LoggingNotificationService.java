package com.springboot.diary;

import com.springboot.model.Diary;
import com.springboot.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoggingNotificationService implements NotificationService {
    @Override
    public void notifyPartner(User partner, Diary diary) {
        log.info("Notify {} about new diary {} with emotion {}", partner.getUsername(), diary.getId(), diary.getEmotion());
    }
}
