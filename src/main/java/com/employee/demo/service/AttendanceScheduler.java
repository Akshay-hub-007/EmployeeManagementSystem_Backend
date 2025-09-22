package com.employee.demo.service;

import com.employee.demo.models.Attendance;
import com.employee.demo.models.Users;
import com.employee.demo.respository.AttendenceRepository;
import com.employee.demo.respository.UserDetailsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.NonUniqueResultException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AttendanceScheduler {

    private final UserDetailsRepository userRepository;
    private final AttendenceRepository attendanceRepository;
    private static final int BATCH_SIZE = 10000;

    @Transactional
    @Scheduled(cron = "0 16 10 * * *") // runs daily at 10:06 AM
    public void createDailyAbsentRecords() {
        LocalDate today = LocalDate.now();

        try (Stream<Users> userStream = userRepository.streamAllUsers()) {
            List<Attendance> batch = new ArrayList<>();

            userStream.forEach(emp -> {
                Attendance attendance = null;

                // Try to fetch today's attendance using existing repository method
                try {
                    attendance = attendanceRepository.findByEmployeeAndDate(emp, today);
                } catch (NonUniqueResultException e) {
                    // Multiple records exist, pick the first one
                    attendance = attendanceRepository.findAll()
                            .stream()
                            .filter(a -> a.getEmployee().getId().equals(emp.getId()) && a.getDate().equals(today))
                            .findFirst()
                            .orElse(null);
                }

                // Only add Absent record if no attendance exists for today
                if (attendance == null) {
                    batch.add(Attendance.builder()
                            .employee(emp)
                            .date(today)
                            .status("Absent")
                            .checkInTime(null)
                            .checkOutTime(null)
                            .workHours(0.0)
                            .createdAt(LocalDateTime.now())
                            .build());
                }

                // Save in batches
                if (batch.size() >= BATCH_SIZE) {
                    attendanceRepository.saveAll(batch);
                    attendanceRepository.flush();
                    batch.clear();
                }
            });

            // Save remaining records
            if (!batch.isEmpty()) {
                attendanceRepository.saveAll(batch);
                attendanceRepository.flush();
            }
        }

        System.out.println("Daily absent records created successfully for " + today);
    }
}
