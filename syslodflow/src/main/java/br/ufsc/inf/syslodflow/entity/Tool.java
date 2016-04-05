package br.ufsc.inf.syslodflow.entity;

import java.util.List;

public class Tool {
	
	private String name;
	private Location location;
	private List<ToolConfiguration> toolConfigurations;
	
	public Tool() {
	
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public List<ToolConfiguration> getToolConfigurations() {
		return toolConfigurations;
	}

	public void setToolConfigurations(List<ToolConfiguration> toolConfigurations) {
		this.toolConfigurations = toolConfigurations;
	}
	
	
	
}