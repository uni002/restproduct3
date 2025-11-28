package com.multi.restproduct.review.controller;


import com.multi.restproduct.common.ResponseDto;
import com.multi.restproduct.common.paging.Pagenation;
import com.multi.restproduct.common.paging.ResponseDtoWithPaging;
import com.multi.restproduct.common.paging.SelectCriteria;
import com.multi.restproduct.review.dto.ReviewDto;
import com.multi.restproduct.review.service.ReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "스프링 부트 스웨거 연동 ReviewController")
@RestController
@RequestMapping("/api/v1")
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    //http://localhost:8090/api/v1/reviews/{productCode}?offset=1
    @GetMapping("/reviews/{productCode}")
    public ResponseEntity<ResponseDto> selectReviewListWithPaging(@PathVariable String productCode, @RequestParam(name="offset", defaultValue="1") String offset) {

        log.info("[ReviewController] selectReviewListWithPaging : " + offset);
        log.info("[ReviewController] productCode : " + productCode);
        int totalCount = reviewService.selectReviewTotal(Integer.parseInt(productCode));
        int limit = 10;
        int buttonAmount = 10;
        SelectCriteria selectCriteria = Pagenation.getSelectCriteria(Integer.parseInt(offset), totalCount, limit, buttonAmount);;

        log.info("[ProductController] selectCriteria : " + selectCriteria);

        ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging();
        responseDtoWithPaging.setPageInfo(selectCriteria);
        responseDtoWithPaging.setData(reviewService.selectReviewListWithPaging(selectCriteria, Integer.parseInt(productCode)));

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "조회 성공", responseDtoWithPaging));
    }
    //http://localhost:8090/api/v1/reviews/product/1
    //특정리뷰조회
    @GetMapping("/reviews/product/{reviewCode}")
    public ResponseEntity<ResponseDto> selectReviewDetail(@PathVariable String reviewCode) {

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "조회 성공",  reviewService.selectReviewDetail(reviewCode)));
    }
    //http://localhost:8090/api/v1/reviews
    @PostMapping("/reviews")
//    {
//        "productCode": 1,
//            "memberCode": "m01",
//            "reviewTitle": "리뷰 제목",
//            "reviewContent": "리뷰 내용",
//            "createdPerson": "m01"
//    }
    public ResponseEntity<ResponseDto> insertProductReview(@RequestBody ReviewDto reviewDto) {

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "리뷰 입력 성공",  reviewService.insertProductReview(reviewDto)));
    }
    //http://localhost:8090/api/v1/reviews
//    {
//        "reviewCode": 1,
//            "reviewTitle": "업데이트된 리뷰 제목",
//            "reviewContent": "잼있어요",
//            "memberCode": "m01"
//    }
    @PutMapping("/reviews")
    public ResponseEntity<ResponseDto> updateProductReview(@RequestBody ReviewDto reviewDto) {

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "리뷰 수정 성공",  reviewService.updateProductReview(reviewDto)));
    }

}
