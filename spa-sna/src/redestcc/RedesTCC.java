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
public class RedesTCC {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        Util script = new Util();
        String[] nomes;
        nomes = script.lerArquivo("src/files/desenvolvedoresNovos.txt", ">");

        System.out.println("graph\n"
                + "[ directed 0");
        int cont = 1;
        String[] email;
        for (String string : nomes) {
//            System.out.println("EMAILS:" + string);
            email = string.split("<");
//            System.out.println(email[1]);
            ResultSet resultado = script.executaSQL("SELECT nome AS nome, id AS id FROM desenvolvedor WHERE email LIKE'%" + email[1] + "%'", "presley");
            try {
                //System.out.println("teste tamanho: " + resultado.getFetchSize());
                resultado.next();                
                if (!resultado.getString("nome").isEmpty()) {
                    System.out.println("       node [\n"
                            + "                id n" + resultado.getString("id") + "\n"
                            + "                label \"" + string + ">\"\n"
                            + "        ]");
                }
            } catch (SQLException ex) {
                Logger.getLogger(RedesTCC.class.getName()).log(Level.SEVERE, null, ex);
            }
            cont++;
        }
        System.out.println("]");
    }
}
