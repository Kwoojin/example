package studio.example.reservation.domain;

import lombok.Getter;

@Getter
public enum ReservationStatus {
    RESERVATION("예약"), CANCEL("취소");

    private final String description;

    ReservationStatus(String description) {
        this.description = description;
    }
}
