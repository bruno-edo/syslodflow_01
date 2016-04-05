package br.ufsc.inf.syslodflow.entity;

import java.util.List;

public class LDWorkflow {
	
	private LDWStep firstLdwStep;
	private List<LDWorkflowExecution> ldWorkFlowExecutions;
	private List<LDWStep> ldwSteps;
	private Condition preCondition;
	private Condition postCondition;
	private String description;
	private String name;
	
	public LDWorkflow() {
		
	}

	public LDWStep getFirstLdwStep() {
		return firstLdwStep;
	}

	public void setFirstLdwStep(LDWStep firstLdwStep) {
		this.firstLdwStep = firstLdwStep;
	}

	public List<LDWorkflowExecution> getLdWorkFlowExecutions() {
		return ldWorkFlowExecutions;
	}

	public void setLdWorkFlowExecutions(
			List<LDWorkflowExecution> ldWorkFlowExecutions) {
		this.ldWorkFlowExecutions = ldWorkFlowExecutions;
	}

	public List<LDWStep> getLdwSteps() {
		return ldwSteps;
	}

	public void setLdwSteps(List<LDWStep> ldwSteps) {
		this.ldwSteps = ldwSteps;
	}

	public Condition getPreCondition() {
		return preCondition;
	}

	public void setPreCondition(Condition preCondition) {
		this.preCondition = preCondition;
	}

	public Condition getPostCondition() {
		return postCondition;
	}

	public void setPostCondition(Condition postCondition) {
		this.postCondition = postCondition;
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
	
	
}