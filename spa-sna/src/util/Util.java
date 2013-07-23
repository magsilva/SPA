/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import conexao.Conexao;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AnaMaciel
 */
public class Util extends Conexao{
    
        /**
     * Le um arquivo e retorna em vetor os dados do arquivo
     * @param caminho onde o arquivo esta
     * @param quebraLinha o delimitador para quebrar a cadeia de caracter do arquivo
     * @return 
     */
    public String[] lerArquivo(String caminho, String quebraLinha) {
        File arquivo = new File(caminho);
        String texto = "";
        try {
            Scanner entradaDeDados = new Scanner(arquivo);
            while (entradaDeDados.hasNext()) {
                texto += entradaDeDados.nextLine();
            }
            return texto.split(quebraLinha);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public List<String> lerArquivoDevs(String caminho, String quebraLinha) {
        File arquivo = new File(caminho);
        String texto = "";
        String[] teste;
        List<String> devs = new ArrayList<>();
        try {
            Scanner entradaDeDados = new Scanner(arquivo);
            while (entradaDeDados.hasNext()) {
                texto = entradaDeDados.nextLine();
                teste = texto.split("<");
                //System.out.println(teste[0]);
                devs.add(teste[0].trim());                
            }
            return devs;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

       /**
     * Le um arquivo e retorna em vetor os dados do arquivo
     * @param caminho onde o arquivo esta
     * @param quebraLinha o delimitador para quebrar a cadeia de caracter do arquivo
     * @return 
     */
    public List<String> verificarDesenvolvedoresDaAPI(String caminho, String quebraLinha) {
        File arquivo = new File(caminho);
        String texto = "";
        String[] teste;
        List<String> devs = new ArrayList<>();
        try {
            Scanner entradaDeDados = new Scanner(arquivo);
            while (entradaDeDados.hasNext()) {
                texto = entradaDeDados.nextLine();
                teste = texto.split("<");
                //System.out.println(teste[0]);
                devs.add(teste[0].trim());                
            }
            return devs;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public String[] lerArquivoIndexado(String caminho, String quebraLinha) {
        File arquivo = new File(caminho);
        String texto = "";
        String[] teste;
        try {
            Scanner entradaDeDados = new Scanner(arquivo);
            while (entradaDeDados.hasNext()) {
                texto = entradaDeDados.nextLine();
                //System.out.println(texto.split(";").length); 
                if(texto.split(";").length > 2)
                    System.out.println(texto);
            }
            //return texto.split("@");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Executa SQL no banco.
     * 
     * @param comando o comando de SELECT 
     * @param database a base de dados onde quer executar o comando
     * @return o resultado query do select realizado
     */
    public ResultSet executaSQL(String comando, String database)
    {
        Connection conn = conecta();
        try {
             Statement stmt = conn.createStatement();
             stmt.execute("use " + database);
             stmt = conn.createStatement();
             return stmt.executeQuery(comando);
        } catch (SQLException e) {
            throw new RuntimeException("Error reading data from database", e);
        }
    }
    
    /**
     * Executa SQL no banco
     * 
     * @param comando o comando de SELECT 
     * @param database a base de dados onde quer executar o comando
     * @return o resultado query do select realizado
     */
    public int executaUpdate(String comando,String database)
    {
    	 Connection conn = conecta();
         try {
              Statement stmt = conn.createStatement();
              stmt.execute("use " + database);
              stmt = conn.createStatement();
              return stmt.executeUpdate(comando);
         } catch (SQLException e) {
             throw new RuntimeException("Error reading data from database", e);
         }
    }

    /**
     * Le um arquivo e imprime os comando do mesmo
     * @param caminho local do arquivo
     */
    public void imprimirSQL(String caminho) {
        String comando[];
        comando = lerArquivo(caminho, ";");
        for (String string : comando) {
            System.out.println(string);
        }
    }
    
}
