package com.springboot.controller;

import com.springboot.diary.DiaryService;
import com.springboot.dto.DiaryRequest;
import com.springboot.dto.DiaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diaries")
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping
    @Operation(tags = "Diary Service", description = "Create new diary entry")
    public ResponseEntity<DiaryResponse> createDiary(@Valid @RequestBody DiaryRequest request) {
        DiaryResponse response = diaryService.createDiary(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
