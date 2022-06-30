package studio.example.lecture.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import studio.example.lecture.api.dto.LectureDto;
import studio.example.lecture.service.LectureService;
import studio.example.model.ResultDto;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/front/lectures")
@RequiredArgsConstructor
@Slf4j
public class LectureFrontController {

    private final LectureService lectureService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResultDto lectures() {
        return ResultDto.builder()
                .content(lectureService.searchLecturePeriod().stream()
                .map(LectureDto::createLectureDto)
                .collect(Collectors.toList()))
                .build();
    }
}
