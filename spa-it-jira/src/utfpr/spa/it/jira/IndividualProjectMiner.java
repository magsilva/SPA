/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utfpr.spa.it.jira;

import apacheJiraMiner.util.Connection;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import utfpr.spa.it.Projeto;

/**
 *
 * @author Douglas
 */
public class IndividualProjectMiner {

    public static void main(String[] args) throws MalformedURLException, Exception {
        Connection.conectarDao();

        HttpProjetosMiner httpProjetos = new HttpProjetosMiner();
        try {
            httpProjetos.minerarProjetos();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Connection.fecharConexao();

        Connection.conectarDao();

        Projeto projeto = Connection.consultaProjetoPorKey("DERBY");
        int proximaPagina = 0;

        if (projeto != null) {
            HttpIssueMiner httpIssues = new HttpIssueMiner(projeto, proximaPagina, true, false);
            httpIssues.setFiltrarCodificarStrings(true, false);
            try {
                httpIssues.minerarIssues();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
