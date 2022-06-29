package studio.example.reservation.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import studio.example.lecture.domain.Lecture;
import studio.example.member.domain.Member;
import studio.example.reservation.domain.Reservation;
import studio.example.reservation.error.AlreadyMemberInLectureException;
import studio.example.reservation.error.NoSuchLectureByIdException;
import studio.example.reservation.error.NoSuchMemberByEmpNoException;
import studio.example.reservation.error.OverTimeLectureException;
import studio.example.reservation.repo.ReservationRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static studio.example.lecture.domain.Lecture.createLecture;

@Transactional
@SpringBootTest
class ReservationServiceTest {

    @Autowired ReservationService reservationService;
    @Autowired ReservationRepository reservationRepository;
    @PersistenceContext EntityManager em;

    @Test
    @DisplayName("Exception")
    public void addReservation() {
        //given
        Member member = Member.builder()
                .name("이름")
                .empNo("X0100")
                .build();
        em.persist(member);

        LocalDateTime now = LocalDateTime.now();
        Lecture lecture = createLecture("강연명1", "장소1", "강사1", 100, now.plusDays(1), now.plusDays(1).plusHours(1), "content1");
        em.persist(lecture);

        Lecture lecture2 = createLecture("강연명2", "장소2", "강사2", 100, now.minusDays(1), now.minusDays(1).plusHours(1), "content2");
        em.persist(lecture2);

        Reservation reservation = Reservation.createReservation(member, lecture);
        em.persist(reservation);

        em.flush();
        em.clear();


        // 존재 하지 않는 사번
        assertThatThrownBy(() -> reservationService.addReservation("I0100", lecture.getId()))
                .isInstanceOf(NoSuchMemberByEmpNoException.class);

        // 존재 하지 않는 강연
        assertThatThrownBy(() -> reservationService.addReservation(member.getEmpNo(), 9999L))
                .isInstanceOf(NoSuchLectureByIdException.class);

        // 기간이 지난 강연 신청 시
        assertThatThrownBy(() -> reservationService.addReservation(member.getEmpNo(), lecture2.getId()))
                .isInstanceOf(OverTimeLectureException.class);

        // 이미 등록되어 있는 강연 신청 시
        assertThatThrownBy(() -> reservationService.addReservation(member.getEmpNo(), lecture.getId()))
                .isInstanceOf(AlreadyMemberInLectureException.class);
    }

    @Test
    public void save() {
        //given
        Member member = Member.builder()
                .name("이름")
                .empNo("X0100")
                .build();
        em.persist(member);

        LocalDateTime now = LocalDateTime.now();
        Lecture lecture = createLecture("강연명1", "장소1", "강사1", 100, now.plusDays(1), now.plusDays(1).plusHours(1), "content1");
        em.persist(lecture);

        em.flush();
        em.clear();

        long id = reservationService.addReservation(member.getEmpNo(), lecture.getId());

        //when
        Reservation findReservation = reservationRepository.findById(id).orElseThrow();

        //then
        assertThat(findReservation.getMember().getId()).isEqualTo(member.getId());
        assertThat(findReservation.getLecture().getId()).isEqualTo(lecture.getId());

    }

}