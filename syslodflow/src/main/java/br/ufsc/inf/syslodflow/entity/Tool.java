package br.ufsc.inf.syslodflow.entity;

import java.io.Serializable;
import java.util.List;

public class Tool extends BaseModel implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String name;
	private Location location;
	private List<ToolConfiguration> toolConfigurations;
	private String uri;
	
	public Tool() {
		
	}
	
	public Tool(String name, Location location, List<ToolConfiguration> toolConfigurations, String uri) {
		
		this.name = name;
		this.location = location;
		this.toolConfigurations = toolConfigurations;
		this.uri = uri;
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