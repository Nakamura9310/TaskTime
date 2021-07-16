package com.example.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.demo.model.Task;

@Mapper
public interface TaskMapper {

    //select全件
    @Select("SELECT * FROM Task")
    public List<Task> selectAllTasks();

    //select undoneのみ 予定日順　
    @Select("SELECT * FROM Task WHERE done = false ORDER BY " +
        "CASE " +
            "WHEN scheduledDate IS NULL THEN'2' " +//null最後
            "WHEN scheduledDate = '' THEN '1' " +//空文字最後
            "ELSE '0' " +
        "END, scheduledDate ASC")
    public List<Task> selectUndoneTasks();

    //select　優先度順
    @Select("SELECT * FROM Task WHERE done = false ORDER BY " +
        "CASE " +
            "WHEN priority IS NULL THEN '2' " +//null最後
            "WHEN priority = '' THEN '1' " +//空文字最後
            "ELSE '0' " +
        "END, priority, scheduledDate ASC")
    public List<Task> selectUndoneTasksByPriority();

    //select doneのみ
    @Select("SELECT * FROM Task WHERE done = true")
    public List<Task> selectDoneTasks();

    //select todayのみ
    @Select("SELECT * FROM Task WHERE scheduledDate = CURRENT_DATE AND done = false")
    public List<Task> selectTodayTask();
    
    /**
     * select1件処理
     * @param taskID
     * @return
     */
    @Select("SELECT * FROM Task WHERE taskID = #{taskID}")
    public Task selectOne(int taskID);
    
    //insert1件
    @Insert("INSERT INTO Task (taskName, estimatedTime, scheduledDate, startTime) VALUES (#{taskName}, #{estimatedTime}, #{scheduledDate}, #{startTime})")
    public void insertOne(Task task);

    //edit task編集
    @Update("UPDATE Task SET "+
        "taskName = #{taskName}, "+
        "estimatedTime = #{estimatedTime}, "+
        "scheduledDate = #{scheduledDate}, "+
        "startTime = #{startTime}, "+
        "priority = #{priority} "+
        "WHERE taskID = #{taskID}")
    public void updateOneTask(Task task);


    /**
     * done処理
     * done→trueへ
     * completionDate→CURRENT_DATE
     * @param taskID
     */
    @Update("UPDATE Task SET done = true, completionDate = CURRENT_DATE WHERE taskID = #{taskID}")
    public void done(int taskID);

    /**
     * undone処理
     * done→falseへ
     * comletionDate→null
     * @param taskID
     */
    @Update("UPDATE Task SET done = false, completionDate = null WHERE taskID = #{taskID}")
    public void undone(int taskID);

    //taskのdelete処理
    @Delete("DELETE from Task WHERE taskID = #{taskID}")
    public void deleteTask(int taskID);


}
