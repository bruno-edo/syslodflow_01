package br.ufsc.inf.syslodflow.enumerator;

public enum NSURIEnum {

	NS("http://ldwpo.aksw.org/terms/1.0/");
	
	private String uri;
	
	NSURIEnum(String uri) {
		this.uri = uri;
	}

	public String getUri() {
		return uri;
	}

}
