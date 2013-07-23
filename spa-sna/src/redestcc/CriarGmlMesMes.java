/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package redestcc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import redestcc.RedesTCC;
import util.Util;

/**
 *
 * @author AnaaMaciel
 */
public class CriarGmlMesMes {

    public static List<String> criaNodesNovatos() {
        Util script = new Util();
        String[] ids;
        int contSolucao = 0, contProblema = 0, contTotal = 0;
        ids = script.lerArquivo("src/files/IDSNovatos.txt", ",");

        int cont = 1;
        String[] email;
        ResultSet resultadoIds = script.executaSQL("SELECT DISTINCT d.nome, d.id, d.email FROM desenvolvedor d, problema p, solucao s WHERE p.desenvolvedor_id = d.id AND s.desenvolvedor_id = d.id AND p.dataRelato BETWEEN '2012-12-01' AND '2012-12-31' ORDER BY d.id", "presley");
        List<String> devsNovatos = new ArrayList<>();
        System.out.println("graph\n"
                + "[ directed 0");

        try {
            while (resultadoIds.next()) {
                for (String idNovato : ids) {
                    //System.out.println(idNovato);

                    //System.out.println("ID ARQUivo: " + idNovato + " ID select: " + resultadoIds.getString("id"));

                    ResultSet resultadoSolucao = script.executaSQL("SELECT COUNT(*) AS contSolucao FROM solucao s WHERE s.dataProposta BETWEEN '2012-12-01' AND '2012-12-31' AND desenvolvedor_id = " + idNovato, "presley");
                    ResultSet resultadoProblema = script.executaSQL("SELECT COUNT(*) AS contProblema FROM problema p WHERE p.dataRelato BETWEEN '2012-12-01' AND '2012-12-31' AND desenvolvedor_id = " + idNovato, "presley");
                    if (idNovato.equals(resultadoIds.getString("id"))) {
                        resultadoProblema.next();
                        resultadoSolucao.next();
                        System.out.println("       node [\n"
                                + "                id n" + resultadoIds.getString("id") + "\n"
                                + "                label \"" + resultadoIds.getString("nome").trim().toLowerCase() + " <" + resultadoIds.getString("email").trim() + ">\"\n"
                                + "                problemas \"" + resultadoProblema.getString("contProblema") + "\"\n"
                                + "                solucoes \"" + resultadoSolucao.getString("contSolucao") + "\"\n"
                                + "                novato \"true\"\n"
                                + "        ]");
                        devsNovatos.add(idNovato);
                    }
                    contProblema = 0;
                    contSolucao = 0;
                }
                resultadoIds.next();
            }
        } catch (SQLException ex) {
            Logger.getLogger(CriarGmlMesMes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return devsNovatos;
    }

    public static void criaNodesVeteranos() {
        Util script = new Util();
        String[] ids;
        ids = script.lerArquivo("src/files/IDSVelhosComEdgeNovato.txt", ",");

        int cont = 1;
        int contSolucao = 0, contProblema = 0, contTotal = 0;
        List<String> idsVelhos = new ArrayList<>();
//            ResultSet resultadoProblema = script.executaSQL("SELECT p.* FROM problema p WHERE p.desenvolvedor_id = " + id, "presley");
//            ResultSet resultadoSolucao = script.executaSQL("SELECT s.* FROM solucao s WHERE s.desenvolvedor_id = " + id, "presley");
        ResultSet resultadoEdges = script.executaSQL("SELECT DISTINCT p.desenvolvedor_id AS problema, s.desenvolvedor_id AS solucao from problema p, solucao s\n"
                + "WHERE p.id = s.problema_id AND p.desenvolvedor_id != 672 AND p.desenvolvedor_id != 671\n"
                + "AND p.desenvolvedor_id != 1263 AND p.desenvolvedor_id != s.desenvolvedor_id\n"
                + "AND p.dataRelato between '2012-12-01' AND '2012-12-31' AND dataProposta between '2012-12-01' AND '2012-12-31'", "presley");
        try {

            while (resultadoEdges.next()) {
                for (String idVelho : ids) {
                    //System.out.println("ID: " + idVelho + " P: " + resultadoEdges.getString("problema") + " S: " + resultadoEdges.getString("solucao"));
                    if ((resultadoEdges.getString("problema").equals(idVelho)) || (resultadoEdges.getString("solucao").equals(idVelho))) {
                        if (!idsVelhos.contains(idVelho)) {
                            idsVelhos.add(idVelho);
                            //System.out.println(idVelho);
                        }
                    }
                }
            }
            for (String id : idsVelhos) {
//                System.out.println(id);
                ResultSet resultadoSolucao = script.executaSQL("SELECT COUNT(*) AS contSolucao FROM solucao s WHERE s.dataProposta BETWEEN '2012-12-01' AND '2012-12-31' AND desenvolvedor_id = " + id, "presley");
                ResultSet resultadoProblema = script.executaSQL("SELECT COUNT(*) AS contProblema FROM problema p WHERE p.dataRelato BETWEEN '2012-12-01' AND '2012-12-31' AND desenvolvedor_id = " + id, "presley");
                ResultSet resultadoDev = script.executaSQL("SELECT d.* FROM desenvolvedor d WHERE d.id = " + id, "presley");
                resultadoProblema.next();
                resultadoSolucao.next();

                resultadoDev.next();
                System.out.println("       node [\n"
                        + "                id n" + resultadoDev.getString("id") + "\n"
                        + "                label \"" + resultadoDev.getString("nome").trim().toLowerCase() + " <" + resultadoDev.getString("email").trim() + ">\"\n"
                        + "                problemas \"" + resultadoProblema.getString("contProblema") + "\"\n"
                        + "                solucoes \"" + resultadoSolucao.getString("contSolucao") + "\"\n"
                        + "                novato \"false\"\n"
                        + "        ]");
                contProblema = 0;
                contSolucao = 0;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CriarGmlMesMes.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void criaEdges(List<String> devsNovatos) {
        Util script = new Util();
        String[] ids;
        ids = script.lerArquivo("src/files/IDSVelhosComEdgeNovato.txt", ",");

        int cont = 1;
        int contSolucao = 0, contProblema = 0, contTotal = 0;
        List<String> idsVelhos = new ArrayList<>();

        ResultSet resultadoEdges = script.executaSQL("SELECT DISTINCT p.desenvolvedor_id AS problema, s.desenvolvedor_id AS solucao from problema p, solucao s\n"
                + "WHERE p.id = s.problema_id AND p.desenvolvedor_id != 672 AND p.desenvolvedor_id != 671\n"
                + "AND p.desenvolvedor_id != 1263 AND p.desenvolvedor_id != s.desenvolvedor_id\n"
                + "AND p.dataRelato between '2012-12-01' AND '2012-12-31' AND dataProposta between '2012-12-01' AND '2012-12-31'", "presley");
        try {
            while (resultadoEdges.next()) {
                for (String string : devsNovatos) {
                    if (string.equals(resultadoEdges.getString("problema")) || string.equals(resultadoEdges.getString("solucao"))) {
                        System.out.println("        edge [\n"
                                + "                source n" + resultadoEdges.getString("problema") + "\n"
                                + "                target n" + resultadoEdges.getString("solucao") + "\n"
                                //+ "                qtdMsgs " + cont + "\n"
                                + "        ]");
                    }
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(CriarGmlMesMes.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("]");
    }

    public static void criaEdges2(List<String> devsNovatos) {
        Util script = new Util();
        String[] ids;
        ids = script.lerArquivo("src/files/IDSVelhosComEdgeNovato.txt", ",");

        int cont = 1;
        int contSolucao = 0, contProblema = 0, contTotal = 0;
        List<String> idsVelhos = new ArrayList<>();

        ResultSet resultadoEdges = script.executaSQL("SELECT DISTINCT p.desenvolvedor_id AS problema, s.desenvolvedor_id AS solucao from problema p, solucao s\n"
                + "WHERE p.id = s.problema_id AND p.desenvolvedor_id != 672 AND p.desenvolvedor_id != 671\n"
                + "AND p.desenvolvedor_id != 1263 AND p.desenvolvedor_id != s.desenvolvedor_id\n"
                + "AND p.dataRelato between '2012-12-01' AND '2012-12-31' AND dataProposta between '2012-12-01' AND '2012-12-31'", "presley");
        try {
            while (resultadoEdges.next()) {
                for (String string : devsNovatos) {
                    if (string.equals(resultadoEdges.getString("problema")) || string.equals(resultadoEdges.getString("solucao"))) {
                        System.out.println("        edge [\n"
                                + "                source n" + resultadoEdges.getString("problema") + "\n"
                                + "                target n" + resultadoEdges.getString("solucao") + "\n"
                                //+ "                qtdMsgs " + cont + "\n"
                                + "        ]");
                    }
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(CriarGmlMesMes.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("]");
    }
    
    public static void main(String[] args) {
        List<String> devsNovatos = CriarGmlMesMes.criaNodesNovatos();
        CriarGmlMesMes.criaNodesVeteranos();
        CriarGmlMesMes.criaEdges(devsNovatos);
    }
}
