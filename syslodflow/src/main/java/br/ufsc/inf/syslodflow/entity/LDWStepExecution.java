package br.ufsc.inf.syslodflow.entity;

import java.util.Date;

public class LDWStepExecution {
	
	private Status status;
	private Message message;
	private LDWStepExecution nextStep;
	private LDWStepExecution previoustStep;
	private Person contributor;
	private String description;
	private String name;
	private Date startedDate;
	private Date endedDate;
	
	public LDWStepExecution() {
		
	}
	
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
	public LDWStepExecution getNextStep() {
		return nextStep;
	}
	public void setNextStep(LDWStepExecution nextStep) {
		this.nextStep = nextStep;
	}
	public LDWStepExecution getPrevioustStep() {
		return previoustStep;
	}
	public void setPrevioustStep(LDWStepExecution previoustStep) {
		this.previoustStep = previoustStep;
	}
	public Person getContributor() {
		return contributor;
	}
	public void setContributor(Person contributor) {
		this.contributor = contributor;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getStartedDate() {
		return startedDate;
	}
	public void setStartedDate(Date startedDate) {
		this.startedDate = startedDate;
	}
	public Date getEndedDate() {
		return endedDate;
	}
	public void setEndedDate(Date endedDate) {
		this.endedDate = endedDate;
	}
	
	
	
}