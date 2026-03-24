package com.pranavrajkota.library2026.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    @NotBlank(message = "there must be some name of the book")
    private String bookName;

    @NotBlank(message = "author name can't be empty")
    private String authorName;

    private List<String> categories;


}