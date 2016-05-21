package br.ufsc.inf.syslodflow.entity;

public class Condition extends BaseModel {
	
	private String description;
	private String uri;
	
	public Condition(String description, String uri) {
		this.description = description;
		this.uri = uri;
		
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