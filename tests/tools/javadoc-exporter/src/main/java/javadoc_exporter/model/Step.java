package javadoc_exporter.model;

import java.util.ArrayList;
import java.util.List;

public class Step {
	private String name;

	public String getName() {
		return name;
	}

	private String methodName;

	public String getMethodName() {
		return methodName;
	}
	
	private String methodSignature;
	
	public String getMethodSignature() {
		return this.methodSignature;
	}

	private List<StepAttribute> throwsList = new ArrayList<StepAttribute>();

	public List<StepAttribute> getThrowsList() {
		return new ArrayList<StepAttribute>(throwsList);
	}

	private String description;

	public String getDescription() {
		return description;
	}

	private List<StepAttribute> paramsList = new ArrayList<StepAttribute>();

	public List<StepAttribute> getParamsList() {
		return new ArrayList<StepAttribute>(paramsList);
	}

	private StepsContainer owner;

	protected void setOwner(StepsContainer owner) {
		this.owner = owner;
	}

	public StepsContainer getOwner() {
		return this.owner;
	}

	public Step(String name, String methodName, String methodSignature, String description,
			List<StepAttribute> throwsList, List<StepAttribute> paramsList) {
		this.name = name;
		this.methodName = methodName;
		this.methodSignature = methodSignature;
		this.description = description;
		this.throwsList = new ArrayList<StepAttribute>(throwsList);
		this.paramsList = new ArrayList<StepAttribute>(paramsList);
	}
}
