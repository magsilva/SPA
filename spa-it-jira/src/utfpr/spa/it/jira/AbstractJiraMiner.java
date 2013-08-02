package utfpr.spa.it.jira;

import javax.persistence.EntityManager;

import com.ironiacorp.persistence.dao.jpa.JPA_Util;

public abstract class AbstractJiraMiner
{
	private static final String DEFAULT_ENTITY_MANAGER_NAME = "SPA";

	private final String emfName;
	
	private JPA_Util jpa;

	public AbstractJiraMiner()
	{
		this(DEFAULT_ENTITY_MANAGER_NAME);
	}

	public AbstractJiraMiner(String emfName)
	{
		jpa = new JPA_Util();
		jpa.loadEntityManagerFactory(emfName);
		this.emfName = emfName;
	}
	
	protected EntityManager getEntityManager()
	{
		return jpa.getEntityManager(emfName);
	}
}