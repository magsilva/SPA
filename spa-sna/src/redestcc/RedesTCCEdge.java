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
public class RedesTCCEdge {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        Util script = new Util();
        //System.out.println("EMAILS:" + string);
//        ResultSet resultado = script.executaSQL("SELECT DISTINCT p.desenvolvedor_id AS problema, s.desenvolvedor_id AS solucao FROM problema p, solucao s WHERE s.problema_id = p.id AND p.desenvolvedor_email != s.desenvolvedor_email ORDER BY problema", "presley");
        ResultSet resultado = script.executaSQL("SELECT DISTINCT p.desenvolvedor_id AS problema, s.desenvolvedor_id AS solucao\n" +
                    "FROM desenvolvedor d, problema p, solucao s WHERE d.id != 672 AND d.id != 671 AND d.id != 1263\n" +
                    "AND p.id = s.problema_id AND p.desenvolvedor_id != s.desenvolvedor_id\n" +
                     "ORDER BY p.id ASC", "presley");
        try {
            resultado.next();
            while (resultado.next()) {
                System.out.println("        edge [\n"
                        + "                source n" + resultado.getString("problema") + "\n"
                        + "                target n" + resultado.getString("solucao") + "\n"
                        + "        ]");
            }
            resultado.next();
        } catch (SQLException ex) {
            Logger.getLogger(RedesTCCEdge.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
