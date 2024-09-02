package cnsa.mergedate.domain.auth.dto.response;

import cnsa.mergedate.domain.member.entity.Member;
import cnsa.mergedate.domain.token.dto.response.TokenIssueResponse;
import lombok.Builder;

@Builder
public record AuthLoginResponse(
    String id,
    String nickname,
    TokenIssueResponse tokenIssue
) {
    public static AuthLoginResponse from(
        Member member,
        TokenIssueResponse tokenIssue
    ) {
        return AuthLoginResponse.builder()
            .id(member.getId())
            .nickname(member.getNickname())
            .tokenIssue(tokenIssue)
            .build();
    }
}
