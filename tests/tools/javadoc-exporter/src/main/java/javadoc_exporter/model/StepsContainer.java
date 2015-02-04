package javadoc_exporter.model;

import java.util.ArrayList;
import java.util.List;

public class StepsContainer {
	private String name;

	public String getName() {
		return this.name;
	}

	private String description;

	public String getDescription() {
		return this.description;
	}

	private String pkgName;

	public String getPkgName() {
		return this.pkgName;
	}

	private List<Step> steps = new ArrayList<Step>();

	public List<Step> getSteps() {
		return new ArrayList<Step>(this.steps);
	}

	public StepsContainer(String name, String description, String pkgName,
			List<Step> steps) {
		this.name = name;
		this.description = description;
		this.pkgName = pkgName;
		this.steps = new ArrayList<Step>(steps);
		for (Step step : this.steps) {
			step.setOwner(this);
		}
	}
}
