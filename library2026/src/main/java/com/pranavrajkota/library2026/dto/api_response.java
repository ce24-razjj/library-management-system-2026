package com.pranavrajkota.library2026.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class api_response<T> {
    private String status;
    private T data;
    private LocalDateTime timeStamp;

}
