package com.springboot.repository;

import com.springboot.model.Diary;
import com.springboot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    List<Diary> findByAuthor(User author);
}
