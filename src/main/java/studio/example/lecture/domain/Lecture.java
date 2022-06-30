package studio.example.lecture.domain;

import lombok.*;
import org.springframework.util.StringUtils;
import studio.example.lecture.error.NotEnoughSeatException;
import studio.example.lecture.error.StartTimeGoeEndTimeException;
import studio.example.model.BaseTimeEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@SequenceGenerator(
        name="lecture_seq_generator",
        sequenceName = "lecture_seq",
        initialValue = 1, allocationSize = 50
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Lecture extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lecture_seq_generator")
    @Column(name = "lecture_id")
    private Long id;

    private String title;
    private String place;
    private String lecturer;
    private int totalNumberSeats;
    private int remainingNumberSeats;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String content;

    public static Lecture createLecture(String title, String place, String lecturer, int totalNumberSeats, LocalDateTime startTime, LocalDateTime endTime, String content) {
        if(startTime.isAfter(endTime) || startTime.isEqual(endTime)){
            throw new StartTimeGoeEndTimeException("end time must be greater than start time");
        }

        Lecture dto = new Lecture();
        dto.title = StringUtils.hasText(title) ? title : lecturer;
        dto.place = place;
        dto.lecturer = lecturer;
        dto.totalNumberSeats = totalNumberSeats;
        dto.remainingNumberSeats = totalNumberSeats;
        dto.startTime = startTime;
        dto.endTime = endTime;
        dto.content = content;
        return dto;
    }

    public void reservationLecture() {
        int remainingNumberSeats = this.remainingNumberSeats -1;
        if(remainingNumberSeats < 0) {
            throw new NotEnoughSeatException("All seats are reserved and cannot be reserved");
        }
        this.remainingNumberSeats = remainingNumberSeats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lecture lecture = (Lecture) o;
        return Objects.equals(getId(), lecture.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
