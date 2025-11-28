package com.multi.restproduct.review.dao;

import com.multi.restproduct.review.dto.ReviewDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReviewMapper {

    ReviewDto selectReviewDetail(String reviewCode);

    int insertReview(ReviewDto toReview);

    int selectReviewTotal(int productCode);

    int updateReview(ReviewDto reviewDto);

    List<ReviewDto> selectReviewListWithPaging(int startRow, int limit, int productCode);
}
