package studio.example.reservation.api.dto;

import lombok.Getter;
import studio.example.lecture.domain.Lecture;
import studio.example.reservation.domain.ReservationStatus;

import java.time.LocalDateTime;

@Getter
public class ReservationLectureDto {

    private Long id;
    private String title;
    private String place;
    private String lecturer;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String content;
    private ReservationStatus status;

    public static ReservationLectureDto createReservationLectureDto(Lecture lecture, ReservationStatus status) {
        ReservationLectureDto dto = new ReservationLectureDto();
        dto.id = lecture.getId();
        dto.title = lecture.getTitle();
        dto.place = lecture.getPlace();
        dto.lecturer = lecture.getLecturer();
        dto.startTime = lecture.getStartTime();
        dto.endTime = lecture.getEndTime();
        dto.content = lecture.getContent();
        dto.status = status;
        return dto;
    }

}
