package studio.example.lecture.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import studio.example.lecture.domain.Lecture;

import java.time.LocalDateTime;
import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long>, LectureRepositoryCustom {

    @Query(value = "select l from Lecture l where l.place = :place and (l.startTime between :startTime and :endTime or l.endTime between :startTime and :endTime)")
    List<Lecture> findLectureDuplicatePlaceCheck(@Param("startTime") LocalDateTime startTime,@Param("endTime") LocalDateTime endTime,@Param("place") String place);

    @Query(value = "select l from Lecture l where l.startTime between :displayStartTime and :displayEndTime")
    List<Lecture> findLecturePeriod(@Param("displayStartTime") LocalDateTime displayStartTime, @Param("displayEndTime") LocalDateTime displayEndTime);
//    Page<Lecture> findLecturePeriod(@Param("displayStartTime") LocalDateTime displayStartTime, @Param("displayEndTime") LocalDateTime displayEndTime, Pageable pageable);

}
