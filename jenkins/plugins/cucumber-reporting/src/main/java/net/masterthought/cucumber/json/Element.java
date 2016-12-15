package net.masterthought.cucumber.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.sun.org.apache.xerces.internal.xs.StringList;
import net.masterthought.cucumber.ConfigurationOptions;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.util.Util;
import org.apache.commons.lang.StringUtils;

import static com.googlecode.totallylazy.Option.option;

public class Element {

    private String name;
    private String description;
    private String keyword;
    private Step[] steps;
    private Tag[] tags;


    public Element() {
    }

    public List<String> getStepsList() {
        List<String> stepsList = new ArrayList<String>();
        for (Step step:steps) {
            stepsList.add(step.getRawName());
        }
        return stepsList;
    }

    public Sequence<Step> getSteps() {
        return getSteps(false);
    }

    public void setSteps(Sequence<Step> steps) { steps.toArray(this.steps); }

	public Sequence<Step> getSteps(Boolean update) {
        if (update) {
            LinkedHashMap<String, Integer> stepNames = new LinkedHashMap<String, Integer>();
            for (Step step : steps) {
                String stepName = step.getRawName();
                Integer index = step.getIndex();
                if (index == null) {
                    index = 1;
                }
                step.setName(stepName + " " + index);
            }
        }
        return Sequences.sequence(option(steps).getOrElse(new Step[]{})).realise();
    }

    public Sequence<Tag> getTags() {
        return Sequences.sequence(option(tags).getOrElse(new Tag[]{})).realise();
    }

    public Util.Status getStatus() {
        //Set scenario status here
        int failedSteps = getSteps().filter(Step.predicates.hasStatus(Util.Status.FAILED)).size();
        int skippedSteps = getSteps().filter(Step.predicates.hasStatus(Util.Status.SKIPPED)).size();
        int pendingSteps = getSteps().filter(Step.predicates.hasStatus(Util.Status.PENDING)).size();
        int undefinedSteps = getSteps().filter(Step.predicates.hasStatus(Util.Status.UNDEFINED)).size();
        int totalSteps = getSteps().number().intValue();

        if (ConfigurationOptions.skippedFailsBuild()) {
            failedSteps += skippedSteps;
        }

        if (!ConfigurationOptions.skippedFailsBuild() && ConfigurationOptions.allSkippedFailsBuild()
                && (skippedSteps == totalSteps)) {
            failedSteps += skippedSteps;
        }

        if (ConfigurationOptions.pendingFailsBuild()) {
            failedSteps += pendingSteps;
        }

        if (ConfigurationOptions.undefinedFailsBuild()) {
            failedSteps += undefinedSteps;
        }
        return failedSteps == 0 ? Util.Status.PASSED : Util.Status.FAILED;
    }

    public String getRawName() {
        return name;
    }

    public String getDescription() {
        String result = "";
        if (Util.itemExists(description)) {
            String content = description.replaceAll("\n", "<br/>");
            result = "<span class=\"step-keyword\">Runner </span><span class=\"scenario-description\">" + content  +
                    "</span>";
        }
        return Util.itemExists(description) ?  Util.result(getStatus()) + result + Util.closeDiv() : "";
    }

    public String getKeyword() {
        return keyword;
    }

    public String getName() {
        List<String> contentString = new ArrayList<String>();

        if (Util.itemExists(keyword)) {
            contentString.add("<span class=\"scenario-keyword\">" + keyword + ": </span>");
        }

        if (Util.itemExists(name)) {
            contentString.add("<span class=\"scenario-name\">" + name + "</span>");
        }

        return Util.itemExists(contentString) ? Util.result(getStatus()) + StringUtils.join(contentString.toArray(), " ") + Util.closeDiv() : "";
    }

    public Sequence<String> getTagList() {
        return processTags();
    }

    public boolean hasTags() {
        return Util.itemExists(tags);
    }

    private Sequence<String> processTags() {
        return getTags().map(Tag.functions.getName());
    }

    public String getTagsList() {
        String result = "<div class=\"feature-tags\"></div>";
        if (Util.itemExists(tags)) {
            String tagList = StringUtils.join(processTags().toList().toArray(), ",");
            result = "<div class=\"feature-tags\">" + tagList + "</div>";
        }
        return result;
    }

    public static class functions {
        public static Function1<Element, Util.Status> status() {
            return new Function1<Element, Util.Status>() {
                @Override
                public Util.Status call(Element element) throws Exception {
                    return element.getStatus();
                }
            };
        }
    }
}
