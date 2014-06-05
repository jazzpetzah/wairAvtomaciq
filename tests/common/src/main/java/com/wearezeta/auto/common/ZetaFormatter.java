package com.wearezeta.auto.common;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;

import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.Background;
import gherkin.formatter.model.Examples;
import gherkin.formatter.model.Feature;
import gherkin.formatter.model.Match;
import gherkin.formatter.model.Result;
import gherkin.formatter.model.Scenario;
import gherkin.formatter.model.ScenarioOutline;
import gherkin.formatter.model.Step;

public class ZetaFormatter implements Formatter, Reporter {

	@Override
	public void background(Background arg0) {
		System.out.println(arg0.getName());
		
	}

	@Override
	public void close() {
		System.out.println("close");
		
	}

	@Override
	public void done() {
		System.out.println("done");
		
	}

	@Override
	public void eof() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void examples(Examples arg0) {
		System.out.println(arg0.getName());
		
	}

	@Override
	public void feature(Feature arg0) {
		System.out.println(arg0.getName());
		
	}

	@Override
	public void scenario(Scenario arg0) {
		System.out.println(arg0.getName());
		
	}

	@Override
	public void scenarioOutline(ScenarioOutline arg0) {
		System.out.println(arg0.getName());
		
	}

	@Override
	public void step(Step arg0) {
		System.out.println(arg0.getName());
		
	}

	@Override
	public void syntaxError(String arg0, String arg1, List<String> arg2,
			String arg3, Integer arg4) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void uri(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void after(Match arg0, Result arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void before(Match arg0, Result arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void embedding(String arg0, byte[] arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void match(Match arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void result(Result arg0) {
		System.out.println(arg0.getStatus());
		
		/*final JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();;
        URI jiraServerUri = null;
		try {
			jiraServerUri = new URI("https://wearezeta.atlassian.net/");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
        final JiraRestClient restClient = factory.createWithBasicHttpAuthentication(jiraServerUri, "sergeii.khyzhniak", "zetaJiraPassword1");
        
       
        //final Promise<Issue> pIssue = restClient.getIssueClient().getIssue("QATEST-3");
        
        Issue issue = null;

        final IssueRestClient client = restClient.getIssueClient();
        
        issue = client.getIssue("QATEST-3").claim();
        
        System.out.println(issue.getDescription());
        
        try {
			restClient.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	
	}

	@Override
	public void write(String arg0) {
		// TODO Auto-generated method stub
		
	}

}
