package com.springboot.diary;

import com.springboot.dto.DiaryRequest;
import com.springboot.dto.DiaryResponse;
import com.springboot.mapper.DiaryMapper;
import com.springboot.model.Couple;
import com.springboot.model.Diary;
import com.springboot.model.User;
import com.springboot.repository.CoupleRepository;
import com.springboot.repository.DiaryRepository;
import com.springboot.repository.UserRepository;
import com.springboot.security.utils.SecurityConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

    private final UserRepository userRepository;
    private final DiaryRepository diaryRepository;
    private final CoupleRepository coupleRepository;
    private final EmotionAnalysisService emotionAnalysisService;
    private final NotificationService notificationService;

    @Override
    public DiaryResponse createDiary(DiaryRequest request) {
        String username = SecurityConstants.getAuthenticatedUsername();
        User author = userRepository.findByUsername(username);

        String emotion = emotionAnalysisService.analyzeEmotion(request.getContent());
        Diary diary = Diary.builder()
                .author(author)
                .content(request.getContent())
                .emotion(emotion)
                .createdAt(LocalDateTime.now())
                .build();
        diaryRepository.save(diary);

        Optional<Couple> coupleOpt = coupleRepository.findByPartner1OrPartner2(author, author);
        coupleOpt.ifPresent(couple -> {
            User partner = couple.getPartner1().equals(author) ? couple.getPartner2() : couple.getPartner1();
            if (partner != null) {
                notificationService.notifyPartner(partner, diary);
            }
        });

        return DiaryMapper.INSTANCE.convertToDiaryResponse(diary);
    }
}
