package com.multi.restproduct.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDto {
    private Long reviewCode;           // 리뷰 코드
    private String memberName;         // 회원 이름
    private Long productCode;          // 상품 코드
    private String memberCode;           // 회원 아이디
    private String reviewTitle;        // 리뷰 제목
    private String reviewContent;      // 리뷰 내용

    private String createdPerson;      // 생성자
    private String modifiedPerson;     // 수정자

    private LocalDateTime createdDate;  // 생성일자
    private LocalDateTime modifiedDate; // 수정일자
}
