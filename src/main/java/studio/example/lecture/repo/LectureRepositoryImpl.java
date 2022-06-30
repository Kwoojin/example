package studio.example.lecture.repo;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import studio.example.lecture.repo.query.LectureQueryDto;
import studio.example.lecture.repo.query.LectureQueryMemberDto;
import studio.example.lecture.repo.query.QLectureQueryDto;
import studio.example.lecture.repo.query.QLectureQueryMemberDto;

import javax.persistence.EntityManager;

import java.util.List;

import static studio.example.lecture.domain.QLecture.*;
import static studio.example.member.domain.QMember.*;
import static studio.example.reservation.domain.QReservation.*;


public class LectureRepositoryImpl implements LectureRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Autowired
    public LectureRepositoryImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<LectureQueryDto> searchPageLecture() {
        return queryFactory
                .select(new QLectureQueryDto(lecture.id, lecture.title, lecture.place, lecture.lecturer))
                .from(lecture)
                .fetch();
    }

    @Override
    public List<LectureQueryMemberDto> searchMemberByLecture(List<Long> ids) {
        return queryFactory
                .select(new QLectureQueryMemberDto(reservation.lecture.id, member.empNo))
                .from(reservation)
                .innerJoin(reservation.member, member)
                .where(reservation.lecture.id.in(ids))
                .fetch();
    }

}
