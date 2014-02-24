package utfpr.spa;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import com.ironiacorp.string.StringUtil;

public class Person
{
	private int id;
	
	private int internalId;
	
	private String name;
	
	private Set<String> alternativeNames;
	
	private String username;
	
	private String email;
	
	private Date firstDate;
	
	private boolean newcomer;
	
	private Set<String> alternativeEmails;

	public Person() {
		alternativeNames = new TreeSet<String>();
		alternativeEmails = new TreeSet<String>();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (StringUtil.isEmpty(name)) {
			throw new IllegalArgumentException("Invalid name");
		}
		
		alternativeNames.remove(name);
		this.name = name;
		
		if (name != null) {
			alternativeNames.add(name);
		}
	}

	public void addAlternativeName(String name) {
		if (name == null || StringUtil.isEmpty(name)) {
			throw new IllegalArgumentException("Invalid name");
		}

		alternativeNames.add(name);
	}
	
	public Iterator<String> getNames() {
		return alternativeNames.iterator();
	}
	
	public boolean isKnownAs(String name) {
		return alternativeNames.contains(name);
	}
	
	public int countNames() {
		return alternativeNames.size();
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		if (StringUtil.isEmpty(email)) {
			throw new IllegalArgumentException("Invalid email");
		}

		if (this.email != null) {
			alternativeEmails.remove(this.email);
		}
		this.email = email;
		if (email != null) {
			alternativeEmails.add(email);
		}
	}
	
	public void addAlternativeEmail(String email) {
		if (StringUtil.isEmpty(email)) {
			throw new IllegalArgumentException("Invalid email");
		}

		if (email != null) {
			alternativeEmails.add(email);
		}
	}
	
	public boolean canBeReachedBy(String email) {
		return alternativeEmails.contains(email);
	}
	
	public Iterator<String> getEmails() {
		return alternativeEmails.iterator();
	}
	
	public int countEmails() {
		return alternativeEmails.size();
	}

	public void merge(Person person) {
		Iterator<String> i = person.getNames();
		while (i.hasNext()) {
			String name = i.next();
			addAlternativeName(name);
		}

		i = person.getEmails();
		while (i.hasNext()) {
			String email = i.next();
			addAlternativeEmail(email);
		}
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public Date getFirstDate() {
		return firstDate;
	}

	public void setFirstDate(Date firstDate) {
		this.firstDate = firstDate;
	}
	
	public boolean isNewcomer() {
		return newcomer;
	}

	public void setNewcomer(boolean newcomer) {
		this.newcomer = newcomer;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getInternalId() {
		return internalId;
	}

	public void setInternalId(int internalId) {
		this.internalId = internalId;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		
		/*if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;*/
		
		/*if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;*/		
		return true;
	}

	@Override
	public String toString() {
		return name;
	}
}
