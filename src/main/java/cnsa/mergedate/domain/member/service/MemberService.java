package cnsa.mergedate.domain.member.service;

import cnsa.mergedate.common.exception.MemberNotFoundException;
import cnsa.mergedate.domain.member.dto.request.MemberUpdateRequest;
import cnsa.mergedate.domain.member.dto.response.MemberNicknameDuplResponse;
import cnsa.mergedate.domain.member.dto.response.MemberResponse;
import cnsa.mergedate.domain.member.entity.Member;
import cnsa.mergedate.domain.member.repository.MemberRepository;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MongoTemplate mongoTemplate;

    @Transactional(readOnly = true)
    public MemberResponse findMemberById(String id) {
        Member member = memberRepository.getMember(id);
        return MemberResponse.from(member);
    }

    @Transactional
    public MemberResponse updateProfile(
        String id, MemberUpdateRequest request
    ) {
        Query query = Query.query(Criteria.where("id").is(id));
        Update update = new Update()
            .set("nickname", request.nickname());

        UpdateResult result = mongoTemplate.upsert(query, update, Member.class);

        if (result.getMatchedCount() == 0) {
            throw new MemberNotFoundException();
        }

        return new MemberResponse(
            request.nickname(), request.selectedDates()
        );
    }

    @Transactional(readOnly = true)
    public MemberNicknameDuplResponse getNicknameDuplication(String nickname) {
        return new MemberNicknameDuplResponse(
            memberRepository.existsByNickname(nickname)
        );
    }
}