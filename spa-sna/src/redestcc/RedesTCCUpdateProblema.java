/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package redestcc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Util;

/**
 *
 * @author AnaMaciel
 */
public class RedesTCCUpdateProblema {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        Util script = new Util();
        //System.out.println("EMAILS:" + string);
        ResultSet resultado = script.executaSQL("SELECT desenvolvedor_email AS email, id AS id FROM problema", "presley");
        try {
//            resultado.next();
//            while (resultado.next()) {
//                System.out.println("        edge [\n"
//                        + "                source " + resultado.getString("problema") + "\n"
//                        + "                target " + resultado.getString("solucao") + "\n"
//                        + "        ]");
//            }
//            resultado.next();
            ResultSet resultadoUpdate;
            while(resultado.next()){
                resultadoUpdate = script.executaSQL("SELECT id AS id FROM desenvolvedor WHERE email = '"+ resultado.getString("email") +"'", "presley");
                while(resultadoUpdate.next()){
                    System.out.println("UPDATE problema SET desenvolvedor_id = '" + resultadoUpdate.getString("id") + "' WHERE desenvolvedor_email = '"+ resultado.getString("email") +"'");
                    script.executaUpdate("UPDATE problema SET desenvolvedor_id = '" + resultadoUpdate.getString("id") + "' WHERE desenvolvedor_email = '"+ resultado.getString("email") +"'", "presley");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(RedesTCCUpdateProblema.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
