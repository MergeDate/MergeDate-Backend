package cnsa.mergedate.common.jwt.util;

import static cnsa.mergedate.common.jwt.constant.JwtConstant.ACCESS_TOKEN_EXPIRE_TIME;
import static cnsa.mergedate.common.jwt.constant.JwtConstant.AUTHORITIES_KEY;
import static cnsa.mergedate.common.jwt.constant.JwtConstant.MEMBER_ID_KEY;
import static cnsa.mergedate.common.jwt.constant.JwtConstant.REFRESH_TOKEN_EXPIRE_TIME;

import cnsa.mergedate.common.exception.TokenExpiredException;
import cnsa.mergedate.common.security.PrincipalDetails;
import cnsa.mergedate.domain.token.dto.response.TokenIssueResponse;
import cnsa.mergedate.domain.token.entity.Token;
import cnsa.mergedate.domain.token.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${jwt.secretKey}")
    private String secretKeyPlain;
    private Key secretKey;

    private final TokenRepository tokenRepository;

    @PostConstruct
    protected void init() {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKeyPlain);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenIssueResponse issueToken(Authentication authentication) {
        String accessToken = generateAccessToken(authentication);
        String refreshToken = generateRefreshToken(authentication);
        return new TokenIssueResponse(accessToken, refreshToken);
    }

    private String generateAccessToken(Authentication authentication) {
        return generateToken(authentication, ACCESS_TOKEN_EXPIRE_TIME);
    }

    private String generateRefreshToken(Authentication authentication) {
        String refreshToken =  generateToken(authentication, REFRESH_TOKEN_EXPIRE_TIME);
        tokenRepository.save(new Token(authentication.getName(), refreshToken));
        return refreshToken;
    }

    private String generateToken(Authentication authentication, Long tokenExpiresIn) {
        String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date expiration = new Date(now + tokenExpiresIn);

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String nickname = principalDetails.nickname();
        String memberId = principalDetails.memberId();

        return getToken(nickname, authorities, memberId, expiration);
    }

    private String getToken(
            String nickname,
            String authorities,
            String memberId,
            Date accessTokenExpiresIn
    ) {
        return Jwts.builder()
            .setSubject(nickname)
            .claim(MEMBER_ID_KEY, memberId)
            .claim(AUTHORITIES_KEY, authorities)
            .setExpiration(accessTokenExpiresIn)
            .signWith(secretKey, SignatureAlgorithm.HS512)
            .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        String email = claims.getSubject();
        String memberId = (String) claims.get(MEMBER_ID_KEY);

        PrincipalDetails principal = PrincipalDetails.of(email, memberId);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다."); // todo : 만료일 때, Action
            throw new TokenExpiredException();
        }
        return false;
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey)
                .build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
