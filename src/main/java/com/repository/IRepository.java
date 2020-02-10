package com.repository;

import com.entity.EmployeeActivityEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRepository extends CrudRepository<EmployeeActivityEntity, Long> {
}
