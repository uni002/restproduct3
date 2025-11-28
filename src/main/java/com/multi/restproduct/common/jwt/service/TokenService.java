package com.multi.restproduct.common.jwt.service;

import com.multi.restproduct.common.exception.RefreshTokenException;
import com.multi.restproduct.common.jwt.TokenProvider;
import com.multi.restproduct.common.jwt.dao.RefreshTokenMapper;
import com.multi.restproduct.common.jwt.dto.RefreshToken;
import com.multi.restproduct.common.jwt.dto.TokenDto;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenMapper refreshTokenMapper;


    @Transactional(noRollbackFor = RefreshTokenException.class)
    public<T> TokenDto createToken(T t) {
        String memberEmail;
        List<String> roles;
        String accessToken;
        String refreshToken;

        if(t instanceof String){

            String jwt = resolveToken((String)t);
            Claims claims = tokenProvider.parseClaims(jwt);

            memberEmail = claims.getSubject();
            String role = (String) claims.get("auth");
            roles = Arrays.asList(role.split(","));
            log.info("String MemberEmail >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> {}", memberEmail);
            log.info("String Roles >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> {}", roles);

        }else if(t instanceof Map){
            Map<String ,Object> data = (Map<String ,Object>)t;

            memberEmail = (String) data.get("email");
            roles = (List<String>)data.get("roles");

            log.info("Map MemberEmail >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> {}", memberEmail);
            log.info("Map Roles >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> {}", roles);
        }else{
            throw new IllegalArgumentException("Invalid token type !!");
        }
        refreshToken = handleRefreshToken(memberEmail);

        accessToken = createAccessToken(memberEmail, roles);


        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private String resolveToken(String token) {
        // Bearer 접두어가 있는 경우 제거하고 순수한 토큰 반환
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token; // Bearer 접두어가 없는 경우 그대로 반환
    }

    //추가 인증 요구 옵션: IP 불일치 시 바로 로그인 요구 대신, 2차 인증(이메일/OTP)으로 안전하게 본인확인 가능.
    private String handleRefreshToken(String memberEmail) {

        Optional<RefreshToken> exsitingToken = refreshTokenMapper.findByEmail(memberEmail);
        if(exsitingToken.isPresent()){ // 리프레시 토큰이 존재하는경우
            RefreshToken refreshToken = exsitingToken.get();
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expiredDate = refreshToken.getExpiredAt();

            if(expiredDate.isBefore(now)){
                refreshTokenMapper.deleteRefreshTokenByEmail(memberEmail);
                throw new RefreshTokenException("Refresh token 이 만료 되었습니다, 다시 로그인 해주세요 ");

            }else{
                return refreshToken.getRefreshToken();
            }
        }else{

            String reToken = createRefreshToken(memberEmail);



            if(tokenProvider.validateToken(reToken)){
                RefreshToken newToken = RefreshToken.builder()
                        .email(memberEmail)
                        .refreshToken(reToken)
                        .expiredAt(tokenProvider.getRefreshTokenExpiry())
                        .issuedAt(LocalDateTime.now())
                        .build();

                refreshTokenMapper.insertRefreshTokenByEmail(newToken);

            }
            return reToken;
        }


    }

    private String createAccessToken(String memberEmail, List<String> roles) {
        return tokenProvider.generateToken(memberEmail,roles, "A");
    }

    private String createRefreshToken(String memberEmail) {
        return tokenProvider.generateToken(memberEmail, null, "R");
    }

    public void deleteRefreshToken(String accessToken) {

        String token = resolveToken(accessToken);

        String email = tokenProvider.getUserId(token);

        refreshTokenMapper.deleteRefreshTokenByEmail(email);

        log.info("Refresh Token 삭제 완료: 사용자 email - {}", email);
    }
}
