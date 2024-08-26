package cnsa.mergedate.domain.member.repository;

import cnsa.mergedate.domain.member.entity.Member;
import cnsa.mergedate.common.exception.MemberNotFoundException;
import java.util.Optional;

public interface MemberRepository {
    default Member getMember(String nickname) {
        return findByNickname(nickname)
            .orElseThrow(MemberNotFoundException::new);
    }
    Optional<Member> findByNickname(String nickname);

}
