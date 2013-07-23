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
public class RedesTCCUpdateDevs {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        Util script = new Util();
        String[] nomes;
        nomes = script.lerArquivo("src/files/emails.txt", ",");

        System.out.println("graph\n"
                + "[ directed 0");
        int cont = 1;
        for (String string : nomes) {
            //System.out.println("EMAILS:" + string);
            ResultSet resultado = script.executaSQL("SELECT nome AS nome FROM desenvolvedor WHERE email LIKE'%" + string + "%'", "presley");
            try {
                resultado.next();
                System.out.println("UPDATE desenvolvedor SET id = '" + cont + "' WHERE email = '"+ string +"'");
                script.executaUpdate("UPDATE desenvolvedor SET id = '" + cont + "' WHERE email = '"+ string +"'", "presley");
//                System.out.println(resultado.getString("nome") + "email: " + string);
//                if (!resultado.getString("nome").isEmpty()) {
//                    System.out.println("       node [\n"
//                            + "                id " + string + "\n"
//                            + "                label \"" + resultado.getString("nome") + "\"\n"
//                            + "        ]");
//                }
            } catch (SQLException ex) {
                Logger.getLogger(RedesTCCUpdateDevs.class.getName()).log(Level.SEVERE, null, ex);
            }
            cont++;
        }
        System.out.println("]");
    }
}
