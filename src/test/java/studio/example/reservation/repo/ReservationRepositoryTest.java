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
    public void findByMemberAndLecture() {
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
        Reservation findReservation = reservationRepository.findByMemberAndLecture(member, lecture).orElseThrow();

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

        PageRequest pageRequest = PageRequest.of(0, 20);
        Page<Reservation> results = reservationRepository.findListByMember(member, pageRequest);
        List<Reservation> content = results.getContent();
        assertThat(content.size()).isEqualTo(20);
    }
}