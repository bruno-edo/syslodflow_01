package br.ufsc.inf.syslodflow.entity;

import java.util.Date;

public class LDWorkflowExecution {
	
	private LDWStepExecution firstLdwStepExecution;
	private LDWStepExecution[] ldwStepExecutions;
	private Report report;
	private String description;
	private String name;
	private Date startedDate;
	private Date endedDate;
	
	public LDWorkflowExecution() {
		
	}

	public LDWStepExecution getFirstLdwStepExecution() {
		return firstLdwStepExecution;
	}

	public void setFirstLdwStepExecution(LDWStepExecution firstLdwStepExecution) {
		this.firstLdwStepExecution = firstLdwStepExecution;
	}

	public LDWStepExecution[] getLdwStepExecutions() {
		return ldwStepExecutions;
	}

	public void setLdwStepExecutions(LDWStepExecution[] ldwStepExecutions) {
		this.ldwStepExecutions = ldwStepExecutions;
	}

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
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

