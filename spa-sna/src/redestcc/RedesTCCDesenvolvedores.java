/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package redestcc;

import br.usp.ime.ru.developer.Developer;
import br.usp.ime.ru.developer.Team;
import br.usp.ime.ru.developer.TeamCompleteEmailLineStringFormater;
import br.usp.ime.ru.developer.TeamExporter;
import br.usp.ime.ru.exporter.stringbuilder.DataFileManager;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Util;

/**
 *
 * @author ZENILUIZ
 */
public class RedesTCCDesenvolvedores {

    public static void main(String[] args) {
        Team previousTeam = new Team();
        List<String> desenvolvedores = new ArrayList<String>();
        Util script = new Util();
//        ResultSet resultado = script.executaSQL("SELECT * FROM desenvolvedor", "presley");
        int cont=0;
        ResultSet resultado = script.executaSQL("SELECT DISTINCT d.* FROM desenvolvedor d, problema p, solucao s WHERE s.desenvolvedor_id = d.id AND p.desenvolvedor_id = d.id AND s.problema_id = p.id", "presley");
        try {
            resultado.next();            
            while (resultado.next()) {
                cont++;
//                System.out.println("I = "+ cont);
                //System.out.println("oi");
//                if (!(resultado.getString("nome").contains("=?"))) {
                    
                    desenvolvedores.add(resultado.getString("nome").trim() + " <" + resultado.getString("email") + ">");
//                } else {
////                    System.out.println(resultado.getString("email").trim());
//                    desenvolvedores.add("<" + resultado.getString("email") + ">");
//                }
            }
//            resultado.next();
        } catch (SQLException ex) {
            Logger.getLogger(RedesTCC.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("DEVS SIZE: " + desenvolvedores.size());
        for (int i = 0; i < desenvolvedores.size(); i++) {
            previousTeam.addDeveloperByString(desenvolvedores.get(i));            
        }
        
        //CRIA O ARQUIVO COM OS DEVS MESCLADOS
//        TeamExporter teamExporter = new TeamExporter();
//        teamExporter.exportTeamLineByLine(new File("src/files/desenvolvedores3.txt"), previousTeam);
        
        
        
//        HashSet<Developer> devs = new HashSet<>();
//        devs = previousTeam.getUniqueDevelopers();
//        for (Iterator<Developer> it = devs.iterator(); it.hasNext();) {
//            Developer developer = it.next();
//            System.out.println("Dev: " + developer.getEmailList());
//        }
        
        DataFileManager<Team> teamFileManager = new DataFileManager<Team>(new TeamCompleteEmailLineStringFormater());
        Team testTeam = teamFileManager.loadFromFile(new File("src/files/desenvolvedores3.txt"));

        TeamExporter teamExporter = new TeamExporter();
        teamExporter.exportTeamIndexedByEmails(new File("src/files/desenvolvedoresIndexedByEmails2.txt"), testTeam);
//        teamExporter.exportTeamIndexedByNames(new File("src/files/desenvolvedoresIndexedByNames.txt"), previousTeam);
//        teamExporter.exportTeamLineByLine(new File("src/files/desenvolvedoresLineByLine.txt"), previousTeam);
        
        
        
        
        
//        TeamExporter teamExporter = new TeamExporter();
//        DataFileManager<Team> teamFileManager = new DataFileManager<Team>(new TeamCompleteEmailLineStringFormater());
//        Team myteam = teamFileManager.loadFromFile(new File("src/files/desenvolvedoresLineByLine.txt"));
        
//        teamExporter.exportTeamLineByLine(new File("teste1.txt"),myteam);
//        teamExporter.exportTeamIndexedByNames(new File("teste2.txt"), myteam);
    }
}
