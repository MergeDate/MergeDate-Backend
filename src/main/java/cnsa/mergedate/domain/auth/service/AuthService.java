package cnsa.mergedate.domain.auth.service;

import cnsa.mergedate.common.exception.MemberNotFoundException;
import cnsa.mergedate.common.jwt.util.JwtProvider;
import cnsa.mergedate.domain.auth.dto.request.AuthLoginRequest;
import cnsa.mergedate.domain.auth.dto.request.AuthLogoutRequest;
import cnsa.mergedate.domain.auth.dto.request.AuthSignUpRequest;
import cnsa.mergedate.domain.auth.dto.response.AuthLoginResponse;
import cnsa.mergedate.domain.auth.dto.response.AuthSignUpResponse;
import cnsa.mergedate.domain.member.entity.Member;
import cnsa.mergedate.domain.member.repository.MemberRepository;
import cnsa.mergedate.domain.token.dto.response.TokenIssueResponse;
import cnsa.mergedate.domain.token.service.TokenService;
import com.mongodb.client.result.UpdateResult;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtProvider jwtProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final MongoTemplate mongoTemplate;

    @Transactional
    public AuthLoginResponse login(AuthLoginRequest authLoginRequest) {
        String password = authLoginRequest.password();

        Authentication authentication
            = getAuthenticate(authLoginRequest.nickname(), password);

        Member member = memberRepository.getMember(
            authLoginRequest.nickname()
        );

        modifyDeletedAt(member.getId(), null);

        TokenIssueResponse tokenIssue = jwtProvider.issueToken(authentication);
        return AuthLoginResponse.from(member, tokenIssue);
    }

    @Transactional
    public AuthSignUpResponse signUp(
        AuthSignUpRequest authSignUpRequest
    ) {
        if (memberRepository.existsByNickname(authSignUpRequest.nickname())) {
            throw new RuntimeException("이미 존재하는 닉네임입니다.");  //todo: Custom Ex
        }

        String password = "hmm";
        String encodedPassword = passwordEncoder.encode(password);

        Member member = Member.builder()
            .password(encodedPassword)
            .nickname(authSignUpRequest.nickname())
            .build();

        Member savedMember = memberRepository.save(member);

        Authentication authentication =
            getAuthenticate(authSignUpRequest.nickname(), password);
        TokenIssueResponse tokenIssue = jwtProvider.issueToken(authentication);

        return AuthSignUpResponse.from(savedMember, tokenIssue);
    }

    @Transactional
    public void logout(AuthLogoutRequest authLogoutRequest) {
        tokenService.deleteToken(authLogoutRequest.refreshToken());
    }

    @Transactional
    public void delete(String memberId) {
        modifyDeletedAt(memberId, LocalDateTime.now());
    }

    private void modifyDeletedAt(String memberId, LocalDateTime localDateTime) {
        Query query = Query.query(Criteria.where("id").is(memberId));
        Update update = new Update().set("deletedAt", localDateTime);

        UpdateResult result = mongoTemplate.upsert(query, update, Member.class);

        if (result.getMatchedCount() == 0) {
            throw new MemberNotFoundException();
        }
    }

    private Authentication getAuthenticate(String nickname, String password) {
        UsernamePasswordAuthenticationToken authenticationToken
            = new UsernamePasswordAuthenticationToken(nickname, password);
        return authenticationManagerBuilder.getObject().authenticate(authenticationToken);
    }
}
