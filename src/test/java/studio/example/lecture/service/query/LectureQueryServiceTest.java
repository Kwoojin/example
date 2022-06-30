package studio.example.lecture.service.query;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import studio.example.lecture.domain.Lecture;
import studio.example.lecture.repo.query.LectureQueryDto;
import studio.example.member.domain.Member;
import studio.example.reservation.domain.Reservation;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

import static studio.example.lecture.domain.Lecture.createLecture;

@SpringBootTest
@Transactional
class LectureQueryServiceTest {

    @Autowired LectureQueryService lectureQueryService;
    @PersistenceContext EntityManager em;

    @Test
    public void test() throws Exception {
        Member member1 = Member.builder().name("이름1").empNo("X0101").build();
        Member member2 = Member.builder().name("이름2").empNo("X0102").build();
        Member member3 = Member.builder().name("이름3").empNo("X0103").build();
        Member member4 = Member.builder().name("이름4").empNo("X0104").build();
        Member member5 = Member.builder().name("이름5").empNo("X0105").build();
        Member member6 = Member.builder().name("이름6").empNo("X0106").build();
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
        em.persist(member5);
        em.persist(member6);

        LocalDateTime now = LocalDateTime.now();
        Lecture lecture1 = createLecture("강연명1", "장소1", "강사1", 100, now.plusDays(1), now.plusDays(1).plusHours(1), "content1");
        Lecture lecture2 = createLecture("강연명2", "장소2", "강사2", 100, now.plusDays(2), now.plusDays(2).plusHours(1), "content2");
        Lecture lecture3 = createLecture("강연명3", "장소3", "강사3", 100, now.plusDays(3), now.plusDays(3).plusHours(1), "content3");
        em.persist(lecture1);
        em.persist(lecture2);
        em.persist(lecture3);

        Reservation reservation1 = Reservation.createReservation(member1, lecture1);
        Reservation reservation2 = Reservation.createReservation(member2, lecture1);
        Reservation reservation3 = Reservation.createReservation(member3, lecture1);
        Reservation reservation4 = Reservation.createReservation(member4, lecture2);
        Reservation reservation5 = Reservation.createReservation(member5, lecture2);
        Reservation reservation6 = Reservation.createReservation(member6, lecture2);
        em.persist(reservation1);
        em.persist(reservation2);
        em.persist(reservation3);
        em.persist(reservation4);
        em.persist(reservation5);
        em.persist(reservation6);

        em.flush();
        em.clear();

        List<LectureQueryDto> results = lectureQueryService.findMemberListByLecture();
        for (LectureQueryDto lectureQueryDto : results) {
            System.out.println("lectureQueryDto = " + lectureQueryDto);
            for(String empNo : lectureQueryDto.getEmpNos()){
                System.out.println("empNo = " + empNo);
            }
        }

    }
}