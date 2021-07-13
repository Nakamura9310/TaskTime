package com.example.demo.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.Task;
import com.example.demo.service.TaskService;
import com.example.demo.utility.GoogleCalendarAPI;

import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@Controller
@RequiredArgsConstructor
@RequestMapping("/app")
public class TaskTimeController {
	
	//コンストラクタインジェクション
	//@RequiredArgsConstructorによりコンストラクタ記述省略
	private final TaskService taskService;
	
	private final RestOperations restOperations = new RestTemplate();
	
	// 認可済みのクライアント情報は OAuth2AuthorizedClientService経由で取得できる
	private final OAuth2AuthorizedClientService authorizedClientService;
	
	private final GoogleCalendarAPI gcapi;

	
	/**
	 * 
	 * @param authentication
	 * @return OAuth2AuthorizedClient
	 * 
	 * https://spring.pleiades.io/spring-security/site/docs/current/api/org/springframework/security/oauth2/client/OAuth2AuthorizedClientService.html
	 * OAuth2AuthorizedClientServiceの
	 * メソッドloadAuthorizedClient(clientRegistrationId, principalName)を、
	 * 認証されたTokenを使って書き換えている
	 * 	clientRegistrationId - クライアントの登録の識別子
	 * 	principalName - エンドユーザー Principal の名前 (リソース所有者)
	 */
	private OAuth2AuthorizedClient getAuthorizedClient(OAuth2AuthenticationToken authentication) {
		return this.authorizedClientService.loadAuthorizedClient(
			authentication.getAuthorizedClientRegistrationId(), authentication.getName()
			);
	}
	
	
	//topページ表示
	@GetMapping("/top")
	public String displayTop(@AuthenticationPrincipal OAuth2User oauth2User, OAuth2AuthenticationToken authentication, Model model) {

		model.addAttribute("authorizedClient", this.getAuthorizedClient(authentication));
		model.addAttribute("attributes", oauth2User.getAttributes());
		model.addAttribute("email", oauth2User.getAttributes().get("email"));

		List<Task> task = taskService.selectUndoneTasks();
		
		
		LocalDate today = LocalDate.now();
		LocalDate mon = today.with(DayOfWeek.MONDAY);
		LocalDate tue = today.with(DayOfWeek.TUESDAY);
		LocalDate wed = today.with(DayOfWeek.WEDNESDAY);
		LocalDate thu = today.with(DayOfWeek.THURSDAY);
		LocalDate fri = today.with(DayOfWeek.FRIDAY);
		LocalDate sat = today.with(DayOfWeek.SATURDAY);
		LocalDate sun = today.with(DayOfWeek.SUNDAY);
		
		double monEstimatedTime = 0;
		double tueEstimatedTime = 0;
		double wedEstimatedTime = 0;
		double thuEstimatedTime = 0;
		double friEstimatedTime = 0;
		double satEstimatedTime = 0;
		double sunEstimatedTime = 0;
		
		for(int i = 0; i < task.size(); i++) {
				if(mon.toString().equals(task.get(i).getScheduledDate())) {monEstimatedTime += task.get(i).getEstimatedTime();}
				if(tue.toString().equals(task.get(i).getScheduledDate())) {tueEstimatedTime += task.get(i).getEstimatedTime();}
				if(wed.toString().equals(task.get(i).getScheduledDate())) {wedEstimatedTime += task.get(i).getEstimatedTime();}
				if(thu.toString().equals(task.get(i).getScheduledDate())) {thuEstimatedTime += task.get(i).getEstimatedTime();}
				if(fri.toString().equals(task.get(i).getScheduledDate())) {friEstimatedTime += task.get(i).getEstimatedTime();}
				if(sat.toString().equals(task.get(i).getScheduledDate())) {satEstimatedTime += task.get(i).getEstimatedTime();}
				if(sun.toString().equals(task.get(i).getScheduledDate())) {sunEstimatedTime += task.get(i).getEstimatedTime();}
		}

		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd");
		model.addAttribute("task", task);
		model.addAttribute("mon", mon.format(dtf));
		model.addAttribute("tue", tue.format(dtf));
		model.addAttribute("wed", wed.format(dtf));
		model.addAttribute("thu", thu.format(dtf));
		model.addAttribute("fri", fri.format(dtf));
		model.addAttribute("sat", sat.format(dtf));
		model.addAttribute("sun", sun.format(dtf));
		
		model.addAttribute("monEstimatedTime", monEstimatedTime);
		model.addAttribute("tueEstimatedTime", tueEstimatedTime);
		model.addAttribute("wedEstimatedTime", wedEstimatedTime);
		model.addAttribute("thuEstimatedTime", thuEstimatedTime);
		model.addAttribute("friEstimatedTime", friEstimatedTime);
		model.addAttribute("satEstimatedTime", satEstimatedTime);
		model.addAttribute("sunEstimatedTime", sunEstimatedTime);

		return "/app/top";
	}

	//topページ（優先度順）
	@PostMapping("/top/priority")
	public String sortByPriority(Model model) {
		List<Task> task = taskService.selectUndoneTasksByPriority();
		model.addAttribute("task", task);
		
		//週間タスク表示ロジックを記載すること

		return "redirect:/app/top";
	}
	
	//新規タスク登録
	@GetMapping("/new")
	public String displayNew(@ModelAttribute Task task) {
		return "/app/new";
	}
	@PostMapping("/new")
	public String register(@ModelAttribute Task task) {
		taskService.insertOne(task);
		return "redirect:/app/top";
	}

	//edit
	@GetMapping("/edit/{taskID}")
	public String displayEdit(Model model, @PathVariable("taskID") int taskID) {
		model.addAttribute("task", taskService.selectOne(taskID));
		return "/app/edit";
	}
	@PostMapping("/edit/{taskID}")
	public String edit(@ModelAttribute Task task, @PathVariable("taskID") int taskID) {
		taskService.updateOneTask(task);
		return "redirect:/app/top";
	}
	
	//done処理
	@PostMapping("/done/{taskID}")
	public String done(@PathVariable("taskID") int taskID) {
		taskService.done(taskID);
		return "redirect:/app/top";
	}

	//undone処理
	@PostMapping("/undone/{taskID}")
	public String undone(@PathVariable("taskID") int taskID) {
		taskService.undone(taskID);
		return "redirect:/app/done";
	}

	//delete処理
	@PostMapping("/delete/{taskID}")
	public String delete(@PathVariable("taskID") int taskID) {
		taskService.deleteTask(taskID);
		return "redirect:/app/done";
	}



	//today.html表示
	@GetMapping("/today")
	public String displayToday(Model model) {
		List<Task> task = taskService.selectTodayTask();
		model.addAttribute("task", task);
		return "/app/today";
	}

	//done.html表示
	@GetMapping("/done")
	public String displayDone(Model model) {
		List<Task> task = taskService.selectDoneTasks();
		model.addAttribute("task", task);
		return "/app/done";
	}
	
	//GoogleCalendarAPI イベント追加test
	@PostMapping("/test")
	public String test(@AuthenticationPrincipal OAuth2User oauth2User, OAuth2AuthenticationToken authentication) throws GeneralSecurityException, IOException {
		gcapi.addEvent(oauth2User, authentication);
		return "redirect:/app/top";
	}
	
	

}
