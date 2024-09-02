package cnsa.mergedate.domain.auth.dto.request;

public record AuthLogoutRequest(
    String refreshToken
) {
}
