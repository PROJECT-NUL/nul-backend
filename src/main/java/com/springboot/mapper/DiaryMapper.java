package com.springboot.mapper;

import com.springboot.dto.DiaryResponse;
import com.springboot.model.Diary;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DiaryMapper {
    DiaryMapper INSTANCE = Mappers.getMapper(DiaryMapper.class);

    DiaryResponse convertToDiaryResponse(Diary diary);
}
