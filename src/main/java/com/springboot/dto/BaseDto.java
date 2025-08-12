package com.springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Common DTO carrying auditing information.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseDto {

    private LocalDateTime createdAt;

    private String createdBy;

    private LocalDateTime updatedAt;

    private String updatedBy;
}
