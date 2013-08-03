package utfpr.spa.it;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
public class Comment implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataComentario;
    
    @Column
    private String autor;
    
    @Column
    private String comentario;
    
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

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Date getDataComentario() {
        return dataComentario;
    }

    public void setDataComentario(Date dataComentario) {
        this.dataComentario = dataComentario;
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
        if (this.getComentario() == null) {
            return null;
        } else {
            return "Data:" + getDataComentario() + " Autor:" + getAutor() + " Comentario:" + getComentario();
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
                || getComentario() == null
                || getDataComentario() == null) {
            return false;
        }
        return true;
    }
}
