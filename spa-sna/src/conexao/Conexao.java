/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AnaMaciel
 */
public abstract class Conexao {
    
    private static Connection conexao = null;
    private static com.mysql.jdbc.Driver mysqlDriver;
    private final static String conecaoMysql = "jdbc:mysql://localhost:3306/?user=root&password=123456";

    /**
     * Estabelece uma conexão a base de dados
     * @return A conexão estabelecida
     */
    public static Connection conecta() {
        if (conexao == null) {
            try {
                mysqlDriver = new com.mysql.jdbc.Driver();
                conexao = DriverManager.getConnection(conecaoMysql);
                return conexao;
            } catch (SQLException ex) {
                Logger.getLogger(Conexao.class.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
        return conexao;
    }

    /**
     * Desconecta da base de dados
     */
    public static void desconecta() {
        try {
            conexao.close();
            DriverManager.deregisterDriver(mysqlDriver);
            conexao = null;
            mysqlDriver = null;
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
