package com.example.demo.service;

import java.util.List;

import com.example.demo.model.Task;
// import java.util.List;
import com.example.demo.repository.TaskMapper;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor//final fieldのコンストラクタを自動的に生成
public class TaskService {

    //TaskMapperをコンストラクタインジェクション
    private final TaskMapper mapper;
    // @Autowired
    // public TaskService(TaskMapper mapper) {
    //     this.mapper = mapper;
    // }
    


    //select全件
    public List<Task> selectAllTasks() {
        return mapper.selectAllTasks();
    }

    //select undoneのみ 予定日順　
    public List<Task> selectUndoneTasks(String userID) {
        return mapper.selectUndoneTasks(userID);
    }

    //select undone 優先度順
    public List<Task> selectUndoneTasksByPriority() {
        return mapper.selectUndoneTasksByPriority();
    }

    //select Doneのみ
    public List<Task> selectDoneTasks() {
        return mapper.selectDoneTasks();
    }

    //select todayのみ
    public List<Task> selectTodayTask() {
        return mapper.selectTodayTask();
    }




    
    //select1件
    public Task selectOne(int taskID) {
    	return mapper.selectOne(taskID);
    }
    
    /**
     * task新規登録処理
     * @param task
     */
    public void insertOneTask(Task task) {
    	mapper.insertOneTask(task);
    }
    // public void registerUserID(int taskID, String userID) {
    //     mapper.registerUserID(taskID, userID);
    // }

    //edit
    public void updateOneTask(Task task) {
        mapper.updateOneTask(task);
    }

    /**
     * 完了ボタン押下時、done処理
     * @param taskID
     */
    public void done(int taskID) {
        mapper.done(taskID);
    }

    //undone
    public void undone(int taskID) {
        mapper.undone(taskID);
    }

    //delete
    public void deleteTask(int taskID) {
        mapper.deleteTask(taskID);
    }

}
