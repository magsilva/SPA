package utfpr.spa.it.jira;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import utfpr.spa.Person;
import utfpr.spa.Project;
import utfpr.spa.it.Comment;
import utfpr.spa.it.Issue;
import utfpr.spa.it.IssuePriority;
import utfpr.spa.it.IssueStatus;

import com.ironiacorp.io.IoUtil;

public class JiraJsonTest
{
	private JiraJson jj;
	
	@Before
	public void setUp() throws Exception {
		jj = new JiraJson();
	}

	@Test
	public void testOneIssue() throws Exception
	{
		InputStream is = JiraJsonTest.class.getClassLoader().getResourceAsStream("utfpr/spa/it/jira/single-issue.json");
		byte[] rawData = IoUtil.toByteArray(is);
		String jsonText = new String(rawData);
		Collection<Issue> issues = jj.getIssue(jsonText);
		
		Person reporter = new Person();
		Person assignee = null;
		Project project = new Project();
		project.setName("mod_python");
		project.setShortName("MODPYTHON");
		GregorianCalendar date = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		
		assertEquals(1, issues.size());
		Issue issue = issues.iterator().next();
		assertEquals(12465606, issue.getInternalId());
		assertEquals("MODPYTHON-257", issue.getName());
		date.set(2010, 4, 27, 22, 23, 16);
		assertEquals(date.getTime().toString(), issue.getCreationDate().toString()); // Comparing dates directly fails, despite having the same data
		assertEquals("md5 module is deprecated; use hashlib instead", issue.getSummary());
		assertEquals("mod_python is spewing a lot of log spam:\r\n\r\n[Thu May 27 15:08:00 2010] [error] /usr/lib/python2.6/dist-packages/mod_python/importer.py:32: DeprecationWarning: the md5 module is deprecated; use hashlib instead", issue.getDescription());
		reporter.setName("Forest");
		reporter.setUsername("forest");
		reporter.setEmail("apache@tibit.com");
		assertEquals(reporter, issue.getReporter());
		assertEquals(assignee, issue.getAssignee());
		assertEquals(IssuePriority.HIGH, issue.getPriority());
		assertEquals(project, issue.getProject());
		assertEquals(IssueStatus.OPEN, issue.getStatus());
	}

	@Test
	public void testTwoIssues() throws Exception
	{
		InputStream is = JiraJsonTest.class.getClassLoader().getResourceAsStream("utfpr/spa/it/jira/two-issues.json");
		byte[] rawData = IoUtil.toByteArray(is);
		String jsonText = new String(rawData);
		Collection<Issue> issues = jj.getIssue(jsonText);
		
		Person reporter = new Person();
		Person assignee = null;			
		GregorianCalendar date = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		Project project = new Project();
		project.setName("mod_python");
		project.setShortName("MODPYTHON");
		
		assertEquals(2, issues.size());
		Iterator<Issue> i = issues.iterator();

		Issue issue = i.next();
		assertEquals(12465606, issue.getInternalId());
		assertEquals("MODPYTHON-257", issue.getName());
		date.set(2010, 4, 27, 22, 23, 16);
		assertEquals(date.getTime().toString(), issue.getCreationDate().toString()); // Comparing dates directly fails, despite having the same data
		assertEquals("md5 module is deprecated; use hashlib instead", issue.getSummary());
		assertEquals("mod_python is spewing a lot of log spam:\r\n\r\n[Thu May 27 15:08:00 2010] [error] /usr/lib/python2.6/dist-packages/mod_python/importer.py:32: DeprecationWarning: the md5 module is deprecated; use hashlib instead", issue.getDescription());
		reporter.setName("Forest");
		reporter.setUsername("forest");
		reporter.setEmail("apache@tibit.com");
		assertEquals(reporter, issue.getReporter());
		assertEquals(assignee, issue.getAssignee());
		assertEquals(IssuePriority.HIGH, issue.getPriority());
		assertEquals(IssueStatus.OPEN, issue.getStatus());
		assertEquals(project, issue.getProject());
		
		issue = i.next();
		assertEquals(12356313, issue.getInternalId());
		assertEquals("MODPYTHON-208", issue.getName());
		date.set(2006, 10, 21, 23, 55, 21);
		assertEquals(date.getTime().toString(), issue.getCreationDate().toString()); // Comparing dates directly fails, despite having the same data
		assertEquals("Automatic construction of handler class.", issue.getSummary());
		assertEquals("When defining a mod_python handler, it is possible to supply a dotted path for the actual handler function to be used. Ie:\n\n  PythonHandler module::instance.function\n\nwhen determing the handler to execute, it will use the dotted path to traverse any object hierarchy. Thus the above will execute \"function()\" of the object instance called \"instance\" in the module called \"module\".\n\nIf instead one provides:\n\n  PythonHandler module::class.function\n\nwhere 'class' is the name of a class type, then referencing 'function' within that type will result in an instance of the class automatically being created just for the current request, with \"function()' then being called on that transient instance of the class.\n\nFor an instance of the class to be created in this way, one must access a member function of the class type. If an instance of the class is callable in its own right, ie., has a __call__() method, it is not however possible to say:\n\n  PythonHandler module::class\n\nTo get that to work, you instead have to use:\n\n  PythonHandler module::class.__call__\n\nFirst change should be that if a class is callable through having a __call__() method, then it should not be necessary to reference the __call__() method explicitly, instead, referencing the class type itself should be enough. Ie., using\n\n  PythonHandler module::class\n\nshould work.\n\nNote that the code will have to be smart enough to handle both new and old style classes.\n\nThe next issue with this automatic initialisation of a class type is that although the __call__() method needs to accept the 'req' argument, the constructor has to as well if it is being created automatically. The code should allow for the case where the class doesn't want to have to deal with the 'req' object as an argument to the constructor. Ie., it should be optional for the constructor, although always still required for the actual function of the class instance being called.", issue.getDescription());
		reporter.setName("Graham Dumpleton");
		reporter.setUsername("grahamd");
		reporter.setEmail("Graham.Dumpleton@gmail.com");
		assertEquals(reporter, issue.getReporter());
		assertEquals(assignee, issue.getAssignee());
		assertEquals(IssuePriority.HIGH, issue.getPriority());
		assertEquals(IssueStatus.OPEN, issue.getStatus());
		assertEquals(project, issue.getProject());
	}
	
