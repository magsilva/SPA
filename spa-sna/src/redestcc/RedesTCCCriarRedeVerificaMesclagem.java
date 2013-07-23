/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package redestcc;

import br.usp.ime.ru.developer.Team;
import br.usp.ime.ru.developer.TeamCompleteEmailLineStringFormater;
import br.usp.ime.ru.developer.TeamExporter;
import br.usp.ime.ru.exporter.stringbuilder.DataFileManager;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Util;

/**
 *
 * @author ZENILUIZ
 */
public class RedesTCCCriarRedeVerificaMesclagem {

    public static void main(String[] args) {
        FileWriter fileWriter = null;

        List<String> desenvolvedores = new ArrayList<String>();
        Util script = new Util();
        int cont = 0;
        String[] string;
        string = script.lerArquivoIndexado("src/files/desenvolvedoresIndexados.txt", ";");
        //System.out.println(string);
//        for (String str : string) {
//            System.out.println(str);
//        }

        List<String> devsAPI = new ArrayList<String>();
        devsAPI = script.lerArquivoDevs("src/files/desenvolvedoresAPI.txt", ",");
        boolean contem = true;

        desenvolvedores = script.verificarDesenvolvedoresDaAPI("src/files/desenvolvedoresTODOS.txt", "<");

        for (String string1 : desenvolvedores) {
                for (String string2 : devsAPI) {
//                    System.out.println("2: " + string2);
                    if (string1.equals(string2)) {
                        //System.out.println(string1 + '-' + string2);
                        contem = true;
                        break;
                    }
                    
                }
                if (!contem) {
                        System.out.println(string1);
                    }
                    contem = false;

        }

    }
}
