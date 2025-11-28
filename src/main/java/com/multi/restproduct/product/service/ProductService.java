package com.multi.restproduct.product.service;

import com.multi.restproduct.common.paging.SelectCriteria;
import com.multi.restproduct.common.util.AwsS3Utils;
import com.multi.restproduct.product.dao.ProductMapper;
import com.multi.restproduct.product.dto.req.RequestProductDto;
import com.multi.restproduct.product.dto.res.ProductAllDto;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ProductService {
//    @Value("${image.image-dir}")
//    private String IMAGE_DIR;
//    @Value("${image.image-url}")
//    private String IMAGE_URL;
//



    @Value("${spring.cloud.aws.s3.base-url}")
    private String S3_URL;
    private String IMAGE_DIR = "products/";
    private final AwsS3Utils awsS3Utils;
    private String IMAGE_URL;

    private final ProductMapper productMapper;
    public ProductService(AwsS3Utils awsS3Utils, ProductMapper productMapper) {
        this.awsS3Utils = awsS3Utils;
        this.productMapper = productMapper;
    }

    @PostConstruct  //@PostConstruct를 사용하여 S3_URL이 주입된 후 IMAGE_URL을 초기화 // 저장할때 폴더명까지 추가되도록함(폴더명으로구분)
    public void init() {
        // this.IMAGE_URL = S3_URL + IMAGE_DIR;

        this.IMAGE_URL = S3_URL ;
        System.out.println(this.IMAGE_URL);
    }

    public int selectProductTotal() {
        int result = productMapper.selectProductTotal();
        return result;
    }

    public List<ProductAllDto> selectProductListWithPaging(SelectCriteria selectCriteria) {

        List<ProductAllDto> productList =  productMapper.selectProductListWithPaging(selectCriteria);
        for(int i = 0 ; i < productList.size() ; i++) {
            productList.get(i).setProductImageUrl(IMAGE_URL + productList.get(i).getProductImageUrl());
        }
        return productList;

    }


    public int selectProductTotalForAdmin() {
        int result = productMapper.selectProductTotalForAdmin();
        return result;
    }

    public List<ProductAllDto> selectProductListWithPagingForAdmin(SelectCriteria selectCriteria) {

        List<ProductAllDto> productList =  productMapper.selectProductListWithPagingForAdmin(selectCriteria);
        for(int i = 0 ; i < productList.size() ; i++) {
            productList.get(i).setProductImageUrl(IMAGE_URL + productList.get(i).getProductImageUrl());
        }
        return productList;

    }

    public ProductAllDto selectProduct(String productCode) {

        ProductAllDto productAllDto = productMapper.findProductbyId(productCode);
        productAllDto.setProductImageUrl(IMAGE_URL + productAllDto.getProductImageUrl());

        return productAllDto;
    }

    public ProductAllDto selectProductForAdmin(String productCode) {
        ProductAllDto productAllDto = productMapper.findProductbyIdForAdmin(productCode);
        productAllDto.setProductImageUrl(IMAGE_URL + productAllDto.getProductImageUrl());

        return productAllDto;
    }

    public List<ProductAllDto>  selectSearchProductList(String search) {

        List<ProductAllDto> productList = productMapper.selectSearchProductList(search);
        for(int i = 0 ; i < productList.size() ; i++) {
            productList.get(i).setProductImageUrl(IMAGE_URL + productList.get(i).getProductImageUrl());
        }
        return productList;

    }

    public String insertProduct(RequestProductDto productDto) {
        String imageName = UUID.randomUUID().toString().replace("-", "");
        String replaceFileName = null;
        int result = 0;
        replaceFileName = awsS3Utils.saveFile(IMAGE_DIR, imageName, productDto.getProductImage());
        log.info("[ProductService] replaceFileName : " + replaceFileName);

        productDto.setProductImageUrl(replaceFileName);

        log.info("[ProductService] insert Image Name : "+ replaceFileName);

        result = productMapper.insertProduct(productDto);

        log.info("[ProductService] result > 0 성공: "+ result);
        return (result > 0) ? "상품 입력 성공" : "상품 입력 실패";
    }

    public String updateProduct(RequestProductDto productDto) {

        String replaceFileName = null;
        int result = 0;

        String oriImage = productMapper.findProductbyIdForAdmin(String.valueOf(productDto.getProductCode())).getProductImageUrl();
        log.info("[updateProduct] oriImage : " + oriImage);

        if(productDto.getProductImage() != null && !productDto.getProductImage().isEmpty()){
            // 이미지 변경 진행
            String imageName = UUID.randomUUID().toString().replace("-", "");
            replaceFileName = awsS3Utils.saveFile(IMAGE_DIR, imageName, productDto.getProductImage());

            log.info("[updateProduct] IMAGE_DIR!!"+ IMAGE_DIR);
            log.info("[updateProduct] imageName!!"+ imageName);

            log.info("[updateProduct] InsertFileName : " + replaceFileName);
            productDto.setProductImageUrl(replaceFileName);

            log.info("[updateProduct] deleteImage : " + oriImage);
            boolean isDelete = awsS3Utils.deleteFile(IMAGE_DIR, oriImage);
            log.info("[update] isDelete : " + isDelete);
        } else {
            // 이미지 변경 없을 시
            productDto.setProductImageUrl(oriImage);
        }

        result = productMapper.updateProduct(productDto);

        log.info("[ProductService] updateProduct End ===================================");
        log.info("[ProductService] result > 0 성공: "+ result);

        return (result > 0) ? "상품 업데이트 성공" : "상품 업데이트 실패";
    }
}
