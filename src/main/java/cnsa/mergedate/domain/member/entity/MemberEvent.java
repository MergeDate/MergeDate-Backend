package cnsa.mergedate.domain.member.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberEvent {
    String eventTitle;

    LocalDateTime startDate;

    LocalDateTime endDate;

    String eventDescription;

    String eventColor;
}
