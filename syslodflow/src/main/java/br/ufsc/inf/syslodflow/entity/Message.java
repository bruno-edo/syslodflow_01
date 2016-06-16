package br.ufsc.inf.syslodflow.entity;

public class Message extends BaseModel {
	
	private String value;
	private String uri;
	
	public Message(String value, String uri) {
		this.value = value;
		this.uri = uri;
	}

	public Message(String value) {
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

	@Override
	public String toString() {
		return "message";
	}
	
	
	
	
}