	@Test
	public void testOneComment() throws Exception
	{
		InputStream is = JiraJsonTest.class.getClassLoader().getResourceAsStream("utfpr/spa/it/jira/comment-HADOOP-9761.json");
		byte[] rawData = IoUtil.toByteArray(is);
		String jsonText = new String(rawData);
		Collection<Comment> comments = jj.getComments(jsonText);
		
		Person author = new Person();
		GregorianCalendar date = new GregorianCalendar(TimeZone.getTimeZone("UTC"));

		assertEquals(1, comments.size());

		Iterator<Comment> i = comments.iterator();

		Comment comment = i.next();
		assertEquals(13715898, comment.getInternalId());
		date.set(2013, 6, 23, 00, 15, 23);
		assertEquals(date.getTime().toString(), comment.getCreationDate().toString()); // Comparing dates directly fails, despite having the same data
		assertEquals("I think the fix is first qualifying paths before they are passed to the underlying FileSystem. I'm a bit surprised VFS doesn't do this already, but I guess it's worked so far since DFS (except for rename) just assumes it's the default FS when given an unqualified path.\n\nI think I'll qualify just for VFS#rename, but it could conceivably be done for all VFS calls.", comment.getBody());
		author.setName("Andrew Wang");
		author.setUsername("andrew.wang");
		author.setEmail("andrew.wang@cloudera.com");
		assertEquals(author, comment.getAutor());
	}
	
	@Test
	public void testComments() throws Exception
	{
		InputStream is = JiraJsonTest.class.getClassLoader().getResourceAsStream("utfpr/spa/it/jira/comments-HADOOP-9761.json");
		byte[] rawData = IoUtil.toByteArray(is);
		String jsonText = new String(rawData);
		Collection<Comment> comments = jj.getComments(jsonText);
		
		Person author = new Person();
		GregorianCalendar date = new GregorianCalendar(TimeZone.getTimeZone("UTC"));

		assertEquals(27, comments.size());

		Iterator<Comment> i = comments.iterator();

		Comment comment = i.next();
		assertEquals(13715898, comment.getInternalId());
		date.set(2013, 6, 23, 00, 15, 23);
		assertEquals(date.getTime().toString(), comment.getCreationDate().toString()); // Comparing dates directly fails, despite having the same data
		assertEquals("I think the fix is first qualifying paths before they are passed to the underlying FileSystem. I'm a bit surprised VFS doesn't do this already, but I guess it's worked so far since DFS (except for rename) just assumes it's the default FS when given an unqualified path.\n\nI think I'll qualify just for VFS#rename, but it could conceivably be done for all VFS calls.", comment.getBody());
		author.setName("Andrew Wang");
		author.setUsername("andrew.wang");
		author.setEmail("andrew.wang@cloudera.com");
		assertEquals(author, comment.getAutor());
		
		while (i.hasNext()) {
			comment = i.next();
		}
		assertEquals(13728544, comment.getInternalId());
		date.set(2013, 7, 3, 14, 10, 40);
		assertEquals(date.getTime().toString(), comment.getCreationDate().toString()); // Comparing dates directly fails, despite having the same data
		assertEquals("SUCCESS: Integrated in Hadoop-Mapreduce-trunk #1507 (See [https://builds.apache.org/job/Hadoop-Mapreduce-trunk/1507/])\nHADOOP-9761.  ViewFileSystem#rename fails when using DistributedFileSystem (Andrew Wang via Colin Patrick McCabe) (cmccabe: http://svn.apache.org/viewcvs.cgi/?root=Apache-SVN&view=rev&rev=1509874)\n* /hadoop/common/trunk/hadoop-common-project/hadoop-common/CHANGES.txt\n* /hadoop/common/trunk/hadoop-common-project/hadoop-common/src/main/java/org/apache/hadoop/fs/FileSystemLinkResolver.java\n* /hadoop/common/trunk/hadoop-hdfs-project/hadoop-hdfs/src/main/java/org/apache/hadoop/hdfs/DistributedFileSystem.java\n* /hadoop/common/trunk/hadoop-hdfs-project/hadoop-hdfs/src/test/java/org/apache/hadoop/fs/viewfs/TestViewFileSystemHdfs.java\n", comment.getBody());
		author.setName("Hudson");
		author.setUsername("hudson");
		author.setEmail("jira@apache.org");
		assertEquals(author, comment.getAutor());	
	}

}
