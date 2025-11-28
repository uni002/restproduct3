package com.multi.restproduct.review.service;

import com.multi.restproduct.common.paging.SelectCriteria;
import com.multi.restproduct.review.dao.ReviewMapper;
import com.multi.restproduct.review.dto.ReviewDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class ReviewService {

    private final ReviewMapper reviewMapper;

    public ReviewService(ReviewMapper reviewMapper) {
        this.reviewMapper = reviewMapper;
    }

    public ReviewDto selectReviewDetail(String reviewCode) {
        log.info("[ReviewService] getReviewDetail Start ==============================");
        ReviewDto reviewDto = reviewMapper.selectReviewDetail(reviewCode);
        log.info("[ReviewService] getReviewDetail End ==============================");
        return reviewDto;
    }

    @Transactional
    public String insertProductReview(ReviewDto reviewDto) {

        String response = "리뷰 입력 실패";

        log.info("[ReviewService] insertProductReview Start ==============================");

        int result = reviewMapper.insertReview(reviewDto);

        if(result > 0) {
            log.info("[ReviewService] Review Insert Success ==============================");
            response = "리뷰 입력 성공";
        }

        log.info("[ReviewService] insertProductReview End ==============================");

        return response;
    }

    public int selectReviewTotal(int productCode) {
        log.info("[ProductService] selectReviewTotal Start ===================================");
        int result = reviewMapper.selectReviewTotal(productCode);

        log.info("[ProductService] selectReviewTotal End ===================================");
        return result;
    }

    public List<ReviewDto> selectReviewListWithPaging(SelectCriteria selectCriteria, int productCode) {
        log.info("[ReviewService] getReviewList Start ==============================");
        List<ReviewDto> reviewList = reviewMapper.selectReviewListWithPaging(selectCriteria.getStartRow(), selectCriteria.getEndRow(), productCode);
        log.info("[ReviewService] getReviewList End ==============================");
        return reviewList;
    }

    @Transactional
    public String updateProductReview(ReviewDto reviewDto) {

        String response = "리뷰 변경 실패";

        log.info("[ReviewService] updateProductReview Start ==============================");

        int result = reviewMapper.updateReview(reviewDto);

        if(result > 0) {
            log.info("[ReviewService] Review Update Success ==============================");
            response = "리뷰 변경 성공";
        }

        log.info("[ReviewService] updateProductReview End ==============================");

        return response;
    }
}
