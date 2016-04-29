package br.ufsc.inf.syslodflow.enumerator;

public enum PropertyURIEnum {
	NAME("http://ldwpo.aksw.org/terms/1.0/name"),
	CREATOR("http://ldwpo.aksw.org/terms/1.0/creator");
	
	private String uri;
	
	PropertyURIEnum(String uri) {
		this.uri = uri;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	

}
