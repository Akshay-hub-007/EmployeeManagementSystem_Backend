package com.employee.demo.respository;

import com.employee.demo.models.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRepository extends JpaRepository<Leave,Long> {
    List<Leave> findAllByEmployeeId(Long employeeId);

}
