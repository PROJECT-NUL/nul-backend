package com.springboot.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DiaryRequest {
    @NotEmpty(message = "{diary_content_not_empty}")
    private String content;
}
