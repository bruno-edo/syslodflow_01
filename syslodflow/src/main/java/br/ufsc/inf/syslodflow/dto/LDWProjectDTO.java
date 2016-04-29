package br.ufsc.inf.syslodflow.dto;

public class LDWProjectDTO {
	
	private String name;
	private String creator;
	
	public LDWProjectDTO() {
		
	}

	public LDWProjectDTO(String name, String creator) {
		this.name = name;
		this.creator = creator;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	

}
