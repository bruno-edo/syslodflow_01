package br.ufsc.inf.syslodflow.entity;

public class Dataset extends BaseModel {
	
	private Format format;
	private License license;
	private Location location;
	private String name;
	private String uri;

	
	public Dataset(String name, Format format, License license,
			Location location, String uri) {
		this.name = name;
		this.format = format;
		this.location = location;
		this.license = license;
		this.uri = uri;
	}
	
	public Dataset() {
		
	}
	
	public Dataset(String uri) {
		this.uri = uri;
	}

	public Format getFormat() {
		return format;
	}
	public void setFormat(Format format) {
		this.format = format;
	}
	public License getLicense() {
		return license;
	}
	public void setLicense(License license) {
		this.license = license;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
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

	@Override
	public String toString() {
		return "dataset";
	}
	

	
	
}