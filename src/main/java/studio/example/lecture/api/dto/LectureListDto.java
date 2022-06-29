package studio.example.lecture.api.dto;

import lombok.Getter;

@Getter
public class LectureListDto {

    private String title;
    private String place;
    private String lecturer;

    public static LectureListDto createLectureListDto(String title, String place, String lecturer) {
        LectureListDto dto = new LectureListDto();
        dto.title = title;
        dto.place = place;
        dto.lecturer = lecturer;
        return dto;
    }
}
