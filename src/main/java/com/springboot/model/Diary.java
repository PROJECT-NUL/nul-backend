package com.springboot.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.springboot.dto.DiaryResponse;
import com.springboot.model.Emotion;

/** Diary entity storing user's diary entries with associated emotion. */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "DIARY")
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private Emotion emotion;

    private LocalDateTime createdAt;

    public DiaryResponse toDiaryResponse() {
        return new DiaryResponse(id, content, emotion);
    }
}
