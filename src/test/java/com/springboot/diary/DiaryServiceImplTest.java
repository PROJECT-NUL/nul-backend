package com.springboot.diary;

import com.springboot.dto.DiaryRequest;
import com.springboot.dto.DiaryResponse;
import com.springboot.model.Couple;
import com.springboot.model.Diary;
import com.springboot.model.Emotion;
import com.springboot.model.User;
import com.springboot.repository.CoupleRepository;
import com.springboot.repository.DiaryRepository;
import com.springboot.repository.UserRepository;
import com.springboot.security.utils.CurrentUserFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiaryServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private DiaryRepository diaryRepository;
    @Mock
    private CoupleRepository coupleRepository;
    @Mock
    private EmotionAnalysisService emotionAnalysisService;
    @Mock
    private NotificationService notificationService;
    @Mock
    private CurrentUserFacade currentUserFacade;

    @InjectMocks
    private DiaryServiceImpl diaryService;

    @Test
    void createDiary_savesDiaryAndNotifiesPartner() {
        DiaryRequest request = new DiaryRequest();
        request.setContent("행복한 하루");

        User author = new User();
        when(currentUserFacade.getCurrentUsername()).thenReturn("user");
        when(userRepository.findByUsername("user")).thenReturn(author);
        when(emotionAnalysisService.analyzeEmotion("행복한 하루")).thenReturn(Emotion.POSITIVE);

        User partner = new User();
        Couple couple = Couple.builder().partner1(author).partner2(partner).build();
        when(coupleRepository.findByPartner1OrPartner2(author, author)).thenReturn(Optional.of(couple));

        DiaryResponse response = diaryService.createDiary(request);

        assertEquals(Emotion.POSITIVE, response.getEmotion());
        verify(diaryRepository).save(any(Diary.class));
        verify(notificationService).notifyPartner(eq(partner), any(Diary.class));
    }
}
