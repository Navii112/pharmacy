package com.my.pharmacy.controller;

import com.my.pharmacy.dto.DocumentDto;
import com.my.pharmacy.dto.KakaoApiResponseDto; // [추가] 해당 DTO 임포트 필요 (경로는 실제 프로젝트에 맞게 수정)
import com.my.pharmacy.dto.OutputDto;
import com.my.pharmacy.service.KakaoAddressSearchService;
import com.my.pharmacy.service.KakaoCategorySearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class FormController {

    private final KakaoAddressSearchService kakaoAddressSearchService;
    private final KakaoCategorySearchService kakaoCategorySearchService;

    @GetMapping("/")
    public String mainForm() {
        return "main";
    }

    @GetMapping("/output")
    public String outputForm() {
        return "output";
    }

    @PostMapping("/search")
    public String searchAddress(@RequestParam("address") String address,
                                Model model) {
        // 1. 입력받은 주소로 위/경도 값 얻어오기
        KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(address);
        log.info("결과 : " + kakaoApiResponseDto);

        // 결과 중 Documents만 빼서 dto 저장
        DocumentDto documentDto = kakaoApiResponseDto.getDocumentList().get(0);
        log.info("도큐먼트만 출력 : " + documentDto);

        // 2. 카카오 카테고리 서비스로 반경 1km이하 약국 정보 얻어오기
        KakaoApiResponseDto kakaoApiCategoryDto = kakaoCategorySearchService.resultCategorySearch(
                documentDto.getLatitude(), documentDto.getLongitude());

        log.info("카테고리 검색 결과 : " + kakaoApiCategoryDto);

        // 3. 출력 양식 Dto에 맞춰서 변환한 후 모델에 담아 출력폼으로 보낸다.
        List<OutputDto> outputDtoList =
                kakaoCategorySearchService.makeOutputDto(
                        kakaoApiCategoryDto.getDocumentList()
                );
        model.addAttribute("outputList", outputDtoList);
        return "output";
    }
}