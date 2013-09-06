package utfpr.spa.vcs;

import java.util.Collection;
import java.util.Date;

import utfpr.spa.Artifact;
import utfpr.spa.Person;

public class Patch extends Artifact
{
	private Person author;
	
	private Person commiter;
	
	private Collection<SourceCodeFile> files;
	
	private String description;
	
	private String version;
	
	private Date commitDate;
	
	private int size;
}
