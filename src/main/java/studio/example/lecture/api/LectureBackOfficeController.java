package studio.example.lecture.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import studio.example.lecture.api.dto.LectureDto;
import studio.example.lecture.api.dto.LectureSaveForm;
import studio.example.lecture.domain.Lecture;
import studio.example.lecture.repo.LectureRepository;
import studio.example.lecture.service.LectureService;
import studio.example.model.ResultDto;

import static studio.example.lecture.api.dto.LectureDto.createLectureDto;

@RestController
@RequestMapping("/back-office/lectures")
@RequiredArgsConstructor
@Slf4j
public class LectureBackOfficeController {

    private final LectureRepository lectureRepository;
    private final LectureService lectureService;

    @GetMapping
    public Page<LectureDto> lectures(Pageable pageable) {
        return lectureRepository.findAll(pageable)
                .map(LectureDto::createLectureDto);
    }

    @PostMapping
    public ResponseEntity<ResultDto> addLecture(@Validated @RequestBody LectureSaveForm form) {
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

        return new ResponseEntity<>(ResultDto.builder()
                .content(createLectureDto(lecture))
                .build(),
                HttpStatus.OK);
    }

}
