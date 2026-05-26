package com.my.pharmacy.service;

import com.my.pharmacy.dto.DocumentDto;
import com.my.pharmacy.dto.KakaoApiResponseDto;
import com.my.pharmacy.dto.OutputDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

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

    // documentList를 받아서 OutputDto의 리스트로 변환
    // 각 documentList 안에 있는 DocumentDto -> OutputDto 변환 후
    // 다시 OutputDto 리스트에 저장
    public List<OutputDto> makeOutputDto(
            List<DocumentDto> documentList
    ) {
        // 전체 15개의 리스트가 들어온다.. 그 중 5개 출력
        return documentList
                .stream()
                .map(x -> convertToOutputDto(x))
                .limit(5)
                .toList();
    }

    // 각각의 DocumentDto를 꺼내서 OutputDto 변환
    private OutputDto convertToOutputDto(DocumentDto document) {

        // 1. 길찾기 URL을 변수로 생성 (도착지 기준)
        // 주의: 장소명에 쉼표(,)가 포함되어 있으면 URL이 깨질 수 있으므로 제거하거나 인코딩하는 것이 좋습니다.
        String placeName = document.getPlaceName() != null ? document.getPlaceName().replace(",", "") : "목적지";

        String directionUrl = String.format("https://map.kakao.com/link/to/%s,%s,%s",
                placeName,
                document.getLatitude(),
                document.getLongitude());

        // 2. OutputDto 변환 및 반환 (Builder 패턴 가정)
        return OutputDto.builder()
                .pharmacyName(document.getPlaceName())
                .pharmacyAddress(document.getAddressName())
                .directionURL(directionUrl)
                .distance(document.getDistance())
                .build();
    }
}