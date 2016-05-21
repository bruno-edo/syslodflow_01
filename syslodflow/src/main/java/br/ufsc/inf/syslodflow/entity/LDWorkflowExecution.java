package br.ufsc.inf.syslodflow.entity;

import java.util.List;


public class LDWorkflowExecution extends BaseModel {

	private LDWStepExecution firstLdwStepExecution;
	private List<LDWStepExecution> ldwStepExecutions;
	private String description;
	private String name;
	private String startedDate;
	private String endedDate;
	private String uri;
	
	
	public LDWorkflowExecution(LDWStepExecution firstLdwStepExecution,
			List<LDWStepExecution> ldwStepExecutions,
			String description, String name, String startedDate,
			String endedDate, String uri) {
		
		this.firstLdwStepExecution = firstLdwStepExecution;
		this.ldwStepExecutions = ldwStepExecutions;
		this.description = description;
		this.name = name;
		this.startedDate = startedDate;
		this.endedDate = endedDate;
		this.uri = uri;
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
	
	@Override
	public String getUri() {
		return uri;
	}

	@Override
	public void setUri(String uri) {
		this.uri = uri;
	}
}

