package com.springboot.dto;

import com.springboot.model.Emotion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiaryResponse {
    private Long id;
    private String content;
    private Emotion emotion;
}
