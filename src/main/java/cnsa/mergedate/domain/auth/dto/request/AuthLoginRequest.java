package cnsa.mergedate.domain.auth.dto.request;

public record AuthLoginRequest(
    String nickname,
    String password
) {
}
