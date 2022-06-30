package studio.example.lecture.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import studio.example.lecture.api.dto.LectureDto;
import studio.example.lecture.api.dto.LectureSaveForm;
import studio.example.lecture.domain.Lecture;
import studio.example.lecture.repo.LectureRepository;
import studio.example.lecture.service.LectureService;
import studio.example.lecture.service.query.LectureQueryService;
import studio.example.model.ResultDto;

import java.util.stream.Collectors;

import static studio.example.lecture.api.dto.LectureDto.createLectureDto;

@RestController
@RequestMapping("/back-office/lectures")
@RequiredArgsConstructor
@Slf4j
public class LectureBackOfficeController {

    private final LectureRepository lectureRepository;
    private final LectureService lectureService;
    private final LectureQueryService lectureQueryService;

    @GetMapping
    public ResultDto getLectures() {
        return ResultDto.builder()
                .content((lectureRepository.findAll().stream()
                    .map(LectureDto::createLectureDto)
                    .collect(Collectors.toList()))
                ).build();
    }

    @PostMapping
    public ResultDto addLecture(@Validated @RequestBody LectureSaveForm form) {
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

        return ResultDto.builder()
                .content(createLectureDto(lecture))
                .build();
    }

    @GetMapping("/empNos")
    public ResultDto getEmpNoListByLectures() {
        return ResultDto.builder()
                .content(lectureQueryService.findEmpNoListByLecture())
                .build();
    }

}
