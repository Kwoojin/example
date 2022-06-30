package studio.example.lecture.repo.query;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class LectureQueryMemberDto {
    private Long id;
    private String empNo;

    @QueryProjection
    public LectureQueryMemberDto(Long id, String empNo) {
        this.id = id;
        this.empNo = empNo;
    }
}
