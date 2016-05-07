package br.ufsc.inf.syslodflow.entity;

public class ToolConfiguration {
	
	private String name;
	private Location location;
	
	public ToolConfiguration() {
		
	}

	public ToolConfiguration(String name, Location location) {
		this.name = name;
		this.location = location;
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
	
	
}