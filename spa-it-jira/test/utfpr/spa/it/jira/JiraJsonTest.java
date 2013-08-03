package utfpr.spa.it.jira;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import utfpr.spa.Person;
import utfpr.spa.it.Issue;
import utfpr.spa.it.IssuePriority;

import com.ironiacorp.io.IoUtil;

public class JiraJsonTest
{
	private JiraJson jj;
	
	@Before
	public void setUp() throws Exception {
		jj = new JiraJson();
	}

	@Test
	public void testSimpleExample() throws Exception
	{
		InputStream is = JiraJsonTest.class.getClassLoader().getResourceAsStream("utfpr/spa/it/jira/single-issue.json");
		byte[] rawData = IoUtil.toByteArray(is);
		String jsonText = new String(rawData);
		Collection<Issue> issues = jj.getIssue(jsonText);
		
		Person reporter = new Person();
		reporter.setName("Forest");
		reporter.setUsername("forest");
		reporter.setEmail("apache@tibit.com");
		
		Person assignee = null;
					
		GregorianCalendar date = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		date.set(2010, 4, 27, 22, 23, 16);
		
		assertEquals(1, issues.size());
		Issue issue = issues.iterator().next();
		assertEquals(12465606, issue.getInternalId());
		assertEquals("MODPYTHON-257", issue.getName());
		assertEquals(date.getTime().toString(), issue.getCreationDate().toString()); // Comparing dates directly fails, despite having the same data
		assertEquals("md5 module is deprecated; use hashlib instead", issue.getSummary());
		assertEquals("mod_python is spewing a lot of log spam:\r\n\r\n[Thu May 27 15:08:00 2010] [error] /usr/lib/python2.6/dist-packages/mod_python/importer.py:32: DeprecationWarning: the md5 module is deprecated; use hashlib instead", issue.getDescription());
		assertEquals(reporter, issue.getReporter());
		assertEquals(assignee, issue.getAssignee());
		assertEquals(IssuePriority.HIGH, issue.getPriority());
	}

}
