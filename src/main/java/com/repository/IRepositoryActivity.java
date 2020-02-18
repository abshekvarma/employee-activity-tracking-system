package com.repository;

import com.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface IRepositoryActivity extends JpaRepository<Activity, Long> {
    List<Activity> findActivityByDateAfter(Date date);
}
