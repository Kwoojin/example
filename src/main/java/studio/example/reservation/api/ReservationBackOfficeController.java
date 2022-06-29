package studio.example.reservation.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import studio.example.reservation.repo.ReservationRepository;

@RestController
@RequestMapping("/back-office/reservations")
@RequiredArgsConstructor
public class ReservationBackOfficeController {

}
