package utfpr.spa.it;

import java.util.Date;

import javax.persistence.*;

import utfpr.spa.Artifact;
import utfpr.spa.Person;

@Entity
public class Comment extends Artifact
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date creationDate;
    
    @Column
    private Person author;
    
    @Column
    private String body;
    
    @Column
    @ManyToOne
    private Issue issue;

    public Comment() {
    }

    public Comment(Issue issue) {
        issue.addComment(this);
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Person getAutor() {
        return author;
    }

    public void setAuthor(Person author) {
        this.author = author;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Comment)) {
            return false;
        }
        Comment other = (Comment) object;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if (this.getBody() == null) {
            return null;
        } else {
            return "Data:" + getCreationDate() + " Autor:" + getAutor() + " Comentario:" + getBody();
        }

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean estaCompleto() {
        if (getAutor() == null
                || getBody() == null
                || getCreationDate() == null) {
            return false;
        }
        return true;
    }
}
