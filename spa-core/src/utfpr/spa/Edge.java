package utfpr.spa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Edge<T, U>
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private T source;
	
	@ManyToOne
    private U target;
	
	protected static final String SEPARATOR = "-";
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public T getSource() {
		return source;
	}

	public void setSource(T source) {
		this.source = source;
	}

	public U getTarget() {
		return target;
	}

	public void setTarget(U target) {
		this.target = target;
	}

	
	
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		
		return result;
	}



    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Edge)) {
            return false;
        }
        
        Edge<T, U> other = (Edge<T, U>) object;
        if (source.equals(other.source) && target.equals(other.target)) {
        	if (id != other.id) {
        		throw new IllegalArgumentException("Objects are equal, but have different id");
        	}
        	return true;
        }
        if (id == other.id) {
        	throw new IllegalArgumentException("Objects are different, but have the same id");
        }
        return false;
    }

    
	@Override
    public String toString() {
        return source.toString() + SEPARATOR + target.toString();
    }
}
