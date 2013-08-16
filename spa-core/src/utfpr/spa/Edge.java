package utfpr.spa;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

/**
 *
 * @author AnaMaciel
 */
@Entity
public class Edge
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private Person source;
	
	@Column
    private Person target;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Person getSource() {
		return source;
	}

	public void setSource(Person source) {
		this.source = source;
	}

	public Person getTarget() {
		return target;
	}

	public void setTarget(Person target) {
		this.target = target;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Edge)) {
            return false;
        }
        Edge other = (Edge) object;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return source.getUsername() + "-" + target.getUsername();
    }
}
