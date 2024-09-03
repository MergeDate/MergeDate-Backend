package cnsa.mergedate.domain.member.dto.request;

import java.time.LocalDateTime;

public record MemberDeleteRequest(
    String nickname,
    String eventTitle,
    LocalDateTime startDate,
    LocalDateTime endDate,
    String eventDescription,
    String eventColor
) {
}
