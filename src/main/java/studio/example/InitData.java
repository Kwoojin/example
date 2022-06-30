package studio.example;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import studio.example.lecture.domain.Lecture;
import studio.example.member.domain.Member;
import studio.example.reservation.domain.Reservation;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static studio.example.lecture.domain.Lecture.*;

@Profile("local")
@Component
@RequiredArgsConstructor
public class InitData {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        @PersistenceContext EntityManager em;

        public void dbInit1() {
            LocalDateTime now = LocalDateTime.now();
            List<Lecture> lectures = new ArrayList<>();
            for(int i=1; i<=20; i++){
                Lecture lecture = createLecture(
                        "title" + i, "place" + i, "lecturer" + i, 30 + i, now.minusDays(2).plusHours(i * 3), now.minusDays(2).plusHours(i * 3 + 1), "content" + i
                );
                lectures.add(lecture);
                em.persist(lecture);
            }

            List<Member> members = new ArrayList<>();
            for(int i=1; i<=40; i++){
                Member member = Member.builder().name("name" + i).empNo("X" + String.format("%04d", i)).build();
                members.add(member);
                em.persist(member);
            }

            for(int i=0; i<15; i++) {
                for(int j=i*2; j<i*2+2; j++){
                    em.persist(Reservation.createReservation(members.get(j), lectures.get(i)));
                }
            }

            for(int i=5; i<10; i++){
                em.persist(Reservation.createReservation(members.get(i), lectures.get(0)));
                em.persist(Reservation.createReservation(members.get(i+1), lectures.get(1)));
            }

            em.persist(Reservation.createReservation(members.get(10), lectures.get(2)));
            em.persist(Reservation.createReservation(members.get(11), lectures.get(2)));
            em.persist(Reservation.createReservation(members.get(12), lectures.get(2)));
        }
    }
}
