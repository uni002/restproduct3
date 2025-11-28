package com.multi.restproduct.product.controller;

import com.multi.restproduct.common.ResponseDto;
import com.multi.restproduct.common.paging.Pagenation;
import com.multi.restproduct.common.paging.ResponseDtoWithPaging;
import com.multi.restproduct.common.paging.SelectCriteria;
import com.multi.restproduct.product.dto.req.RequestProductDto;
import com.multi.restproduct.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ProductController {

    private final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public ResponseEntity<ResponseDto> selectProductListWithPaging(@RequestParam(name = "offset",defaultValue = "1") String offset){


        //예외 테스트할때
        //throw new IllegalStateException("Rest 컨트롤러에서 발생한 예외입니다.");

        int totalCount = productService.selectProductTotal();
        int limit = 10;
        int buttonAmount = 10;
        SelectCriteria selectCriteria = Pagenation.getSelectCriteria(Integer.parseInt(offset),totalCount,limit,buttonAmount);

        ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging();
        responseDtoWithPaging.setPageInfo(selectCriteria);
        responseDtoWithPaging.setData(productService.selectProductListWithPaging(selectCriteria));

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK  , " 조회성공" , responseDtoWithPaging));
    }

    private SelectCriteria getSelectCriteria(int offset, int totalCount) {
        int limit = 10;
        int buttonAmount = 10;
        return Pagenation.getSelectCriteria(offset, totalCount, limit, buttonAmount);
    }

    ///products-management
    @GetMapping("/products-management")
    public ResponseEntity<ResponseDto> getProductListForAdmin(@RequestParam(name = "offset", defaultValue = "1") int offset) {
        SelectCriteria selectCriteria = getSelectCriteria(offset, productService.selectProductTotalForAdmin());
        ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging(productService.selectProductListWithPagingForAdmin(selectCriteria), selectCriteria);
        return ResponseEntity.ok(new ResponseDto(HttpStatus.OK, "조회 성공", responseDtoWithPaging));
    }
    @GetMapping("/products/{productCode}")
    public ResponseEntity<ResponseDto> getProductDetail(@PathVariable("productCode") String productCode) {
        return ResponseEntity.ok(new ResponseDto(HttpStatus.OK, "상품 상세정보 조회 성공", productService.selectProduct(productCode)));
    }

    @GetMapping("/products-management/{productCode}")
    public ResponseEntity<ResponseDto> getProductDetailForAdmin(@PathVariable("productCode") String productCode) {
        return ResponseEntity.ok(new ResponseDto(HttpStatus.OK, "상품 상세정보 조회 성공", productService.selectProductForAdmin(productCode)));
    }


    @GetMapping("/products/search")
    public ResponseEntity<ResponseDto> getSearchProduct(@RequestParam(name = "query" , defaultValue = "") String search) {
        return ResponseEntity.ok(new ResponseDto(HttpStatus.OK, "상품 검색 조회 성공", productService.selectSearchProductList(search)));
    }


    @PostMapping("/products-management/products")
    public ResponseEntity<ResponseDto> insertProduct(@ModelAttribute RequestProductDto productDto) {
        //log.info("[ProductController] Insert Product: {}", productDto);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.CREATED, "상품 입력 성공", productService.insertProduct(productDto)));
    }

    @PutMapping("/products-management/products")
    public ResponseEntity<ResponseDto> updateProduct(@ModelAttribute RequestProductDto productDto) {
        // log.info("[ProductController] Update Product: {}", productDto);
        return ResponseEntity.ok(new ResponseDto(HttpStatus.CREATED, "상품 업데이트 성공", productService.updateProduct(productDto)));
    }

}
