package br.ufsc.inf.syslodflow.enumerator;

public enum ClassURIEnum {
	LDWPROJECT("http://ldwpo.aksw.org/terms/1.0/LDWProject");
	
	private String uri;
	
	ClassURIEnum(String uri) {
		this.uri = uri;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
	
}
