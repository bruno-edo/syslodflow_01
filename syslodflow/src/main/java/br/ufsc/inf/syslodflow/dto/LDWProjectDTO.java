package br.ufsc.inf.syslodflow.dto;

import java.io.Serializable;
import java.nio.file.Path;

/**
 * DTO for class LDWProject
 * @author Jhonatan
 * 
 */
public class LDWProjectDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String name;
	private String creator;
	private String fileName;
	private Path path;
	private String uri;
	
	public LDWProjectDTO() {
		
	}

	public LDWProjectDTO(String name, String creator, String uri) {
		this.name = name;
		this.creator = creator;
		this.uri = uri;
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uri == null) ? 0 : uri.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LDWProjectDTO other = (LDWProjectDTO) obj;
		if (uri == null) {
			if (other.uri != null)
				return false;
		} else if (!uri.equals(other.uri))
			return false;
		return true;
	}



}
