package com.springboot.diary;

import com.springboot.model.Emotion;

public interface EmotionAnalysisService {
    Emotion analyzeEmotion(String text);
}
