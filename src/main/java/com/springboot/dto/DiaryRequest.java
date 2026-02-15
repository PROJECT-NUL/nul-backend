package com.springboot.dto;

import com.springboot.model.Diary;
import com.springboot.model.Emotion;
import com.springboot.model.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class DiaryRequest {
    @NotEmpty(message = "{diary_content_not_empty}")
    private String content;

    public Diary toDiary(User author, Emotion emotion) {
        return Diary.builder()
                .author(author)
                .content(content)
                .emotion(emotion)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
