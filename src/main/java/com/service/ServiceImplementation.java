package com.service;

import com.constants.Constants;
import com.domain.*;
import com.entity.Activity;
import com.entity.EmployeeActivityEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.repository.IRepositoryActivity;
import com.repository.IRepositoryEmployee;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

@SuppressWarnings("unused")
@Service
@Slf4j
public class ServiceImplementation implements IService {

    @Autowired
    private IRepositoryActivity iRepositoryActivity;

    @Autowired
    private IRepositoryEmployee iRepositoryEmployee;

    @PostConstruct
    public void readAllFiles() {
        File file = new File(Constants.FOLDER_NAME);
        if (file.mkdir()) {
            log.info("Directory Created for the path " + Constants.FOLDER_NAME);
        }
        File[] allFiles = file.listFiles();
        if (allFiles != null) {
            for (File eachFile :
                    allFiles) {
                if (eachFile != null) {
                    saveFileToDatabase(eachFile);
                }
            }
        } else {
            log.error("No Files present ");
        }
    }

    private void saveFileToDatabase(File files) {
        log.info("processing File");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            EmployeeActivityEntity employeeActivityEntity = new EmployeeActivityEntity();
            RequestEmployeeActivity requestEmployeeActivity = objectMapper.readValue(files, RequestEmployeeActivity.class);
            if (requestEmployeeActivity != null) {
                for (RequestActivity requestActivity : requestEmployeeActivity.getActivities()) {
                    if (Stream.of(Constants.LOGIN, Constants.LOGOUT, Constants.TEABREAK, Constants.LUNCHBREAK, Constants.GAMEMOOD, Constants.NAPTIME).noneMatch(requestActivity.getName()::equalsIgnoreCase)) {
                        log.error("Activity Names are Unmatched, unable to Append to Database");
                        continue;
                    }
                    Activity activity = new Activity();
                    activity.setName(requestActivity.getName());
                    activity.setDate(DateUtils.addMinutes(new Date(requestActivity.getTime()), requestActivity.getDuration()));
                    employeeActivityEntity.addActivities(activity);
                }
                employeeActivityEntity.setEmployee_id(requestEmployeeActivity.getEmployee_id());
                iRepositoryEmployee.save(employeeActivityEntity);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public Response processResponse() {
        log.info("inside processResponse() method");
        Response response = new Response();
        List<ResponseActivity> employeeTodayActivityList = new ArrayList<>();
        List<ResponseStatistics> employeeStatisticsList = new ArrayList<>();
        Map<String, Integer> activityNameMap = new HashMap<>();
        employeeTodayActivityList.add(getTodayActivity());
        mapAllEmployeeStatistics(activityNameMap);
        activityNameMap.forEach((s, integer) -> {
            ResponseStatistics employeeStatistics = new ResponseStatistics();
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

    private ResponseActivity getTodayActivity() {
        log.info("inside getTodayActivity() method");
        ResponseActivity employeeActivity = new ResponseActivity();
        List<ActivityDomain> activityDomainResponseList = new ArrayList<>();
        List<Activity> activityList = iRepositoryActivity.findActivityByDateAfter(DateUtils.addHours(new Date(), -Constants.HOURS));
        for (com.entity.Activity activity : activityList) {
            ActivityDomain activityDomainResponse = new ActivityDomain();
            activityDomainResponse.setName(activity.getName());
            activityDomainResponse.setStart_time(activity.getDate().getTime());
            activityDomainResponseList.add(activityDomainResponse);
            employeeActivity.setEmployee_id(activity.getEmployeeActivityEntity().getEmployee_id());
        }
        employeeActivity.setActivities(activityDomainResponseList);
        log.info("returning response");
        return employeeActivity;
    }

    private void mapAllEmployeeStatistics(Map<String, Integer> activityNameMap) {
        log.info("Inside mapAllEmployeeStatistics() method");
        List<Activity> activityList = iRepositoryActivity.findActivityByDateAfter(DateUtils.addDays(new Date(), -Constants.DAYS));
        for (com.entity.Activity activity : activityList) {
            activityNameMap.put(activity.getName(), activityNameMap.getOrDefault(activity.getName(), 0) + 1);
        }
    }
}
