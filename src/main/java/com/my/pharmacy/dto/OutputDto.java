package com.my.pharmacy.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class OutputDto {
    // 약국이름
    private String pharmacyName;
    // 약국주소
    private String pharmacyAddress;
    // 약국 전화번호
    private String pharmacyPhone;
    // 거리(m)
    private String distance;
    // 길안내 URL
    private String directionURL;
    // 로드뷰 URL
    private String roadViewURL;

}
