package br.ufsc.inf.syslodflow.entity;

public class Person extends BaseModel {
	
	private String name;
	private String uri;
	
	public Person(String name, String uri) {
		this.name = name;
		this.uri = uri;
	}
	
	public Person() {
		
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