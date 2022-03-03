package com.vahidsaghlatoon.homeservicesystem.dto;

import com.vahidsaghlatoon.homeservicesystem.model.MainService;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Builder
public class MainServiceDto {
    private Long id;
    @NotBlank(message = "پر کردن این فیلد اجباری است")
    @Pattern(regexp = "^[\u0600-\u06FF\uFB8A\u067E\u0686\u06AF\u200C\u200F ]+$"
            , message = "فقط از حروف فارسی استفاده نمایید")
    private String title;
    @NotBlank(message = "پر کردن این فیلد اجباری است")
    @Pattern(regexp = "^[\u0600-\u06FF\uFB8A\u067E\u0686\u06AF\u200C\u200F ]+$"
            , message = "فقط از حروف فارسی استفاده نمایید")
    private String description;

    public Long getId() {
        return id;
    }

    public MainService MainServiceDto2MainService( MainServiceDto theMainServiceDto){
        return MainService.builder()
                .title(theMainServiceDto.getTitle())
                .description(theMainServiceDto.getDescription())
                .build();
    }

}
