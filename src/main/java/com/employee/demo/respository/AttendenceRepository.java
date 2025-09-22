package com.employee.demo.respository;

import com.employee.demo.models.Attendance;
import com.employee.demo.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendenceRepository extends JpaRepository<Attendance, Long> {
    Attendance findByEmployeeAndDate(Users employee, LocalDate date);

    List<Attendance> findByEmployee_Id(Long id);

}
