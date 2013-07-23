/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package redestcc;

import br.usp.ime.ru.developer.Team;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
public class RedesTCCCriarRedeDevs {

    public static void main(String[] args) {
        FileWriter fileWriter = null;       
        try {
            Team previousTeam = new Team();
            List<String> desenvolvedores = new ArrayList<String>();
            Util script = new Util();
            //        ResultSet resultado = script.executaSQL("SELECT * FROM desenvolvedor", "presley");
            int cont = 0;
            fileWriter = new FileWriter(new File("src/files/IDS.txt"));
            PrintWriter printWriter = new PrintWriter(fileWriter);
            
            
            ResultSet resultado = script.executaSQL("select distinct p.id, s.id, p.desenvolvedor_id as devProblema, s.desenvolvedor_id as devSolucao from problema p left join solucao s on s.problema_id = p.id INNER JOIN desenvolvedor d ON d.id = s.desenvolvedor_id WHERE p.desenvolvedor_id != s.desenvolvedor_id GROUP BY devSolucao ORDER BY s.desenvolvedor_id", "presley");
            try {
                resultado.next();
                while (resultado.next()) {
                    cont++;
                    System.out.println("Dev:" + resultado.getString("devSolucao"));
                    //                devs = script.executaSQL("select * from desenvolvedor WHERE id = "+ resultado.getString("devSolucao"), "presley");
                    //                desenvolvedores.add(devs.getString("nome").trim() + " <" + devs.getString("email") + ">");
                    printWriter.println(resultado.getString("devSolucao")+",");
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
    //            TeamExporter teamExporter = new TeamExporter();
    //            teamExporter.exportTeamLineByLine(new File("src/files/desenvolvedoresNovos.txt"), previousTeam);
        } catch (IOException ex) {
            Logger.getLogger(RedesTCCCriarRedeDevs.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fileWriter.close();
            } catch (IOException ex) {
                Logger.getLogger(RedesTCCCriarRedeDevs.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
      

    }
}
