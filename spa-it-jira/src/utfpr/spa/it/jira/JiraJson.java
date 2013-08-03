package utfpr.spa.it.jira;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import utfpr.spa.Person;
import utfpr.spa.Project;
import utfpr.spa.it.Issue;
import utfpr.spa.it.IssuePriority;
import utfpr.spa.it.IssueStatus;

public class JiraJson
{

	public Collection<Issue> getIssue(String jsonText) throws Exception
	{
		List<Issue> issues = new ArrayList<Issue>();
		
		DateTimeFormatter isoDateParser = ISODateTimeFormat.dateTimeParser();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(jsonText);
		JsonNode issuesNode = rootNode.path("issues");
		
		Iterator<JsonNode> issueNodes = issuesNode.getElements();
		while (issueNodes.hasNext()) {
			Issue issue = new Issue();
			JsonNode issueNode = issueNodes.next();
			JsonNode fieldsNode = issueNode.path("fields");
			
			issue.setInternalId(Integer.parseInt(issueNode.path("id").getTextValue()));
			issue.setName(issueNode.path("key").getTextValue());
			issue.setCreationDate(isoDateParser.parseDateTime(fieldsNode.path("created").getTextValue()).toDate());
			if (! fieldsNode.path("resolutiondate").isNull()) {
				issue.setResolvedDate(isoDateParser.parseDateTime(issueNode.path("fields/resolutiondate").getTextValue()).toDate());
			}
			issue.setSummary(fieldsNode.path("summary").getTextValue());
            issue.setDescription(fieldsNode.path("description").getTextValue());
			if (! fieldsNode.path("assignee").isNull()) {
				Person assignee = new Person();
				assignee.setName(fieldsNode.path("assignee").getTextValue());
				issue.setAssignee(assignee);
			}
			if (fieldsNode.path("priority").isContainerNode()) {
				JsonNode priorityNode = fieldsNode.path("priority");
				String priorityName = priorityNode.path("name").getTextValue();
				if ("Major".equalsIgnoreCase(priorityName)) {
					issue.setPriority(IssuePriority.HIGH);
				} else {
					System.out.println("Unknown priority");
				}
			}
			if (fieldsNode.path("project").isContainerNode()) {
				JsonNode projectNode = fieldsNode.path("project");
				Project project = new Project();
				project.setName(projectNode.path("name").getTextValue());
				project.setShortName(projectNode.path("key").getTextValue());
				issue.setProject(project);
			}
			if (fieldsNode.path("reporter").isContainerNode()) {
				JsonNode reporterNode = fieldsNode.path("reporter");
				Person reporter = new Person();
				reporter.setName(reporterNode.path("displayName").getTextValue());
				reporter.setUsername(reporterNode.path("name").getTextValue());
				reporter.setEmail(reporterNode.path("emailAddress").getTextValue());
				issue.setReporter(reporter);
			}
			issues.add(issue);
			if (! fieldsNode.path("resolution").isNull()) {
				String resolution = fieldsNode.path("resolution").getTextValue();
				if ("".equalsIgnoreCase(resolution)) {
					issue.setStatus(IssueStatus.RESOLVED);
				} else {
					System.out.println("Unknow resolution: " + resolution);
				}
			} else {
				if (fieldsNode.path("status").isContainerNode()) {
					JsonNode statusNode = fieldsNode.path("status");
					String status = statusNode.path("name").getTextValue();
					if ("Open".equalsIgnoreCase(status)) {
						issue.setStatus(IssueStatus.OPEN);
					} else {
						System.out.println("Unknown status: " + status);
					}
				}
			}
		}
		return issues;
	}
}