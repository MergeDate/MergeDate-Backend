package cnsa.mergedate.domain.member.dto.response;

import cnsa.mergedate.domain.member.entity.Member;
import cnsa.mergedate.domain.member.entity.MemberEvent;
import java.util.List;

public record MemberResponse(
    String nickname,
    List<MemberEvent> memberEvents
) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(
            member.getNickname(),
            member.getMemberEvents()
        );
    }
}
