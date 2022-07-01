package studio.example.reservation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import studio.example.lecture.domain.Lecture;
import studio.example.lecture.repo.LectureRepository;
import studio.example.member.domain.Member;
import studio.example.member.repo.MemberRepository;
import studio.example.reservation.domain.Reservation;
import studio.example.reservation.domain.ReservationStatus;
import studio.example.reservation.error.*;
import studio.example.reservation.repo.ReservationRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final LectureRepository lectureRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public long addReservation(String empNo, Long lectureId) {
        Assert.hasText(empNo, "empNo must not be Null");
        Member member = memberRepository.findByEmpNo(empNo).orElseThrow(() -> new NoSuchMemberByEmpNoException("Couldn't find a member that matches the empNo"));
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow(() -> new NoSuchLectureByIdException("Couldn't find a lecture that matches the id"));
        if (LocalDateTime.now().isAfter(lecture.getStartTime()))
            throw new OverTimeLectureException("Lecture that have already expired cannot be applied for");

        Optional<Reservation> findReservation = reservationRepository.findByMemberAndLectureAndStatus(member, lecture, ReservationStatus.COMPLETE);
        if(findReservation.isPresent())
            throw new AlreadyMemberInLectureException("There is already a member registered in Lecture");

        List<Reservation> reservationInTime = reservationRepository.findReservationInTime(member, ReservationStatus.COMPLETE, lecture.getStartTime(), lecture.getEndTime());
        if(reservationInTime.size()>0)
            throw new OverlapReservationLectureTimeException("There is already a member registered in Lecture");

        Reservation reservation = Reservation.createReservation(member, lecture);
        reservationRepository.save(reservation);
        return reservation.getId();
    }

    public List<Reservation> getReservation(String empNo) {
        Assert.hasText(empNo, "empNo must not be Null");

        if(!StringUtils.hasText(empNo)){
            throw new NullPointerException();
        }
        Member member = memberRepository.findByEmpNo(empNo).orElseThrow(() -> new NoSuchMemberByEmpNoException("Couldn't find a member that matches the empNo"));
        return reservationRepository.findListByMember(member);
    }
}
