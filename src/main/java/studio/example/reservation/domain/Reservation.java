package studio.example.reservation.domain;


import lombok.*;
import studio.example.lecture.domain.Lecture;
import studio.example.member.domain.Member;
import studio.example.model.BaseTimeEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@SequenceGenerator(
        name="reservation_seq_generator",
        sequenceName = "reservation_seq",
        initialValue = 1, allocationSize = 50
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reservation_seq_generator")
    @Column(name = "reservation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_reservation_to_member"))
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id", foreignKey = @ForeignKey(name = "fk_reservation_to_lecture"))
    private Lecture lecture;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    public static Reservation createReservation(Member member, Lecture lecture) {
        Reservation dto = new Reservation();
        dto.member = member;
        dto.lecture = lecture;
        dto.status = ReservationStatus.COMPLETE;

        lecture.reservationLecture();
        return dto;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}