package javadoc_exporter.model;

public class StepAttribute {
	private String name;
	public String getName() {
		return this.name;
	}
	
	private String value;
	public String getValue() {
		return this.value;
	}
	
	public StepAttribute(String name, String value) {
		this.name = name;
		this.value = value;
	}
}
