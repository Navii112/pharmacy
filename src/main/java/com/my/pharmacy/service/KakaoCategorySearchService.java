package com.my.pharmacy.service;

import com.my.pharmacy.dto.KakaoApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class KakaoCategorySearchService {

    private final RestTemplate restTemplate;

    @Value("${kakao.rest.api.key}")
    private String kakaoRestApiKey;

    private static final String KAKAO_CATEGORY_URL = "https://dapi.kakao.com/v2/local/search/category";
    // 카테고리 상수
    private static final String CATEGORY = "PM9";

    // 수정: static 키워드 제거
    public KakaoApiResponseDto resultCategorySearch(double latitude, double longtitude) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(KAKAO_CATEGORY_URL);
        // 1. 카테고리
        uriBuilder.queryParam("category_group_code", CATEGORY);
        // 2. x값, y값
        uriBuilder.queryParam("x", longtitude);
        uriBuilder.queryParam("y", latitude);
        // 3. 검색 반경
        uriBuilder.queryParam("radius", 1000);
        // 4. 검색 사이즈 - 나중에 처리

        // 5. 정렬처리
        uriBuilder.queryParam("sort", "distance");

        // url에 포함된 한글을 UTF-8 인코딩 처리
        URI uri = uriBuilder.build().encode().toUri();

        // 헤더 작업
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "KakaoAK "+ kakaoRestApiKey);
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        // 카카오 api 호출
        return restTemplate
                .exchange(
                        uri,
                        HttpMethod.GET,
                        httpEntity,
                        KakaoApiResponseDto.class
                ).getBody();
    }
}