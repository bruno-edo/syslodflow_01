package br.ufsc.inf.syslodflow.entity;

import java.io.Serializable;

public class Tool extends BaseModel implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String name;
	private Location location;
	private String uri;
	
	public Tool(String name, String uri) {
		
		this.name = name;
		this.uri = uri;
	}
	
	public Tool(String name, Location location, String uri) {
		
		this.name = name;
		this.location = location;
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

	@Override
	public String getUri() {
		return uri;
	}

	@Override
	public void setUri(String uri) {
		this.uri = uri;
	}

	@Override
	public int hashCode() {
		return (name == null) ? 0 : name.hashCode();
	}
	
	
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj instanceof Tool) {
			return ((Tool)obj).getName().equals(this.name);
		} 
		return false;
	}

	@Override
	public String toString() {
		return "Tool [name=" + name + "]";
	}
	
	

	
	
	
}
