/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utfpr.spa.it.jira;

import java.io.ObjectInputStream.GetField;

import com.ironiacorp.persistence.dao.jpa.JPA_DAO;

import utfpr.spa.it.Projeto;

public class AllProjectsFullMiner extends AbstractJiraMiner
{
	public AllProjectsFullMiner()
	{
		super();
	}
	
	public void minerarSomenteProjetos() {
        HttpProjetosMiner httpProjetos = new HttpProjetosMiner();
        try {
            httpProjetos.minerarProjetos();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

	public boolean mineracaoDasIssuesDoProjetoJaIniciada(Projeto projeto)
    {
    	JPA_DAO<Integer, Projeto> dao = new JPA_DAO<Integer, Projeto>(Integer.class, Projeto.class);
    	dao.setEntityManager(getEntityManager());
        dao.refresh(projeto);
        if (projeto.getIssues().isEmpty()) {
            return false;
        }
        return true;
    }

    public void minerarIssuesDosProjetos(int idProjetoInicial, int idProjetoFinal)
    {
    	JPA_DAO<Integer, Projeto> dao = new JPA_DAO<Integer, Projeto>(Integer.class, Projeto.class);
    	dao.setEntityManager(getEntityManager());
        for (int i = idProjetoInicial; i <= idProjetoFinal; i++) {
            Projeto projeto = dao.findById(i);
            if (projeto != null && !mineracaoDasIssuesDoProjetoJaIniciada(projeto)) {
                HttpIssueMiner httpIssues = new HttpIssueMiner(projeto, 1, true, true);
                httpIssues.minerarIssues();
            }
        }
        dao.close();
    }
    

    public static void main(String[] args)
    {
    	AllProjectsFullMiner jiraMiner = new AllProjectsFullMiner();


        /*
         * este método irá coletar todos os projetos da página: "https://issues.apache.org/jira/secure/BrowseProjects.jspa#all"
         * não tem problema rodar este método várias vezes pois não cadastrará projetos repetidos
         */
        jiraMiner.minerarSomenteProjetos();


        /*
         * este método percorrerá todos os projetos cadastrados acima e irá minerar suas Issues e comentários
         */
        //    minerarIssuesDosProjetos(0, 999);

    }

}
