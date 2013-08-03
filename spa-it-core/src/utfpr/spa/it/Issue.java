package utfpr.spa.it;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import utfpr.spa.Artifact;
import utfpr.spa.Person;


@Entity
public class Issue extends Artifact
{
	@Column
	private String name;
	
	@Column
	private String summary;


	@Column
    private String description;
	
	@Column
	private Person assignee;
    
	@Column
	private Person reporter;
    
	@Column
	@Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    
	@Column
	@Temporal(TemporalType.TIMESTAMP)
    private Date resolvedDate;
    
	@Column
	@OneToMany(orphanRemoval=true, fetch=FetchType.LAZY)
    private List<Comment> comments;
    
	@Column
    private String configurationItemName;
    
	@Column
    private IssueStatus status;
	
	@Column
	private IssueResolution resolution;
    
	@Column
    private IssuePriority priority;
    
    public Issue() {
        comments = new ArrayList<Comment>();
    }

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
    
    public Person getAssignee() {
        return assignee;
    }

    public void setAssignee(Person assignee) {
        this.assignee = assignee;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getResolvedDate() {
        return resolvedDate;
    }

    public void setResolvedDate(Date resolvedDate) {
        this.resolvedDate = resolvedDate;
    }

    public Person getReporter() {
        return reporter;
    }

    public void setReporter(Person reporter) {
        this.reporter = reporter;
    }

    public List<Comment> getComentarios() {
        return comments;
    }

    public void setComentarios(List<Comment> coments) {
        if (coments != null) {
            for (Comment cmt : coments) {
                this.addComment(cmt);
            }
        }
    }

    public void addComment(Comment comment)
    {
        if (! comments.contains(comment)) {
            comments.add(comment);
        }
        comment.setIssue(this);
    }
    
    public void removeComment(Comment comment)
    {
        if (comments.contains(comment)) {
            comments.remove(comment);
        }
        comment.setIssue(null);
    }
    
    public IssuePriority getPriority() {
        return priority;
    }

    public void setPriority(IssuePriority priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IssueStatus getStatus() {
        return status;
    }

    public void setStatus(IssueStatus status) {
        this.status = status;
    }

	public IssueResolution getResolution() {
		return resolution;
	}

	public void setResolution(IssueResolution resolution) {
		this.resolution = resolution;
	}
    
    public String getComponent() {
        return configurationItemName;
    }

    public void setComponent(String component) {
        this.configurationItemName = component;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString()
    {
    	return "Issue{" + "num=" + getInternalId() + ", author=" + reporter.getName() + ", status=" + status + ", priority=" + priority + "}";
   }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result	+ ((assignee == null) ? 0 : assignee.hashCode());
		result = prime * result	+ ((comments == null) ? 0 : comments.hashCode());
		result = prime * result	+ ((configurationItemName == null) ? 0 : configurationItemName.hashCode());
		result = prime * result	+ ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result	+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result	+ ((priority == null) ? 0 : priority.hashCode());
		result = prime * result	+ ((reporter == null) ? 0 : reporter.hashCode());
		result = prime * result	+ ((resolvedDate == null) ? 0 : resolvedDate.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((resolution == null) ? 0 : resolution.hashCode());
		result = prime * result + ((summary == null) ? 0 : summary.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Issue other = (Issue) obj;
		if (assignee == null) {
			if (other.assignee != null)
				return false;
		} else if (!assignee.equals(other.assignee))
			return false;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (configurationItemName == null) {
			if (other.configurationItemName != null)
				return false;
		} else if (!configurationItemName.equals(other.configurationItemName))
			return false;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (priority != other.priority)
			return false;
		if (reporter == null) {
			if (other.reporter != null)
				return false;
		} else if (!reporter.equals(other.reporter))
			return false;
		if (resolvedDate == null) {
			if (other.resolvedDate != null)
				return false;
		} else if (!resolvedDate.equals(other.resolvedDate))
			return false;
		if (status != other.status)
			return false;
		if (resolution != other.resolution)
			return false;
		if (summary == null) {
			if (other.summary != null)
				return false;
		} else if (!summary.equals(other.summary))
			return false;
		return true;
	}
}
