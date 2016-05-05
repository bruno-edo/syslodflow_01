package br.ufsc.inf.syslodflow.entity;

import java.util.List;

public class LDWStep {
	
	private Dataset inputDataset;
	private Dataset outputDataset;
	private List<LDWStepExecution> ldwStepExecutions;
	private Tool tool;
	private ToolConfiguration toolConfiguration;
	private Task task;
	private String command;
	private String description;
	private String name;


	public LDWStep() {
		
	}

	public Dataset getInputDataset() {
		return inputDataset;
	}

	public void setInputDataset(Dataset inputDataset) {
		this.inputDataset = inputDataset;
	}

	public Dataset getOutputDataset() {
		return outputDataset;
	}

	public void setOutputDataset(Dataset outputDataset) {
		this.outputDataset = outputDataset;
	}

	public List<LDWStepExecution> getLdwStepExecutions() {
		return ldwStepExecutions;
	}

	public void setLdwStepExecutions(List<LDWStepExecution> ldwStepExecutions) {
		this.ldwStepExecutions = ldwStepExecutions;
	}

	public Tool getTool() {
		return tool;
	}

	public void setTool(Tool tool) {
		this.tool = tool;
	}

	public ToolConfiguration getToolConfiguration() {
		return toolConfiguration;
	}

	public void setToolConfiguration(ToolConfiguration toolConfiguration) {
		this.toolConfiguration = toolConfiguration;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}
	
	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
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