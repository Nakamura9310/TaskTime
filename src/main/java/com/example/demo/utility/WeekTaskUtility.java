package com.example.demo.utility;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import com.example.demo.model.Task;
import com.example.demo.service.TaskService;

import org.springframework.stereotype.Service;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Service
@Data
@RequiredArgsConstructor
public class WeekTaskUtility {

    private final TaskService taskService;

    private int monEstimatedTime = 0;
    private int tueEstimatedTime = 0;
    private int wedEstimatedTime = 0;
    private int thuEstimatedTime = 0;
    private int friEstimatedTime = 0;
    private int satEstimatedTime = 0;
    private int sunEstimatedTime = 0;




    public void weekTask() {
        List<Task> task = taskService.selectUndoneTasks();
    
        LocalDate today = LocalDate.now();
        LocalDate mon = today.with(DayOfWeek.MONDAY);
        LocalDate tue = today.with(DayOfWeek.TUESDAY);
        LocalDate wed = today.with(DayOfWeek.WEDNESDAY);
        LocalDate thu = today.with(DayOfWeek.THURSDAY);
        LocalDate fri = today.with(DayOfWeek.FRIDAY);
        LocalDate sat = today.with(DayOfWeek.SATURDAY);
        LocalDate sun = today.with(DayOfWeek.SUNDAY);
        
        for(int i = 0; i < task.size(); i++) {
            if(mon.toString().equals(task.get(i).getScheduledDate())) {monEstimatedTime += task.get(i).getEstimatedTime();}
            if(tue.toString().equals(task.get(i).getScheduledDate())) {tueEstimatedTime += task.get(i).getEstimatedTime();}
            if(wed.toString().equals(task.get(i).getScheduledDate())) {wedEstimatedTime += task.get(i).getEstimatedTime();}
            if(thu.toString().equals(task.get(i).getScheduledDate())) {thuEstimatedTime += task.get(i).getEstimatedTime();}
            if(fri.toString().equals(task.get(i).getScheduledDate())) {friEstimatedTime += task.get(i).getEstimatedTime();}
            if(sat.toString().equals(task.get(i).getScheduledDate())) {satEstimatedTime += task.get(i).getEstimatedTime();}
            if(sun.toString().equals(task.get(i).getScheduledDate())) {sunEstimatedTime += task.get(i).getEstimatedTime();}
        }

    }


    

}
