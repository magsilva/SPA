package utfpr.spa;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

/**
 *
 * @author Douglas
 */
@Entity
public class Project
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

	
    @Column
    private String name;
    
    @Column
    private String shortName;

    @Column
    @OneToMany
    private Set<Person> owners;
    
    @Column
    @OneToMany
    Set<Person> developers;
    
    @Column
    private String url;
    
    @OneToMany(cascade= CascadeType.REFRESH, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Artifact> artifacts;

    public Project() {
    	artifacts = new ArrayList<Artifact>();
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public List<Artifact> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(List<Artifact> issues) {
        this.artifacts = issues;
    }

    public void add(Artifact artifact) {
        if (! artifacts.contains(artifact)) {
        	artifacts.add(artifact);
        }
        artifact.setProject(this);
    }

    public void remove(Artifact artifact) {
        if (artifacts.contains(artifact)) {
        	artifacts.remove(artifact);
        }
        artifact.setProject(null);
    }
    
    public String getName() {
        return name;
    }

    public void setName(String nome) {
        this.name = nome;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Project)) {
            return false;
        }
        Project other = (Project) object;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "project[ id=" + id + " ]";
    }
}
