package utfpr.spa.it.jira;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import utfpr.spa.Person;
import utfpr.spa.Project;
import utfpr.spa.it.Comment;
import utfpr.spa.it.Issue;
import utfpr.spa.it.IssuePriority;
import utfpr.spa.it.IssueStatus;

public class JiraJson
{
	private Person getPerson(JsonNode node, String path)
	{
		JsonNode personNode = node.path(path);
		if (! personNode.path("author").isNull()) {
			Person person = new Person();
			person.setName(personNode.path("displayName").getTextValue());
			person.setUsername(personNode.path("name").getTextValue());
			person.setEmail(personNode.path("emailAddress").getTextValue());
			return person;
		}
		return null;
	}
	
	public Collection<Comment> getComments(String jsonText) throws Exception
	{
		List<Comment> comments = new ArrayList<Comment>();
		DateTimeFormatter isoDateParser = ISODateTimeFormat.dateTimeParser();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(jsonText);
		JsonNode commentsNode = rootNode.path("comments");
		
		Iterator<JsonNode> commentsNodes = commentsNode.getElements();
		while (commentsNodes.hasNext()) {
			Comment comment = new Comment();
			JsonNode commentNode = commentsNodes.next();
			
			comment.setInternalId(Integer.parseInt(commentNode.path("id").getTextValue()));
			comment.setAuthor(getPerson(commentNode, "author"));
			comment.setBody(commentNode.path("body").getTextValue());
			comment.setCreationDate(isoDateParser.parseDateTime(commentNode.path("created").getTextValue()).toDate());
			
			comments.add(comment);
		}
		
		return comments;
	}
	
	
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
			issue.setReporter(getPerson(fieldsNode, "reporter"));
		
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