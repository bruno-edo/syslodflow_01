package br.ufsc.inf.syslodflow.enumerator;

public enum LangModelEnum {

	RDFXMLABBREV("RDF/XML-ABBREV"),
	RDFXML("RDF/XML");
	
	LangModelEnum(String type)	{
		this.type = type;
	}
	
	private String type;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
