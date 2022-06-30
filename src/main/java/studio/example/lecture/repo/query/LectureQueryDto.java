package studio.example.lecture.repo.query;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString(of = {"id", "title", "place", "lecturer"})
public class LectureQueryDto {
    private Long id;
    private String title;
    private String place;
    private String lecturer;
    List<String> empNos;

    @QueryProjection
    public LectureQueryDto(Long id, String title, String place, String lecturer) {
        this.id = id;
        this.title = title;
        this.place = place;
        this.lecturer = lecturer;
    }

    public void setEmpNos(List<String> empNos) {
        this.empNos = empNos;
    }
}
