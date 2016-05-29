package br.ufsc.inf.syslodflow.entity;

public class Homepage extends BaseModel {
	
	private String name;
	private Location location;
	private String uri;
	
	public Homepage() {
		location = new Location();
	}
	
	public Homepage(String name, Location location, String uri) {
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
	
	
}