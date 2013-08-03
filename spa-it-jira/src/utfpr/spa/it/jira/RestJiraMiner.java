package utfpr.spa.it.jira;

import java.io.IOException;
import java.net.URI;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import com.ironiacorp.http.HttpJob;
import com.ironiacorp.http.HttpJobRunner;
import com.ironiacorp.http.HttpMethod;
import com.ironiacorp.http.HttpMethodResult;
import com.ironiacorp.http.impl.httpclient4.HttpJobRunnerHttpClient4;

public class RestJiraMiner
{
	public RestJiraMiner() throws Exception
	{
		HttpJob job = new HttpJob(HttpMethod.GET, new URI("https://issues.apache.org/jira/rest/api/2/search?jql=project=HADOOP+ORDER+BY+key+ASC&startAt=1&maxResults=2&fields=*all"));
		HttpJobRunner runner = new HttpJobRunnerHttpClient4();
		runner.addJob(job);
		runner.run();
		HttpMethodResult result = job.getResult();

		String page = new String(result.getContentAsByteArray());
		System.out.println(page);
	
		// /rest/api/2/issue/{issueIdOrKey}/comment
	}
	
	public static void main(String[] args) throws Exception
	{
		RestJiraMiner miner = new RestJiraMiner();
	}

}
