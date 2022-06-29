package studio.example.lecture.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studio.example.lecture.domain.Lecture;
import studio.example.lecture.error.DuplicatePlaceAndTimeException;
import studio.example.lecture.repo.LectureRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LectureService {

    private final LectureRepository lectureRepository;

    @Transactional
    public Long addLecture(Lecture lecture) {
        List<Lecture> lectures = lectureRepository.findLectureDuplicatePlaceCheck(lecture.getStartTime(), lecture.getEndTime(), lecture.getPlace());
        if(lectures.size() > 0){
            throw new DuplicatePlaceAndTimeException("There is already another lecture at that time and place");
        }

        Lecture saveLecture = lectureRepository.save(lecture);
        return saveLecture.getId();
    }

    public Page<Lecture> searchLecturePeriod(Pageable pageable) {
        LocalDateTime displayStartTime = LocalDateTime.now().minusDays(1);
        LocalDateTime displayEndTime = LocalDateTime.now().plusDays(7);
        return lectureRepository.findLecturePeriod(displayStartTime, displayEndTime, pageable);
    }
}
