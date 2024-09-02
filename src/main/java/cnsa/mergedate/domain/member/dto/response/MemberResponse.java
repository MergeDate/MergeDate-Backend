package cnsa.mergedate.domain.member.dto.response;

import cnsa.mergedate.domain.member.entity.Member;
import java.time.LocalDate;
import java.util.List;

public record MemberResponse(
    String nickname,
    List<LocalDate> selectedDates
) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(
            member.getNickname(),
            member.getSelectedDates()
        );
    }
}
