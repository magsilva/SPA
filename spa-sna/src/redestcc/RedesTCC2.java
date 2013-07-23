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
public class RedesTCC2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        Util script = new Util();
        String[] nomes;
        nomes = script.lerArquivo("src/files/desenvolvedores3.txt", ">");

        System.out.println("graph\n"
                + "[ directed 0");
        int cont = 1;
        String[] email;
        
            ResultSet resultado = script.executaSQL("SELECT DISTINCT d.id AS id, d.nome AS nome, d.email AS email FROM desenvolvedor d, problema p, solucao s WHERE s.desenvolvedor_id = d.id AND p.desenvolvedor_id = d.id OR s.problema_id = p.id AND d.id != 672 AND d.id != 671 AND d.id != 1263", "presley");
            try {
                //System.out.println("teste tamanho: " + resultado.getFetchSize());
                while (resultado.next()){ 
                if (!resultado.getString("nome").isEmpty()) {
                    System.out.println("       node [\n"
                            + "                id n" + resultado.getString("id") + "\n"
                            + "                label \"" + resultado.getString("nome") + " <"+ resultado.getString("email") +">\"\n"
                            + "        ]");
                }
                }
            } catch (SQLException ex) {
                Logger.getLogger(RedesTCC2.class.getName()).log(Level.SEVERE, null, ex);
            }
            cont++;
        
        System.out.println("]");
    }
}
