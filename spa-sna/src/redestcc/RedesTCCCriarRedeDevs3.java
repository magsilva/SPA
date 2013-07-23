/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package redestcc;

import br.usp.ime.ru.developer.Team;
import br.usp.ime.ru.developer.TeamExporter;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Util;

/**
 *
 * @author ZENILUIZ
 */
public class RedesTCCCriarRedeDevs3 {

    public static void main(String[] args) {
        Team previousTeam = new Team();
        List<String> desenvolvedores = new ArrayList<String>();
        Util script = new Util();
        int cont = 0;
        String[] ids;
        ids = script.lerArquivo("src/files/IDS.txt", ",");


        for (String string : ids) {
            ResultSet resultado = script.executaSQL("select nome as nome, email as email from desenvolvedor where id = " + string, "presley");
            System.out.println("select nome as nome, email as email from desenvolvedor where id = " + string);
            try {
                resultado.next();
                desenvolvedores.add(resultado.getString("nome") + "<" + resultado.getString("email") + ">");
            } catch (SQLException ex) {
                Logger.getLogger(RedesTCC.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("DEVS SIZE: " + desenvolvedores.size());
        for (int i = 0; i < desenvolvedores.size(); i++) {
            previousTeam.addDeveloperByString(desenvolvedores.get(i));
        }
        
        
        
        //CRIA O ARQUIVO COM OS DEVS MESCLADOS
        TeamExporter teamExporter = new TeamExporter();
        teamExporter.exportTeamLineByLine(new File("src/files/desenvolvedoresNovos.txt"), previousTeam);



    }
}
