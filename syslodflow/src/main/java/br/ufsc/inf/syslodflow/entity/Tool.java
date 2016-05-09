package br.ufsc.inf.syslodflow.entity;

import java.util.List;

public class Tool extends BaseModel {
	
	private String name;
	private Location location;
	private List<ToolConfiguration> toolConfigurations;
	private String uri;
	
	public Tool(String name, Location location, List<ToolConfiguration> toolConfigurations) {
		
		this.name = name;
		this.location = location;
		this.toolConfigurations = toolConfigurations;
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

	@Override
	public String getUri() {
		return uri;
	}

	@Override
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	
	
	
	
}