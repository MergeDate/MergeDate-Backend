package cnsa.mergedate.domain.member.entity;

import cnsa.mergedate.common.entity.BaseTimeEntity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member extends BaseTimeEntity {

    @Id
    private String id;

    private String nickname;

    private String password;

    private List<LocalDate> selectedDates = new ArrayList<>();
}
