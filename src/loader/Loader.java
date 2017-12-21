
package loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import algorithm.ResolutionAlgorithm;
import model.CNFClause;
import model.CNFSubClause;
import model.Literal;
import writer.ProofTreeWriter;

public class Loader {

	//loads KB and expression/literal from specified txt file
	public static void fileLoader(String filename){
    	int lineCounter = 1;

        File f = null;
        BufferedReader TXTreader = null;
        String line;

        try {
            f = new File(filename);
        } catch (NullPointerException e) {
            System.err.println("File not found.");
        }

        try {
            TXTreader = new BufferedReader(new FileReader(f));
        } catch (FileNotFoundException e) {
            System.err.println("Error opening file!");
        }

        try {

        	line = TXTreader.readLine();
    		 
            while (line!=null){
            	
        		StringTokenizer st = new StringTokenizer(line.trim());
                String token = st.nextToken();
                CNFSubClause A = new CNFSubClause();
                CNFClause KB = new CNFClause();
                CNFClause expression = new CNFClause();
                boolean endOfKB = false;
                boolean noMoreKB = false;
                boolean error = false;
                Literal a = null;
                
//                System.out.println("\n******************************* Running Algorithm for line "+ lineCounter +
//                		" **************************************");
//                System.out.println("\nKB: ");
            	
            	while (st.hasMoreTokens()){
                	
                	//detect a malformed txt
                	if (token.equals("(") || token.equals(")") || token.equals(":") || token.equals("):") || token.equals(":(")){
                		System.err.println("Malformed input file: [(, ), :] there shouldn't be a space in front AND after these characters");
                		error = true;
                		break;
                	}
                	
                	if (token.equals("AND")){
                		if (endOfKB){
                			expression.getSubclauses().add(A);
                		}
                		else{
                			KB.getSubclauses().add(A);
                		}
                		
                		//A.printLinear(false);
                		A = new CNFSubClause();
                		token = st.nextToken();
                	}
                	
                	if (token.startsWith("(") && !token.endsWith(")") && !token.endsWith(":")){
                		token = token.substring(1);
                		if (token.startsWith("NOT_")){
                			A.getLiterals().add(new Literal(token.substring(4), true));
                		}
                		else{
                			A.getLiterals().add(new Literal(token, false));
                		}
                	}
                	else if (token.endsWith(")") && !token.startsWith("(")){
                		token = token.substring(0, token.length()-1);
                		if (token.startsWith("NOT_")){
                			A.getLiterals().add(new Literal(token.substring(4), true));
                		}
                		else{
                			A.getLiterals().add(new Literal(token, false));
                		}
                	}
                	else if (token.equals("OR")){
                		token = st.nextToken();
                		if (token.endsWith(")")){
                			token = token.substring(0, token.length()-1);
                    		if (token.startsWith("NOT_")){
                    			A.getLiterals().add(new Literal(token.substring(4), true));
                    		}
                    		else{
                    			A.getLiterals().add(new Literal(token, false));
                    		}
                		}
                		else if (token.endsWith("):")){
                			token = token.substring(0, token.length()-2);
                			endOfKB = true;
                    		if (token.startsWith("NOT_")){
                    			A.getLiterals().add(new Literal(token.substring(4), true));
                    		}
                    		else{
                    			A.getLiterals().add(new Literal(token, false));
                    		}
                			
                		}
                		else{
                			A.getLiterals().add(new Literal(token, false));
                		}
                	}
                	else if (token.startsWith("(") && token.endsWith(")")){
                		token = token.substring(1, token.length()-1);
                		if (token.startsWith("NOT_")){
                			A.getLiterals().add(new Literal(token.substring(4), true));
                		}
                		else{
                			A.getLiterals().add(new Literal(token, false));
                		}
                	}
                	else if (token.endsWith(":")){
                		endOfKB = true;
                		if (token.startsWith("(")){
                			token = token.substring(1, token.length()-2);
                		}
                		else if (!token.substring(token.length()-2).equals(")")){
                			token = token.substring(0, token.length()-1);
                		}
                		else{
                			token = token.substring(0, token.length()-2);
                		}
                		
                		if (token.startsWith("NOT_")){
                			A.getLiterals().add(new Literal(token.substring(4), true));
                		}
                		else{
                			A.getLiterals().add(new Literal(token, false));
                		}
                		
                		
                	}
                	
                	if(endOfKB && !noMoreKB){
                		KB.getSubclauses().add(A);
//                		A.printLinear(true);
                		A = new CNFSubClause();
                		noMoreKB = true;
                		
//                		System.out.println("\nAlpha: ");
                	}
                	
                	if(!st.hasMoreTokens()) {
                		expression.getSubclauses().add(A);
//                		A.printLinear(true);
                		break;
                	}
                	token = st.nextToken();
                	
                	//if we need to prove only one literal
                	if(!st.hasMoreTokens()){
                		boolean neg;
                		if (token.startsWith("(") && token.endsWith(")")) {
                			token = token.substring(1, token.length()-1);
                		}
                        if (token.startsWith("NOT_")){
                        	neg = true;
                        	token = token.substring(4);
                        }
                        else{
                        	neg = false;
                        }
                        a = new Literal(token, neg);
//                        System.out.println(a.printReturnAsString());
                	}
                	
                }
                
//                System.out.println("\nprint subclauses of KB:");
//                for(CNFSubClause sc: KB.getSubclauses()){
//                	System.out.println("--");
//                	for (Literal l: sc.getLiterals()) {
//                		System.out.println(l.getName());
//                	}
//                	System.out.println("--");
//                }
//                
//                System.out.println("\nprint subclauses of alpha:");
//                for(CNFSubClause sc: expression.getSubclauses()){
//                	System.out.println("--");
//                	for (Literal l: sc.getLiterals()) {
//                		System.out.println(l.getName());
//                	}
//                	System.out.println("--");
//                }
                
                //initiate the resolution process
                if(!error){
                	String newFilename = ProofTreeWriter.writeInitial(filename);
                	ResolutionAlgorithm.run(a, expression, KB, newFilename);
                }
            	line = TXTreader.readLine();
            	lineCounter++;
            }
            
            
            
        } //try
        catch (IOException e) {
            System.err.println("Error reading line " + lineCounter + ".");
        }

        try {
            TXTreader.close();
        } catch (IOException e) {
            System.err.println("Error closing file.");
        }
	}
	
}
