package cnsa.mergedate.domain.auth.controller;

import cnsa.mergedate.common.response.ResponseBody;
import cnsa.mergedate.common.security.PrincipalDetails;
import cnsa.mergedate.domain.auth.dto.request.AuthLoginRequest;
import cnsa.mergedate.domain.auth.dto.request.AuthLogoutRequest;
import cnsa.mergedate.domain.auth.dto.request.AuthSignUpRequest;
import cnsa.mergedate.domain.auth.dto.response.AuthLoginResponse;
import cnsa.mergedate.domain.auth.dto.response.AuthSignUpResponse;
import cnsa.mergedate.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseBody<AuthLoginResponse> login(
        @RequestBody AuthLoginRequest authLoginRequest
    ) {
        return ResponseBody.ok(authService.login(authLoginRequest));
    }

    @PostMapping("/sign-up")
    public ResponseBody<AuthSignUpResponse> signUp(
        @RequestPart AuthSignUpRequest authSignUpRequest
    ) {
        return ResponseBody.ok(authService.signUp(authSignUpRequest));
    }

    @PostMapping("/logout")
    public ResponseBody<Void> logout(
        @RequestBody AuthLogoutRequest authLogoutRequest
    ) {
        authService.logout(authLogoutRequest);
        return ResponseBody.ok();
    }

    @DeleteMapping()
    public ResponseBody<Void> delete(
        @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        authService.delete(principalDetails.memberId());
        return ResponseBody.ok();
    }
}
