package cnsa.mergedate.domain.member.service;

import cnsa.mergedate.common.exception.EventNotFoundException;
import cnsa.mergedate.common.exception.MemberNotFoundException;
import cnsa.mergedate.domain.member.dto.request.MemberDeleteRequest;
import cnsa.mergedate.domain.member.dto.request.MemberUpdateRequest;
import cnsa.mergedate.domain.member.dto.response.MemberNicknameDuplResponse;
import cnsa.mergedate.domain.member.dto.response.MemberResponse;
import cnsa.mergedate.domain.member.entity.Member;
import cnsa.mergedate.domain.member.entity.MemberEvent;
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
    public void updateEvent(
        String id, MemberUpdateRequest request
    ) {
        Query query = Query.query(Criteria.where("id").is(id));

        MemberEvent newEvent = new MemberEvent(
            request.eventTitle(),
            request.startDate(),
            request.endDate(),
            request.eventDescription(),
            request.eventColor()
        );

        Update update = new Update()
            .set("nickname", request.nickname())
            .push("memberSelectedDates", newEvent);

        UpdateResult result = mongoTemplate.updateFirst(query, update, Member.class);

        if (result.getMatchedCount() == 0) {
            throw new MemberNotFoundException();
        }
    }

    @Transactional
    public void deleteEvent(
        String id, MemberDeleteRequest request
    ) {
        Query query = Query.query(Criteria.where("id").is(id));

        Update update = new Update()
            .pull("memberSelectedDates",
                Query.query(Criteria
                    .where("eventTitle").is(request.eventTitle())
                    .and("startDate").is(request.startDate())
                    .and("endDate").is(request.endDate())
                    .and("eventDescription").is(request.eventDescription())
                    .and("eventColor").is(request.eventColor())
                ).getQueryObject()
            );

        UpdateResult result = mongoTemplate.updateFirst(query, update, Member.class);

        if (result.getMatchedCount() == 0) {
            throw new MemberNotFoundException();
        }

        if (result.getModifiedCount() == 0) {
            throw new EventNotFoundException();
        }
    }

    @Transactional(readOnly = true)
    public MemberNicknameDuplResponse getNicknameDuplication(String nickname) {
        return new MemberNicknameDuplResponse(
            memberRepository.existsByNickname(nickname)
        );
    }
}