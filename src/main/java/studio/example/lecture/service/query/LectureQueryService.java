package studio.example.lecture.service.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studio.example.lecture.repo.LectureRepository;
import studio.example.lecture.repo.query.LectureQueryDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LectureQueryService {

    private final LectureRepository lectureRepository;

    public List<LectureQueryDto> findEmpNoListByLecture() {
        List<LectureQueryDto> result = lectureRepository.searchPageLecture();

        List<Long> ids = toLectureIds(result);
        Map<Long, List<String>> empNosMap = lectureRepository.searchMemberByLecture(ids).stream()
                .collect(groupingBy(o -> o.getId(), mapping(o -> o.getEmpNo(), toList())));

        result.forEach( o -> {
            o.setEmpNos(empNosMap.getOrDefault(o.getId(), new ArrayList<>()));
        });

        return result;
    }

    private List<Long> toLectureIds(List<LectureQueryDto> result) {
        return result.stream()
                .map(ob -> ob.getId())
                .collect(toList());
    }

}
