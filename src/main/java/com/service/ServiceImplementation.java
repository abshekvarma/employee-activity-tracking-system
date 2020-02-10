package com.service;

import com.constants.Constants;
import com.domain.ActivityResponse;
import com.domain.EmployeeActivity;
import com.domain.EmployeeStatistics;
import com.domain.Response;
import com.entity.Activity;
import com.entity.EmployeeActivityEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.repository.IRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Service
@Slf4j
public class ServiceImplementation implements IService {

    @Autowired
    private IRepository repository;

    @Override
    public void readAllFiles() {
        File file = new File(Constants.FOLDER_NAME);
        File[] allFiles = file.listFiles();
        if (allFiles != null) {
            for (File eachFile :
                    allFiles) {
                if (eachFile != null) {
                    saveFileToDatabase(eachFile);
                }
            }
        }
    }

    private void saveFileToDatabase(File files) {
        log.info("processing File");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            EmployeeActivityEntity employeeActivity = objectMapper.readValue(files, EmployeeActivityEntity.class);
            if (employeeActivity != null) {
                for (Activity activity : employeeActivity.getActivities()) {
                    if (Stream.of(Constants.LOGIN, Constants.LOGOUT, Constants.TEABREAK, Constants.LUNCHBREAK, Constants.GAMEMOOD, Constants.NAPTIME).noneMatch(activity.getName()::equalsIgnoreCase)) {
                        log.error("Activity Names are Unmatched, unable to Append to Database");
                        employeeActivity = new EmployeeActivityEntity();
                        break;
                    }
                }
                repository.save(employeeActivity);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public Response processResponse() {
        log.info("inside processResponse() method");
        Iterable<EmployeeActivityEntity> employeeActivityEntityIterable = repository.findAll();
        Response response = new Response();
        List<EmployeeActivity> employeeTodayActivityList = new ArrayList<>();
        List<EmployeeStatistics> employeeStatisticsList = new ArrayList<>();
        Map<String, Integer> activityNameMap = new HashMap<>();
        for (EmployeeActivityEntity employeeActivityEntity : employeeActivityEntityIterable) {
            employeeTodayActivityList.add(getTodayActivity(employeeActivityEntity));
            mapAllEmployeeStatistics(employeeActivityEntity, activityNameMap);
        }
        activityNameMap.forEach((s, integer) -> {
            EmployeeStatistics employeeStatistics = new EmployeeStatistics();
            employeeStatistics.setActivity_name(s);
            employeeStatistics.setOccurrences(integer);
            employeeStatisticsList.add(employeeStatistics);
        });
        employeeStatisticsList.sort((o1, o2) -> o2.getOccurrences().compareTo(o1.getOccurrences()));
        employeeTodayActivityList.sort((o1, o2) -> o2.getEmployee_id().compareTo(o1.getEmployee_id()));
        response.setAll_employees_last_7_days_statistics(employeeStatisticsList);
        response.setTodays_activities(employeeTodayActivityList);
        return response;
    }


    private EmployeeActivity getTodayActivity(EmployeeActivityEntity employeeActivityEntity) {
        log.info("inside getTodayActivity() method");
        EmployeeActivity employeeActivity = new EmployeeActivity();
        List<ActivityResponse> activityResponseList = new ArrayList<>();
        for (Activity activity : employeeActivityEntity.getActivities()) {
            ActivityResponse activityResponse = new ActivityResponse();
            Date date = DateUtils.addMilliseconds(new Date(activity.getTime()), activity.getDuration());
            if (TimeUnit.MILLISECONDS.toHours(new Date().getTime() - date.getTime()) < Constants.HOURS) {
                activityResponse.setName(activity.getName());
                activityResponse.setStart_time(activity.getTime());
                activityResponseList.add(activityResponse);
            }
        }
        employeeActivity.setEmployee_id(employeeActivityEntity.getEmployee_id());
        employeeActivity.setActivities(activityResponseList);
        log.info("returning response");
        return employeeActivity;
    }

    private void mapAllEmployeeStatistics(EmployeeActivityEntity employeeActivityEntity, Map<String, Integer> activityNameMap) {
        log.info("Inside mapAllEmployeeStatistics() method");
        for (Activity activity : employeeActivityEntity.getActivities()) {
            Date date = DateUtils.addMilliseconds(new Date(activity.getTime()), activity.getDuration());
            if (TimeUnit.MILLISECONDS.toDays(new Date().getTime() - date.getTime()) < Constants.DAYS) {
                activityNameMap.put(activity.getName(), activityNameMap.getOrDefault(activity.getName(), 0) + 1);
            }
        }
    }

}
