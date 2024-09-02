package cnsa.mergedate.domain.token.controller;

import cnsa.mergedate.common.response.ResponseBody;
import cnsa.mergedate.domain.token.dto.request.TokenReIssueRequest;
import cnsa.mergedate.domain.token.dto.response.TokenIssueResponse;
import cnsa.mergedate.domain.token.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/token")
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("/re-issuance")
    public ResponseBody<TokenIssueResponse> reIssueToken(@RequestBody TokenReIssueRequest request) {
        return ResponseBody.ok(tokenService.getNewTokenIssue(request));
    }
}
