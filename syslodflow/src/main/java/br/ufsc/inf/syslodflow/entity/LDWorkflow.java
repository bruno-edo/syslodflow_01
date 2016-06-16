package br.ufsc.inf.syslodflow.entity;

import java.util.List;

public class LDWorkflow extends BaseModel {
	
	private String description;
	private String name;
	private Condition preCondition;
	private Condition postCondition;
	private LDWStep firstLdwStep;
	private List<LDWorkflowExecution> ldWorkFlowExecutions;
	private List<LDWStep> ldwSteps;
	private String uri;

	public LDWorkflow(String description, String name, Condition preCondition,
			Condition postCondition, LDWStep firstLdwStep,
			List<LDWorkflowExecution> ldWorkFlowExecutions,
			List<LDWStep> ldwSteps, String uri) {
		
		this.description = description;
		this.name = name;
		this.preCondition = preCondition;
		this.postCondition = postCondition;
		this.firstLdwStep = firstLdwStep;
		this.ldWorkFlowExecutions = ldWorkFlowExecutions;
		this.ldwSteps = ldwSteps;
		this.uri = uri;
	}
	
	public LDWorkflow() {
		preCondition = new Condition();
		postCondition = new Condition();
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
	
	@Override
	public String getUri() {
		return uri;
	}

	@Override
	public void setUri(String uri) {
		this.uri = uri;
	}

	@Override
	public String toString() {
		return "ldWorkflow";
	}
	
	
}