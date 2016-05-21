package br.ufsc.inf.syslodflow.entity;

public class Task extends BaseModel {
	
	private String name;
	private String description;
	private String uri;

	public Task(String name, String description, String uri) {
		this.name = name;
		this.description = description;
		this.uri = uri;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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