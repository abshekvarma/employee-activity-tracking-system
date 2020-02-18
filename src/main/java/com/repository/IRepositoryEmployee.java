package com.repository;

import com.entity.EmployeeActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRepositoryEmployee extends JpaRepository<EmployeeActivityEntity, Long> {
}
