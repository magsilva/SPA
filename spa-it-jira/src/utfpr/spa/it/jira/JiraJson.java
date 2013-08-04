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
import utfpr.spa.it.IssueResolution;
import utfpr.spa.it.IssueStatus;

public class JiraJson
{
	private Person getPerson(JsonNode node, String path)
	{
		JsonNode personNode = node.path(path);
		if (! personNode.isNull()) {
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
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(jsonText);
		JsonNode commentsNode = rootNode.path("comments");
		
		return getComments(commentsNode);
	}
	
	public Collection<Comment> getComments(JsonNode commentsNode) throws Exception
	{
		List<Comment> comments = new ArrayList<Comment>();
		DateTimeFormatter isoDateParser = ISODateTimeFormat.dateTimeParser();
		
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
	
	public int countIssues(String jsonText) throws Exception
	{
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(jsonText);
		return rootNode.path("total").getIntValue();
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
				issue.setResolvedDate(isoDateParser.parseDateTime(fieldsNode.path("resolutiondate").getTextValue()).toDate());
			}
			issue.setSummary(fieldsNode.path("summary").getTextValue());
            issue.setDescription(fieldsNode.path("description").getTextValue());
			if (fieldsNode.path("priority").isContainerNode()) {
				JsonNode priorityNode = fieldsNode.path("priority");
				String priorityName = priorityNode.path("name").getTextValue();
				if ("Major".equalsIgnoreCase(priorityName)) {
					issue.setPriority(IssuePriority.HIGH);
				} else if ("Critical".equalsIgnoreCase(priorityName)) {
					issue.setPriority(IssuePriority.CRITICAL);
				} else if ("Blocker".equalsIgnoreCase(priorityName)) {
					issue.setPriority(IssuePriority.BLOCKER);
				} else if ("Minor".equalsIgnoreCase(priorityName)) {
					issue.setPriority(IssuePriority.MINOR);
				} else if ("Trivial".equalsIgnoreCase(priorityName)) {
					issue.setPriority(IssuePriority.TRIVIAL);
				} else {
					System.out.println("Unknown priority: " + priorityName);
				}
			}
			if (fieldsNode.path("project").isContainerNode()) {
				JsonNode projectNode = fieldsNode.path("project");
				Project project = new Project();
				project.setInternalId(Integer.parseInt(projectNode.path("id").getTextValue()));
				project.setName(projectNode.path("name").getTextValue());
				project.setShortName(projectNode.path("key").getTextValue());
				issue.setProject(project);
			}
			issue.setAssignee(getPerson(fieldsNode, "assignee"));
			issue.setReporter(getPerson(fieldsNode, "reporter"));
		
			if (! fieldsNode.path("resolution").isNull()) {
				JsonNode resolutionNode = fieldsNode.path("resolution");
				String resolution = resolutionNode.path("name").getTextValue();
				if ("Fixed".equalsIgnoreCase(resolution)) {
					issue.setResolution(IssueResolution.FIXED);
				} else if ("Duplicate".equalsIgnoreCase(resolution)) {
					issue.setResolution(IssueResolution.DUPLICATE);
				} else if ("Won't Fix".equalsIgnoreCase(resolution)) {
					issue.setResolution(IssueResolution.WONT_FIX);
				} else if ("Invalid".equalsIgnoreCase(resolution)) {
					issue.setResolution(IssueResolution.INVALID);
				} else if ("Cannot Reproduce".equalsIgnoreCase(resolution)) {
					issue.setResolution(IssueResolution.CANNOT_REPRODUCE);
				} else if ("Incomplete".equalsIgnoreCase(resolution)) {
					issue.setResolution(IssueResolution.INCOMPLETE);
				} else if ("Later".equalsIgnoreCase(resolution)) {
					issue.setResolution(IssueResolution.LATER);
				} else if ("Not A  Problem".equalsIgnoreCase(resolution)) {
					issue.setResolution(IssueResolution.NOT_A_PROBLEM);
				} else {
					System.out.println("Unknown resolution: " + resolution);
				}
			}
			
			if (fieldsNode.path("status").isContainerNode()) {
				JsonNode statusNode = fieldsNode.path("status");
				String status = statusNode.path("name").getTextValue();
				if ("Open".equalsIgnoreCase(status)) {
					issue.setStatus(IssueStatus.OPEN);
				} else if ("Closed".equalsIgnoreCase(status)) {
					issue.setStatus(IssueStatus.CLOSED);
				} else if ("Resolved".equalsIgnoreCase(status)) {
					issue.setStatus(IssueStatus.RESOLVED);
				} else if ("Reopened".equalsIgnoreCase(status)) {
					issue.setStatus(IssueStatus.REOPENED);
				} else if ("Patch Available".equalsIgnoreCase(status)) {
					issue.setStatus(IssueStatus.PATCH_AVAILABLE);
				} else if ("In Progress".equalsIgnoreCase(status)) {
					issue.setStatus(IssueStatus.IN_PROGRESS);
				} else {
					System.out.println("Unknown status: " + status);
				}
			}
			

			if (fieldsNode.path("comment") != null) {
				JsonNode commentNode = fieldsNode.path("comment");
				JsonNode commentsNode = commentNode.path("comments");
				Collection<Comment> comments = getComments(commentsNode);
				int commentCount = commentNode.path("total").getIntValue();
				if (commentCount != comments.size()) {
					System.out.println("Ops, missing some comments for issue " + issue.getName() + " (#" + issue.getInternalId()   +")"
							+ " - We were expecting " + commentCount + ", but found only " + comments.size());
				}
				for (Comment comment : comments) {
					issue.addComment(comment);
				}
			}
			
			issues.add(issue);

		}
			
		return issues;
	}
}