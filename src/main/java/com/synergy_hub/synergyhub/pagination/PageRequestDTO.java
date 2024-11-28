package com.synergy_hub.synergyhub.pagination;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class PageRequestDTO {
    private final int page;     //페이지 번호
    private final int size;     //페이지 크기
    private final String sortBy;    //정렬 기준 필드
    private final boolean ascending;    //정렬 방향(true:오름차순, false:내림차순)

    public static PageRequestDTO defaultInstance() {
        return PageRequestDTO.builder()
            .page(1)
            .size(10)
            .sortBy("date")
            .ascending(true)
            .build();
    }
}
