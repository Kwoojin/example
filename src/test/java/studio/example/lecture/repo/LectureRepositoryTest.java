package studio.example.lecture.repo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import studio.example.lecture.domain.Lecture;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static studio.example.lecture.domain.Lecture.createLecture;

@SpringBootTest
@Transactional
class LectureRepositoryTest {

    @Autowired LectureRepository lectureRepository;
    @PersistenceContext EntityManager em;

    @Test
    public void save() {
        LocalDateTime now = LocalDateTime.now();
        Lecture lecture = createLecture("강연명", "장소", "강사", 100, now.plusDays(1), now.plusDays(1).plusHours(1), "content");
        Lecture saveLecture = lectureRepository.save(lecture);

        Lecture findLecture = lectureRepository.findById(saveLecture.getId()).orElseThrow();
        assertThat(findLecture).isEqualTo(saveLecture);
    }

    @Test
    @DisplayName("기간 & 장소 중복 검사")
    public void findLectureDuplicatePlaceCheck() {
        LocalDateTime now = LocalDateTime.now();
        String place = "same";
        Lecture lecture1 = createLecture("강연명", place, "강사", 100, now.plusDays(1), now.plusDays(1).plusHours(1), "content");
        Lecture lecture2 = createLecture("강연명", place, "강사", 100, now.plusDays(2), now.plusDays(2).plusHours(1), "content");
        Lecture lecture3 = createLecture("강연명", place, "강사", 100, now.plusDays(3), now.plusDays(3).plusHours(1), "content");
        em.persist(lecture1);
        em.persist(lecture2);
        em.persist(lecture3);

        em.flush();
        em.clear();

        LocalDateTime startTime = now.plusDays(1);
        LocalDateTime endTime = now.plusDays(2).plusHours(1);

        //장소가 같고 시간이 겹치는 것이 2개
        List<Lecture> results1 = lectureRepository.findLectureDuplicatePlaceCheck(startTime, endTime, place);
        assertThat(results1.size()).isEqualTo(2);
        assertThat(results1).containsExactly(lecture1, lecture2);

        //기간은 겹치지만 장소가 다를 때
        startTime = now.plusDays(1);
        endTime = now.plusDays(2).plusHours(1);
        List<Lecture> results2 = lectureRepository.findLectureDuplicatePlaceCheck(startTime, endTime, "diff");
        assertThat(results2.size()).isEqualTo(0);

        //장소가 같지만 기간이 다를 때
        startTime = now.plusDays(1).plusHours(2);
        endTime = now.plusDays(1).plusHours(4);
        List<Lecture> results3 = lectureRepository.findLectureDuplicatePlaceCheck(startTime, endTime, place);

        assertThat(results3.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("강연은 7일전 ~ 1일 후 까지 공개")
    public void findLecturePeriod() {
        LocalDateTime now = LocalDateTime.now();
        Lecture lecture1 = getLecture(now.minusDays(2));
        Lecture lecture2 = getLecture(now.minusDays(1).plusHours(1));
        Lecture lecture3 = getLecture(now);
        Lecture lecture4 = getLecture(now.plusDays(2));
        Lecture lecture5 = getLecture(now.plusDays(5));
        Lecture lecture6 = getLecture(now.plusDays(8));
        em.persist(lecture1);
        em.persist(lecture2);
        em.persist(lecture3);
        em.persist(lecture4);
        em.persist(lecture5);
        em.persist(lecture6);

        em.flush();
        em.clear();

        LocalDateTime displayStartTime = LocalDateTime.now().minusDays(1);
        LocalDateTime displayEndTime = LocalDateTime.now().plusDays(7);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Lecture> results = lectureRepository.findLecturePeriod(displayStartTime, displayEndTime, pageRequest);
        List<Lecture> content = results.getContent();

        assertThat(content.size()).isEqualTo(4);
    }

    static int idx=0;

    private Lecture getLecture(LocalDateTime startTime) {
        return createLecture("강연명"+ ++idx, "장소"+idx, "강사"+idx, 100+idx, startTime.plusHours(idx*2), startTime.plusHours(idx*2).plusHours(1), "content"+idx);
    }
}
