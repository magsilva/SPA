package utfpr.spa;

import java.util.Date;
import java.util.Set;

public class Person
{
	private String name;
	
	private Set<String> alternativeNames;
	
	private String username;
	
	private String email;
	
	private Date firstDate;
	
	private boolean newcomer;
	
	private Set<String> alternativeEmails;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
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
