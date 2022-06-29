package studio.example.reservation.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import studio.example.reservation.domain.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
