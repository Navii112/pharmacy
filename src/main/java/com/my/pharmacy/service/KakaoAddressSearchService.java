package com.my.pharmacy.service;

import com.my.pharmacy.dto.KakaoApiResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoAddressSearchService {
    // postman 처럼 api 요청에 필요한 값을 세팅한 후 호출해 주는
    // 결과를 DTO에 담아주는 역할.
    private final RestTemplate restTemplate;

    // 환경변수에서 ${KAKAO_REST_API_KEY} 값을 가져와서 변수 저장
    // yml 파일에 있는 계층값을 순서대로 . 으로 연결해서 표현.
    @Value("${kakao.rest.api.key}")
    private String kakaoRestApiKey;

    // https://dapi.kakao.com/v2/local/search/address?query=중부대로 100
    // 주소검색에 필요한 기초 URL을 상수로 선언
    private static final String KAKAO_LOCAL_URL = "https://dapi.kakao.com/v2/local/search/address";

    public KakaoApiResponseDto requestAddressSearch(String address) {
        log.info("Kakao-Api-Key : " + kakaoRestApiKey);
        log.info("address : " + address);
        // 넘어오는 주소값이 비었는지 확인
        if (ObjectUtils.isEmpty(address)) return null;

        // 1. URL 만들기
        //UriBuilder : URL 구성에 필요한 값을 붙여서 완성형 URL을 생성
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(KAKAO_LOCAL_URL);
        uriBuilder.queryParam("query", address);
        // url에 포함된 한글을 UTF-8 인코딩 처리
        URI uri = uriBuilder.build().encode().toUri();
        log.info("uri : " + uri);
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
