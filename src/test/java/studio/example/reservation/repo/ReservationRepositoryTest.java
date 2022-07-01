package studio.example.reservation.repo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import studio.example.lecture.domain.Lecture;
import studio.example.member.domain.Member;
import studio.example.reservation.domain.Reservation;
import studio.example.reservation.domain.ReservationStatus;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static studio.example.lecture.domain.Lecture.createLecture;

@SpringBootTest
@Transactional
class ReservationRepositoryTest {

    @Autowired ReservationRepository reservationRepository;
    @Autowired EntityManager em;

    @Test
    public void findByMemberAndLectureAndStatus() {
        //given
        Member member = Member.builder().name("이름").empNo("X0100").build();
        em.persist(member);

        LocalDateTime now = LocalDateTime.now();
        Lecture lecture = createLecture("강연명1", "장소1", "강사1", 100, now.plusDays(1), now.plusDays(1).plusHours(1), "content1");
        em.persist(lecture);

        Reservation reservation = Reservation.createReservation(member, lecture);
        em.persist(reservation);

        em.flush();
        em.clear();

        //when
        Reservation findReservation = reservationRepository.findByMemberAndLectureAndStatus(member, lecture, ReservationStatus.COMPLETE).orElseThrow();

        //then
        assertThat(findReservation).isEqualTo(reservation);
    }

    @Test
    public void findListByMember() {
        //given
        Member member = Member.builder().name("이름").empNo("X0100").build();
        em.persist(member);

        LocalDateTime now = LocalDateTime.now();
        for(int i=0; i<20; i++){
            Lecture lecture = createLecture("강연명"+i, "장소"+i, "강사"+i, 100+i, now.plusDays(1).plusHours(i), now.plusDays(1).plusHours(1+i), "content"+i);
            em.persist(lecture);

            Reservation reservation = Reservation.createReservation(member, lecture);
            em.persist(reservation);
        }
        em.flush();
        em.clear();

        List<Reservation> results = reservationRepository.findListByMember(member);
        assertThat(results.size()).isEqualTo(20);
    }

    @Test
    public void findReservationInTime() {
        Member member = Member.builder().name("이름").empNo("X0100").build();
        em.persist(member);

        LocalDateTime now = LocalDateTime.now();
        Lecture lecture1 = createLecture("강연명1", "장소1", "강사1", 100, now.plusDays(1), now.plusDays(1).plusHours(3), "content1");
        Lecture lecture2 = createLecture("강연명2", "장소2", "강사2", 100, now.plusDays(1).minusHours(1), now.plusDays(1).plusHours(2), "content2");
        em.persist(lecture1);
        em.persist(lecture2);

        Reservation reservation = Reservation.createReservation(member, lecture1);
        em.persist(reservation);

        em.flush();
        em.clear();

        List<Reservation> results = reservationRepository.findReservationInTime(member, ReservationStatus.COMPLETE, lecture2.getStartTime(), lecture2.getEndTime());
        assertThat(results.size()).isEqualTo(1);
    }
}