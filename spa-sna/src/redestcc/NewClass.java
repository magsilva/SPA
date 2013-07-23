/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package redestcc;

import br.usp.ime.ru.developer.Team;
import br.usp.ime.ru.developer.TeamExporter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ZENILUIZ
 */
public class NewClass {
    public static void main(String[] args) {
        Team previousTeam = new Team();
//        previousTeam.addDeveloperByString("jteodoro <jteodoro@ime.usp.br>");
//        
//        previousTeam.assignGeneralEmails(".+(@apache.org)"); //define um email principal para cada pessoa
//        
//        previousTeam.getUniqueDevelopers();//retorna uma lista dos desenvolvedores que não se repetem
//        
//        DataFileManager<Team> teamFileManager = new DataFileManager<Team>(new TeamCompleteEmailLineStringFormater());
//        //teamFileManager.saveToFile(previousTeam, file);//salva no arquivo 
//        
//        
//        TeamExporter exporter = new TeamExporter();
        //exporter.exportTeamLineByLine(file,team); jeito mais facil de exportar
        //exportTeamIndexedByNames
        //exportTeamIndexedByEmails
        
        List<String> teste = new ArrayList<String>();
        teste.add("alexey n. solofnenko <trelawny@pacbell.net>");
        teste.add("alexey n. solofnenko <trelawny@pacbell.net>");
        teste.add("alexey solofnenko <a.solofnenko@mdli.com>");
        teste.add("alexey solofnenko <a.solofnenko@mdli.com>");
        teste.add("alexey solofnenko <a.solofnenko@mdl.com>");
        teste.add("alexey solofnenko <a.solofnenko@mdl.com>");
        teste.add("alexey panchenko <alex+news@olmisoft.com>");
        teste.add("alexey n. solofnenko <trelony@gmail.com>");
        teste.add("alexey n. solofnenko <trelony@gmail.com>");
        teste.add("alexey n. solofnenko <trelawny@pacbell.net>");
        
        for (int i=0;i<teste.size();i++) {
            previousTeam.addDeveloperByString(teste.get(i));
        }

        TeamExporter teamExporter = new TeamExporter();
//        DataFileManager<Team> teamFileManager = new DataFileManager<Team>(new TeamCompleteEmailLineStringFormater());
        //Team myteam = teamFileManager.loadFromFile(new File("teste.txt"));
        
        //Team buildedTeam = teamMiner.buildTeam(recursiveFileTeamFinder, previousTeam);
        teamExporter.exportTeamLineByLine(new File("teste111.txt"),previousTeam);
        teamExporter.exportTeamIndexedByNames(new File("testeNames.txt"), previousTeam);
        teamExporter.exportTeamIndexedByEmails(new File("testeEmails.txt"), previousTeam);
    }
}
