package br.ufsc.inf.syslodflow.entity;

public class Status extends BaseModel {
	
	private String value;
	private String uri;
	
	public Status(String value, String uri) {
		this.value = value;
		this.uri = uri;
	}

	public Status(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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