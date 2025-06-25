package com.springboot.diary;

import com.springboot.dto.DiaryRequest;
import com.springboot.dto.DiaryResponse;

public interface DiaryService {
    DiaryResponse createDiary(DiaryRequest request);
}
