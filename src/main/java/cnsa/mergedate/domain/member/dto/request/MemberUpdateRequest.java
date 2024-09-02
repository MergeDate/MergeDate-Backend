package cnsa.mergedate.domain.member.dto.request;

import java.time.LocalDate;
import java.util.List;

public record MemberUpdateRequest(
    String nickname,
    List<LocalDate> selectedDates
) {
}
