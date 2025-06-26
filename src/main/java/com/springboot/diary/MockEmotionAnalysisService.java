package com.springboot.diary;

import com.springboot.model.Emotion;
import org.springframework.stereotype.Service;

@Service
public class MockEmotionAnalysisService implements EmotionAnalysisService {
    @Override
    public Emotion analyzeEmotion(String text) {
        if (text == null) {
            return Emotion.NEUTRAL;
        }
        String lower = text.toLowerCase();
        if (lower.contains("행복") || lower.contains("기뻐") || lower.contains("좋아")) {
            return Emotion.POSITIVE;
        }
        if (lower.contains("슬퍼") || lower.contains("우울") || lower.contains("힘들")) {
            return Emotion.NEGATIVE;
        }
        return Emotion.NEUTRAL;
    }
}
