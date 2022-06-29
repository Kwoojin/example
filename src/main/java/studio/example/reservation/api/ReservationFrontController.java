package studio.example.reservation.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import studio.example.lecture.api.LectureBackOfficeController;
import studio.example.lecture.domain.Lecture;
import studio.example.reservation.api.dto.ReservationLectureDto;
import studio.example.reservation.domain.Reservation;
import studio.example.reservation.service.ReservationService;

import static studio.example.reservation.api.ReservationFrontController.ReservationDto.*;
import static studio.example.reservation.api.dto.ReservationLectureDto.*;

@RestController
@RequestMapping("/front/reservations")
@RequiredArgsConstructor
public class ReservationFrontController {

    private final ReservationService reservationService;

    @PostMapping("/{lectureId}")
    public ResponseEntity<?> addReservation(@RequestBody String empNo, @PathVariable Long lectureId) {
        long id = reservationService.addReservation(empNo, lectureId);
        return new ResponseEntity<>(createLectureDto(id, empNo, lectureId), HttpStatus.CREATED);
    }

    @GetMapping
    public Page<ReservationLectureDto> getReservation(@RequestBody String empNo, Pageable pageable) {
        return reservationService.getReservation(empNo, pageable)
                .map(r -> createReservationLectureDto(r.getLecture(), r.getStatus()));
    }

    @Getter
    static class ReservationDto {
        private Long id;
        private String empNo;
        private Long lectureId;
        public static ReservationDto createLectureDto(Long id, String empNo, Long lectureId) {
            ReservationDto dto = new ReservationDto();
            dto.id = id;
            dto.empNo = empNo;
            dto.lectureId = lectureId;
            return dto;
        }
    }
}
