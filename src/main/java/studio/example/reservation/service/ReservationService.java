package studio.example.reservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studio.example.lecture.domain.Lecture;
import studio.example.lecture.repo.LectureRepository;
import studio.example.member.domain.Member;
import studio.example.member.repo.MemberRepository;
import studio.example.reservation.domain.Reservation;
import studio.example.reservation.error.AlreadyMemberInLectureException;
import studio.example.reservation.error.NoSuchLectureByIdException;
import studio.example.reservation.error.NoSuchMemberByEmpNoException;
import studio.example.reservation.error.OverTimeLectureException;
import studio.example.reservation.repo.ReservationRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final LectureRepository lectureRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public long addReservation(String empNo, Long lectureId) {
        Member member = memberRepository.findByEmpNo(empNo).orElseThrow(() -> new NoSuchMemberByEmpNoException("Couldn't find a member that matches the empNo"));
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow(() -> new NoSuchLectureByIdException("Couldn't find a lecture that matches the id"));
        if (LocalDateTime.now().isAfter(lecture.getStartTime()))
            throw new OverTimeLectureException("Lecture that have already expired cannot be applied for");

        Optional<Reservation> findReservation = reservationRepository.findByMemberAndLecture(member, lecture);
        if(findReservation.isPresent())
            throw new AlreadyMemberInLectureException("There is already a member registered in Lecture");

        Reservation reservation = Reservation.createReservation(member, lecture);
        reservationRepository.save(reservation);
        return reservation.getId();
    }

    public Page<Reservation> getReservation(String empNo, Pageable pageable) {
        Member member = memberRepository.findByEmpNo(empNo).orElseThrow(() -> new NoSuchMemberByEmpNoException("Couldn't find a member that matches the empNo"));
        return reservationRepository.findListByMember(member, pageable);
    }
}
