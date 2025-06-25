package com.springboot.diary;

import com.springboot.model.User;
import com.springboot.model.Diary;

public interface NotificationService {
    void notifyPartner(User partner, Diary diary);
}
