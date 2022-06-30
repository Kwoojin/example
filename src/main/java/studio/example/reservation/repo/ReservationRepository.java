package studio.example.reservation.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import studio.example.lecture.domain.Lecture;
import studio.example.member.domain.Member;
import studio.example.reservation.domain.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findByMemberAndLecture(@Param("member") Member member, @Param("lecture") Lecture lecture);

    @Query(value = "select r from Reservation r join fetch r.lecture l where r.member = :member",
            countQuery = "select count(r) from Reservation r where r.member = :member")
    List<Reservation> findListByMember(@Param("member") Member member);
}
