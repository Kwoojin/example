package studio.example.reservation.domain;

import lombok.Getter;

@Getter
public enum ReservationStatus {
    COMPLETE("완료"), CANCEL("취소");

    private final String description;

    ReservationStatus(String description) {
        this.description = description;
    }
}
