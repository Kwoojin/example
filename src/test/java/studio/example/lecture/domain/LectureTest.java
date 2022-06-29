package studio.example.lecture.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import studio.example.lecture.error.StartTimeGoeEndTimeException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static studio.example.lecture.domain.Lecture.*;

@Transactional
@SpringBootTest
class LectureTest {

    @Test
    @DisplayName("start time >= end time")
    public void startTimeGoeEndTime() {
        LocalDateTime startTime = LocalDateTime.now().plusDays(2);
        LocalDateTime endTime = LocalDateTime.now();

        assertThatThrownBy(() -> createLecture("강의제목", "장소", "강사", 100, startTime, endTime, "content"))
                .isInstanceOf(StartTimeGoeEndTimeException.class);
    }

    @Test
    @DisplayName("title 값이 null 또는 빈 값")
    public void titleIsNullThenLecturer() {
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = LocalDateTime.now().plusDays(1).plusHours(1);
        Lecture lecture1 = createLecture("", "장소", "강사1", 100, startTime, endTime, "content");
        Lecture lecture2 = createLecture(null, "장소", "강사2", 100, startTime, endTime, "content");

        assertThat(lecture1.getLecturer()).isEqualTo("강사1");
        assertThat(lecture2.getLecturer()).isEqualTo("강사2");
    }
}