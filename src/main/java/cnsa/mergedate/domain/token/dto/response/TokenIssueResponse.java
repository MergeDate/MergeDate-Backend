package cnsa.mergedate.domain.token.dto.response;

public record TokenIssueResponse(
    String accessToken,
    String refreshToken
) {
}
