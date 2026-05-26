package com.my.pharmacy.controller;

import com.my.pharmacy.dto.DocumentDto;
import com.my.pharmacy.service.KakaoAddressSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class FormController {
    private final KakaoAddressSearchService kakaoAddressSearchService;

    @GetMapping
    public String mainForm() {
        return "main";
    }

    @GetMapping("/output")
    public String outputForm() {
        return "output";
    }

    @PostMapping("/search")
    public String searchAddress(@RequestParam("address")String address) {
        kakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch();
        log.info("결과 : " + kakaoApiResponseDto);
        //
        DocumentDto documentDto = kakaoApiResponseDto
                .getDocumentList().get(0);
        return "output";
    }
}
