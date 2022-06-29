package studio.example.lecture.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import studio.example.lecture.api.dto.LectureListDto;
import studio.example.lecture.api.dto.LectureSaveForm;
import studio.example.lecture.domain.Lecture;
import studio.example.lecture.error.StartTimeGoeEndTimeException;
import studio.example.lecture.repo.LectureRepository;
import studio.example.lecture.service.LectureService;

@RestController
@RequestMapping("/back-office/lectures")
@RequiredArgsConstructor
@Slf4j
public class LectureBackOfficeController {

    private final LectureRepository lectureRepository;
    private final LectureService lectureService;

    @GetMapping
    public Page<LectureListDto> lectures(Pageable pageable) {
        return lectureRepository.findAll(pageable)
                .map(lecture -> LectureListDto.createLectureListDto(
                        lecture.getTitle(),
                        lecture.getPlace(),
                        lecture.getLecturer()
                ));
    }

    @PostMapping
    public ResponseEntity<?> addLecture(@Validated @RequestBody LectureSaveForm form) {
        Lecture lecture = Lecture.createLecture(
                form.getTitle(),
                form.getPlace(),
                form.getLecturer(),
                form.getTotalCount(),
                form.getStartTime(),
                form.getEndTime(),
                form.getContent()
        );
        lectureService.addLecture(lecture);

        return new ResponseEntity<>(form, HttpStatus.CREATED);
    }
}
