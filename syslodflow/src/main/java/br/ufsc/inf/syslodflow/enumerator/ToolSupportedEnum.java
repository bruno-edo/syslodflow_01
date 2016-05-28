package br.ufsc.inf.syslodflow.enumerator;

public enum ToolSupportedEnum {
	SPARQLIFY("Sparqlify"),
	VIRTUOSO("Virtuoso RDF Store"),
	MYSQL("MySQL"),
	LIMES("LIMES");
	
	private String name;
	
	ToolSupportedEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}
