package utfpr.spa.it.jira;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import utfpr.spa.Edge;
import utfpr.spa.Person;
import utfpr.spa.it.Comment;
import utfpr.spa.it.Issue;

import com.ironiacorp.io.IoUtil;

public class CriaRedeCompleta {
	
	public Map<String,Person> devs = new HashMap<String,Person>();
	public HashMap<String, Edge> edges = new HashMap<String, Edge>();
	private Collection<Issue> issues;
	
	public CriaRedeCompleta(){
		
		List<String> arquivos = new ArrayList<>();
		issues = new HashSet<Issue>();
		try {
			geraArquivoGml();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static List<String> fileTreePrinter(File initialPath, int initialDepth) throws Exception {       
        List<String> arquivos = new ArrayList<>();
        if (initialPath.exists()) {  
            File[] contents = initialPath.listFiles();  
            for (int i=0; i<contents.length; i++) {  
                if (contents[i].isDirectory()) {  
                    fileTreePrinter(contents[i], initialDepth + 1);  
                }  
                else {  
                    String[] dpt = new String[initialDepth];  
                    for (int j=0; j<initialDepth; j++) {  
                        dpt[j] = "";  
                    }  
                    arquivos.add(contents[i].getName());
                    //System.out.println(contents[i].getName());                 
                }  
            }  
        }  
        return arquivos;
    } 
	
	public void buildNodes() throws Exception {
		
		//totalIssues+=issues.size();
		for (Issue issue : issues) {
			Person reporter = issue.getReporter();
			Person person = devs.get(reporter.getUsername());
			if(person == null){
				if(reporter.getFirstDate() == null){
					reporter.setFirstDate(issue.getCreationDate());				
				}
				devs.put(reporter.getUsername(),reporter);
			}else{
				if(issue.getCreationDate().before(person.getFirstDate())){
					person.setFirstDate(issue.getCreationDate());
				}
			}		
			Collection<Comment> comments = issue.getComentarios();
			for (Comment comment : comments) {
				Person autor = comment.getAuthor();
				Person personComment = devs.get(autor.getUsername());
				if(personComment == null){
					if(autor.getFirstDate() == null){
						autor.setFirstDate(comment.getCreationDate());				
					}
					devs.put(autor.getUsername(),autor);
				}else{
						if(comment.getCreationDate().before(personComment.getFirstDate())){
							personComment.setFirstDate(comment.getCreationDate());
						}
				}	
			}
		}
		System.out.println("Total nodes: " + devs.size());
	}
	
	public void buildEdges() throws Exception{				
		for (Issue issue : issues) {
			Collection<Comment> comments = issue.getComentarios();
			for (Comment comment : comments) {
				Edge edgeE = new Edge();
				edgeE.setSource(issue.getReporter());
				edgeE.setTarget(comment.getAuthor());
				edges.put(edgeE.toString(), edgeE);
			}
		}		
	}
	
	public void geraArquivoGml() throws IOException, ParseException{
		File arquivo = new File("C:/Users/AnaaMaciel/Documents/UTFPR/TCC/JIRA/Redes/redeCompletaEclipse_v1.gml"); 
        FileOutputStream fos = new FileOutputStream(arquivo);                
        
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
        Date startDate = new Date(); 
        Date endDate = new Date(); 
        Date endDateOld = new Date(); 
        startDate = (Date)formatter.parse("01/07/2012"); 
        endDate = (Date)formatter.parse("31/12/2012"); 
        endDateOld = (Date)formatter.parse("30/06/2012"); 
        String inicio_arquivo = "graph\n" + "[ directed 1.0\n"; 
        fos.write(inicio_arquivo.getBytes());
		for (Person dev : devs.values()) {
					if((dev.getFirstDate().after(startDate))&&(dev.getFirstDate().before(endDate))){
						dev.setNewcomer(true);
					}else if(dev.getFirstDate().before(endDateOld)){
						dev.setNewcomer(false);
					}
				
				String no = "       node [\n" 
											+ "                "
											+ "id \""+ dev.getUsername() + "\"\n" + ""
											+ "                label \""+ dev.getName() + " <"	+ dev.getUsername() + ">\"\n"
											+ "                firstDate \""+dev.getFirstDate()+"\"\n"
											+ "                newcomer "+dev.isNewcomer()+"\n"
					+ "        ]\n";
				
				fos.write(no.getBytes());
							
		}
		for(String chave: edges.keySet()){
			if((edges.get(chave).getSource().isNewcomer()) || (edges.get(chave).getTarget().isNewcomer())){
				String edge = "       edge [\n" 
							+ "                source \""+ edges.get(chave).getSource().getUsername()+ "\"\n" + ""
							+ "                target \""+edges.get(chave).getTarget().getUsername()+"\"\n"
							+ "        ]\n";
				fos.write(edge.getBytes());
			}
		    //System.out.println("chave: "+chave+", valor: "+edges.get(chave)+".");
		}
		
		
		
		String fim_arquivo = "]"; 
        fos.write(fim_arquivo.getBytes());
		fos.close(); 
	}
	
	private void obterDados(){
		List<String> arquivos = new ArrayList<>();
		try {
			arquivos = fileTreePrinter(new File("C:/Users/AnaaMaciel/Documents/UTFPR/TCC/JIRA/Dados/Hadoop-Commons/Jira/"), 0);
		} catch (Exception e) {			
			e.printStackTrace();
		} 
		for (String arquivo : arquivos) {
			try {
				String page = new String(
						IoUtil.dumpFile("C:/Users/AnaaMaciel/Documents/UTFPR/TCC/JIRA/Dados/Hadoop-Commons/Jira/"+ arquivo));
				JiraJson jiraJson = new JiraJson();
				issues.addAll(jiraJson.getIssue(page));
				
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
		
		System.out.println("Total issues: " + issues.size());
		
	}
	

	public static void main(String[] args) throws Exception {
		CriaRedeCompleta rede = new CriaRedeCompleta();
		rede.obterDados();
		rede.buildNodes();
		rede.buildEdges();
		rede.geraArquivoGml();
				
		
	}

}
