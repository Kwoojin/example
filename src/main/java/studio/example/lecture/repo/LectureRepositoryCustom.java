package studio.example.lecture.repo;

import studio.example.lecture.repo.query.LectureQueryDto;
import studio.example.lecture.repo.query.LectureQueryMemberDto;

import java.util.List;

public interface LectureRepositoryCustom {
    List<LectureQueryDto> searchPageLecture ();

    List<LectureQueryMemberDto> searchMemberByLecture(List<Long> ids);
}
