package br.ufsc.inf.syslodflow.entity;

public class License extends BaseModel {
	
	private String name;
	private String uri;

	public License(String name, String uri) {
		this.name = name;
		this.uri = uri;
	}
	
	public License(String uri) {
		this.uri = uri;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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