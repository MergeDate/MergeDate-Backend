package cnsa.mergedate.domain.token.service;

import cnsa.mergedate.common.exception.LoginRequiredException;
import cnsa.mergedate.common.exception.RefreshTokenNotValidatedException;
import cnsa.mergedate.common.jwt.util.JwtProvider;
import cnsa.mergedate.domain.token.dto.request.TokenReIssueRequest;
import cnsa.mergedate.domain.token.dto.response.TokenIssueResponse;
import cnsa.mergedate.domain.token.entity.Token;
import cnsa.mergedate.domain.token.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;
    private final JwtProvider jwtProvider;

    public TokenIssueResponse getNewTokenIssue(TokenReIssueRequest request) {
        if (!jwtProvider.validateToken(request.refreshToken())) {
            throw new RefreshTokenNotValidatedException();
        }

        Authentication authentication = jwtProvider.getAuthentication(request.refreshToken());
        String tokenId = authentication.getName();

        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(RefreshTokenNotValidatedException::new);

        if (!token.getTokenValue().equals(request.refreshToken())) {
            tokenRepository.delete(token);
            throw new LoginRequiredException();
        }
        return jwtProvider.issueToken(authentication);
    }

    @Transactional
    public void deleteToken(String refreshToken) {
        Authentication authentication = jwtProvider.getAuthentication(refreshToken);
        String tokenId = authentication.getName();

        tokenRepository.deleteById(tokenId);
    }
}
