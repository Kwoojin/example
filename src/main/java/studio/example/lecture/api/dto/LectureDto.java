package studio.example.lecture.api.dto;

import lombok.Getter;
import studio.example.lecture.domain.Lecture;

import java.time.LocalDateTime;

@Getter
public class LectureDto {

    private Long id;
    private String title;
    private String place;
    private String lecturer;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String content;

    public static LectureDto createLectureDto(Lecture ob) {
        LectureDto dto = new LectureDto();
        dto.id = ob.getId();
        dto.title = ob.getTitle();
        dto.place = ob.getPlace();
        dto.lecturer = ob.getLecturer();
        dto.startTime = ob.getStartTime();
        dto.endTime = ob.getEndTime();
        dto.content = ob.getContent();
        return dto;
    }
}
