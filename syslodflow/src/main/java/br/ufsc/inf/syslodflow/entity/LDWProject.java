package br.ufsc.inf.syslodflow.entity;

import java.util.List;

public class LDWProject {
	
	private Person creator;
	private Homepage homePage;
	private List<LDWorkflow> ldWorkFlows;
	private Report report;
	private String goal;
	private String description;
	private String name;
	
	public LDWProject() {
		
	}

	public Person getCreator() {
		return creator;
	}

	public void setCreator(Person creator) {
		this.creator = creator;
	}

	public Homepage getHomePage() {
		return homePage;
	}

	public void setHomePage(Homepage homePage) {
		this.homePage = homePage;
	}

	public List<LDWorkflow> getLdWorkFlows() {
		return ldWorkFlows;
	}

	public void setLdWorkFlows(List<LDWorkflow> ldWorkFlows) {
		this.ldWorkFlows = ldWorkFlows;
	}

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}

	public String getGoal() {
		return goal;
	}

	public void setGoal(String goal) {
		this.goal = goal;
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