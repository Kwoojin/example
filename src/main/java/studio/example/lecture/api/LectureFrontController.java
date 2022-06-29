package studio.example.lecture.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import studio.example.lecture.api.dto.LectureDto;
import studio.example.lecture.service.LectureService;

@RestController
@RequestMapping("/front/lectures")
@RequiredArgsConstructor
@Slf4j
public class LectureFrontController {

    private final LectureService lectureService;

    @GetMapping
    public Page<LectureDto> lectures(Pageable pageable) {
        return lectureService.searchLecturePeriod(pageable)
                .map(LectureDto::createLectureDto);
    }
}
