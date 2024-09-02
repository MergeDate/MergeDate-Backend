package cnsa.mergedate.domain.member.repository;

import cnsa.mergedate.common.exception.MemberNotFoundException;
import cnsa.mergedate.domain.member.entity.Member;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MemberRepository extends MongoRepository<Member, String> {
    default Member getMember(String nickname) {
        return findByNickname(nickname)
            .orElseThrow(MemberNotFoundException::new);
    }
    Optional<Member> findByNickname(String nickname);

    boolean existsByNickname(String nickname);
}
