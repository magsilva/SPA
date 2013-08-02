package utfpr.spa.it.jira;

import java.io.File;

import com.ironiacorp.persistence.dao.jpa.JPA_DAO;

import utfpr.spa.it.Projeto;

public class AllProjectsCommitsMiner extends AbstractJiraMiner
{
	public AllProjectsCommitsMiner()
	{
		super();
	}
	
    public void processAllProjects()
    {
    	JPA_DAO<Integer, Projeto> dao = new JPA_DAO<Integer, Projeto>(Integer.class, Projeto.class);
    	dao.setEntityManager(getEntityManager());
        for (int id = 368; id >= 1; id--) {
            Projeto projeto = dao.findById(id);
            if (projeto != null && ! projetoJaMinerado(projeto)) {
                HttpIssueMiner httpIssues = new HttpIssueMiner(projeto);
                httpIssues.atualizarCommitsDasIssues();
            }
        }
        dao.close();
    }

    private boolean projetoJaMinerado(Projeto projeto) {
        File file = new File("src");
        File[] files = file.listFiles();
        for (File fl : files) {
            if (fl.getName().contains(".commit") && fl.getName().replaceAll(".commit", "").equals(projeto.getxKey())) {
                return true;
            }
        }
        return false;
    }
}
