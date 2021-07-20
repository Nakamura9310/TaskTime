package com.example.demo.model;



import lombok.Data;

@Data
public class Task {
	
	//User情報と紐付けするためのフィールド
	private String userID;
	
	//task識別番号
	private int taskID;
	
	private String taskName;
	
	private double estimatedTime;
	
	private String scheduledDate;
	
	private String startTime;
	
	private String accumlatedTime;
	
	private boolean done;
	
	private String completionDate;

	private String priority;

}
