package utfpr.spa.it.jira;

import java.io.FileWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

import utfpr.spa.it.Issue;

import com.ironiacorp.http.HttpJob;
import com.ironiacorp.http.HttpJobRunner;
import com.ironiacorp.http.HttpMethod;
import com.ironiacorp.http.HttpMethodResult;
import com.ironiacorp.http.impl.httpclient3.HttpJobRunnerHttpClient3;
import com.ironiacorp.http.impl.httpclient4.HttpJobRunnerHttpClient4;

// https://docs.atlassian.com/jira/REST/latest/
public class RestJiraMiner
{
	private String jiraURI;
	
	private String projectShortname;
	
	private Collection<Issue> issues;
	
	private String backupDirname;
	
	public String getJiraURI() {
		return jiraURI;
	}

	public void setJiraURI(String jiraURI) {
		this.jiraURI = jiraURI;
	}


	public String getProjectShortname() {
		return projectShortname;
	}

	public void setProjectShortname(String projectShortname) {
		this.projectShortname = projectShortname;
	}
	
	public RestJiraMiner()
	{
		setIssues(new ArrayList<Issue>());
	}
	
	public void collect() throws Exception
	{
		int start, max, total, count;
		HttpJob job;
		JiraJson jj = new JiraJson();
		FileWriter fw;
		
		start = 600;
		max = 50;
		count = start;
		total = Integer.MAX_VALUE;
		do {
			HttpJobRunner runner = new HttpJobRunnerHttpClient3();
			HttpMethodResult result;
			Collection<Issue> batch;
			String searchUrl;
			String page;
			String filename;
			
			searchUrl = jiraURI + "/search?"
					+ "jql=project=" + projectShortname + "+ORDER+BY+key+ASC&"
					+ "startAt=" + start
					+ "&maxResults=" + max
					+ "&fields=id,key,created,resolutiondate,summary,description,priority,project,assignee,reporter,resolution,status,comment";
			job = new HttpJob(HttpMethod.GET, new URI(searchUrl));
			runner.addJob(job);
			System.out.print("Running search using " + searchUrl + "...");
			runner.run();
			result = job.getResult();
			if (result != null ) {
				byte[] rawData = result.getContentAsByteArray();
				if (rawData != null) {
					page = new String(rawData);
					System.out.println("Ok");
					
					System.out.println("Parsing issues...");
					total = jj.countIssues(page);
					batch = jj.getIssue(page);
					count += batch.size();
					System.out.println("Ok. Got " + batch.size() + " (" + (total - count) + " to go!)");
					
					filename = projectShortname + "-" + start + "-" + (count - 1) + ".json";
					System.out.print("Saving data to " + backupDirname + filename + "...");
					fw = new FileWriter(backupDirname + filename);
					fw.append(page);
					fw.flush();
					fw.close();
					System.out.println("Ok");
					
					start += batch.size();
					issues.addAll(batch);					
				}
			}

			Thread.sleep(120 * 1000);
		} while (count < total);
	}

	public Collection<Issue> getIssues() {
		return issues;
	}

	public void setIssues(Collection<Issue> issues) {
		this.issues = issues;
	}

	public String getBackupDirname() {
		return backupDirname;
	}

	public void setBackupDirname(String backupDirname) {
		this.backupDirname = backupDirname;
	}
	
	public static void main(String[] args) throws Exception
	{
		RestJiraMiner miner = new RestJiraMiner();
		miner.setBackupDirname("/home/magsilva/");
		miner.setJiraURI("https://issues.apache.org/jira/rest/api/2");
		miner.setProjectShortname("HADOOP");
		miner.collect();
		for (Issue issue : miner.getIssues()) {
			System.out.println(issue);
		}
	}

}
