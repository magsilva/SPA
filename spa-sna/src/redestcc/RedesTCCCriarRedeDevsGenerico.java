package redestcc;

import br.usp.ime.ru.developer.Team;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.Util;

public class RedesTCCCriarRedeDevsGenerico
{
	public static final String DATABASE =  "presley";
    
    public static void main(String[] args)
    {
        Team team = new Team();
        List<String> developers = new ArrayList<String>();
        Util util = new Util();
        String[] ids;
        
        /*
        for ()
        ids = script.lerArquivo("src/files/IDS.txt", ",");
        */

        for (String string : ids) {
        	String sql = "select nome as nome, email as email from desenvolvedor where id = " + string;
        	ResultSet resultado = util.executaSQL(sql, DATABASE);
            try {
            	if (resultado.next()) {
            		String name = resultado.getString("nome").trim().toLowerCase();
            		String email = resultado.getString("email");
            		developers.add(name + " <" + email + ">");
            	} else {
            		throw new RuntimeException("Unexpected result: database should return at least one result");
            	}
            } catch (SQLException ex) {
        		throw new RuntimeException("Unexpected result: database should return at least one result");
            }
        }
        System.out.println("Developers (raw): " + developers.size());
        
        for (String dev : developers) {
            team.addDeveloperByString(dev);
        }
        System.out.println("Developers (without duplicates): " + team);
    }
}
