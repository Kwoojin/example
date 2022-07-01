package studio.example.reservation.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import studio.example.model.ResultDto;
import studio.example.reservation.api.dto.ReservationAddForm;
import studio.example.reservation.service.ReservationService;

import java.util.stream.Collectors;

import static studio.example.reservation.api.ReservationFrontController.ReservationDto.*;
import static studio.example.reservation.api.dto.ReservationLectureDto.*;

@RestController
@RequestMapping("/front/reservations")
@RequiredArgsConstructor
public class ReservationFrontController {

    private final ReservationService reservationService;

    @PostMapping("/{lectureId}")
    public ResultDto addReservation(@RequestBody ReservationAddForm form, @PathVariable Long lectureId) {
        long id = reservationService.addReservation(form.getEmpNo(), lectureId);
        return ResultDto.builder().content((createReservationDto(id, form.getEmpNo(), lectureId))).build();
    }

    @GetMapping
    public ResultDto getReservation(@RequestParam String empNo) {
        return ResultDto.builder()
                .content(reservationService.getReservation(empNo).stream()
                        .map(r -> createReservationLectureDto(r.getLecture(), r.getStatus()))
                        .collect(Collectors.toList()))
                .build();
    }

    @Getter
    static class ReservationDto {
        private Long id;
        private String empNo;
        private Long lectureId;
        public static ReservationDto createReservationDto(Long id, String empNo, Long lectureId) {
            ReservationDto dto = new ReservationDto();
            dto.id = id;
            dto.empNo = empNo;
            dto.lectureId = lectureId;
            return dto;
        }
    }
}
