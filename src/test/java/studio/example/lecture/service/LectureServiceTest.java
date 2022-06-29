package studio.example.lecture.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import studio.example.lecture.domain.Lecture;
import studio.example.lecture.error.DuplicatePlaceAndTimeException;
import studio.example.lecture.repo.LectureRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static studio.example.lecture.domain.Lecture.createLecture;

@SpringBootTest
@Transactional
class LectureServiceTest {

    @Autowired LectureService lectureService;
    @Autowired LectureRepository lectureRepository;
    @PersistenceContext EntityManager em;

    @Test
    public void save() {
        LocalDateTime now = LocalDateTime.now();
        Lecture lecture1 = createLecture("강연명1", "장소1", "강사1", 100, now.plusDays(1), now.plusDays(1).plusHours(1), "content1");
        em.persist(lecture1);
        em.flush();
        em.clear();

        //시간은 같지만 장소는 다른 경우
        Lecture lecture2 = createLecture("강연명2", "장소2", "강사2", 100, now.plusDays(1), now.plusDays(1).plusHours(1), "content2");
        Long l2 = lectureService.addLecture(lecture2);
        //장소는 같지만 시간이 다른 경우
        Lecture lecture3 = createLecture("강연명3", "장소1", "강사3", 100, now.plusDays(2), now.plusDays(2).plusHours(1), "content3");
        Long l3 = lectureService.addLecture(lecture3);

        Lecture findLecture2 = lectureRepository.findById(l2).orElseThrow();
        Lecture findLecture3 = lectureRepository.findById(l3).orElseThrow();
        Assertions.assertThat(findLecture2).isEqualTo(lecture2);
        Assertions.assertThat(findLecture3).isEqualTo(lecture3);
    }

    @Test
    @DisplayName("장소 및 시간이 겹칠 경우")
    public void saveFail() {
        LocalDateTime now = LocalDateTime.now();
        Lecture lecture1 = createLecture("강연명1", "장소", "강사1", 100, now.plusDays(1), now.plusDays(1).plusHours(1), "content1");
        em.persist(lecture1);
        em.flush();
        em.clear();

        Lecture lecture2 = createLecture("강연명2", "장소", "강사2", 100, now.plusDays(1).minusHours(1), now.plusDays(1).plusHours(2), "content2");

        Assertions.assertThatThrownBy(() -> lectureService.addLecture(lecture2))
                .isInstanceOf(DuplicatePlaceAndTimeException.class);
    }

}