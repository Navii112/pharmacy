package com.my.pharmacy.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class DocumentDto {
    @JsonProperty("address_name")
    private String addressName;

    @JsonProperty("place_name")
    private String placeName;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("x")
    private double longitude; // 경도

    @JsonProperty("y")
    private double latitude; // 위도

    @JsonProperty("distance")
    private String distnace;
}
