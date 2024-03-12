package org.mailRead.MailReaderModel;

import java.util.Date;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Component
@Entity
@Scope("protocol")
@Table(name = "maildata")
public class EmailData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String usermail;
	private String subject;
	private String tasksDone;
	private String projectName;
	private String taskToDo;
	private String taskOnHold;
	private Date date;

	public EmailData() {
		super();
	}

	public EmailData(Long id, String usermail, String subject, String tasksDone, String taskToDo, String taskOnHold,
			Date date, String projectName) {
		super();
		this.id = id;
		this.usermail = usermail;
		this.subject = subject;
		this.tasksDone = tasksDone;
		this.taskToDo = taskToDo;
		this.taskOnHold = taskOnHold;
		this.date = date;
		this.projectName = projectName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsermail() {
		return usermail;
	}

	public void setUsermail(String usermail) {
		this.usermail = usermail;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTasksDone() {
		return tasksDone;
	}

	public void setTasksDone(String tasksDone) {
		this.tasksDone = tasksDone;
	}

	public String getTaskToDo() {
		return taskToDo;
	}

	public void setTaskToDo(String taskToDo) {
		this.taskToDo = taskToDo;
	}

	public String getTaskOnHold() {
		return taskOnHold;
	}

	public void setTaskOnHold(String taskOnHold) {
		this.taskOnHold = taskOnHold;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	

}
