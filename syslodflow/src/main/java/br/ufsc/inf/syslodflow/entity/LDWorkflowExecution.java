package br.ufsc.inf.syslodflow.entity;

import java.util.List;


public class LDWorkflowExecution {
	
	private LDWStepExecution firstLdwStepExecution;
	private List<LDWStepExecution> ldwStepExecutions;
	private Report report;
	private String description;
	private String name;
	private String startedDate;
	private String endedDate;
	
	public LDWorkflowExecution() {
		
	}

	public LDWStepExecution getFirstLdwStepExecution() {
		return firstLdwStepExecution;
	}

	public void setFirstLdwStepExecution(LDWStepExecution firstLdwStepExecution) {
		this.firstLdwStepExecution = firstLdwStepExecution;
	}

	public List<LDWStepExecution> getLdwStepExecutions() {
		return ldwStepExecutions;
	}

	public void setLdwStepExecutions(List<LDWStepExecution> ldwStepExecutions) {
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

	public String getStartedDate() {
		return startedDate;
	}

	public void setStartedDate(String startedDate) {
		this.startedDate = startedDate;
	}

	public String getEndedDate() {
		return endedDate;
	}

	public void setEndedDate(String endedDate) {
		this.endedDate = endedDate;
	}	
}

