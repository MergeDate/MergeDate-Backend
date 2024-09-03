package cnsa.mergedate.domain.member.controller;

import cnsa.mergedate.common.response.ResponseBody;
import cnsa.mergedate.common.security.PrincipalDetails;
import cnsa.mergedate.domain.member.dto.request.MemberDeleteRequest;
import cnsa.mergedate.domain.member.dto.request.MemberUpdateRequest;
import cnsa.mergedate.domain.member.dto.response.MemberNicknameDuplResponse;
import cnsa.mergedate.domain.member.dto.response.MemberResponse;
import cnsa.mergedate.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseBody<MemberResponse> findMember(
        @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        return ResponseBody.ok(
            memberService.findMemberById(principalDetails.memberId())
        );
    }

    @PostMapping("/update")
    public ResponseBody<Void> updateEvent(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @RequestPart MemberUpdateRequest memberUpdateRequest
    ) {
        memberService.updateEvent(principalDetails.memberId(), memberUpdateRequest);
        return ResponseBody.ok();
    }

    @PostMapping("/delete")
    public ResponseBody<Void> deleteEvent(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @RequestPart MemberDeleteRequest memberDeleteRequest
    ) {
        memberService.deleteEvent(principalDetails.memberId(), memberDeleteRequest);
        return ResponseBody.ok();
    }

    @GetMapping("/duplication-check")
    public ResponseBody<MemberNicknameDuplResponse> checkNicknameDuplication(
        @RequestParam String nickname
    ) {
        return ResponseBody.ok(
            memberService.getNicknameDuplication(nickname)
        );
    }
}
