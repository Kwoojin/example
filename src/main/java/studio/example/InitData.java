package studio.example;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import studio.example.lecture.repo.LectureRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

import static studio.example.lecture.domain.Lecture.*;

@Profile("local")
@Component
@RequiredArgsConstructor
public class InitData {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final LectureRepository lectureRepository;

        public void dbInit1() {
            lectureRepository.save(createLecture(
                "1", "1", "1", 100, LocalDateTime.now(), LocalDateTime.now(), "2"
            ));

            lectureRepository.save(createLecture(
                    "2", "2", "2", 100, LocalDateTime.now().plusDays(7), LocalDateTime.now(), "2"
            ));

            lectureRepository.save(createLecture(
                    "3", "3", "3", 100, LocalDateTime.now().minusDays(7), LocalDateTime.now(), "2"
            ));
        }
    }
}
