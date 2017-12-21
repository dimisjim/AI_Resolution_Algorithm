
package algorithm;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import model.CNFClause;
import model.CNFSubClause;
import model.Literal;
import writer.ProofTreeWriter;

public class ExpressionNegated {

	//Step 1: Negate all literals of the expression individually
	//Step 2: Generate permutations
	//Step 3: Write the CNF negation on a txt
	//Step 4: Read that txt and construct the final CNFClause to be inserted
	//        in the resolution algorithm
	
	//find the negation of the expression in CNF form	
	public static CNFClause getNegativeCNFinCNF(CNFClause expression, String filename){

        List<String> result = new ArrayList<String>();
		CNFClause finalExpression = null;
    	CNFClause expressionNegatedCaseSize1 = new CNFClause();
        
        //negate all literals individually
        CNFClause expressionNegated = new CNFClause();
        for (CNFSubClause s: expression.getSubclauses()) {
        	CNFSubClause sc = new CNFSubClause();
        	for (Literal l: s.getLiterals()) {
        		Literal newl = new Literal(l.getName(), !l.getNeg(), false);
        		sc.getLiterals().add(newl);
        	}
        	expressionNegated.getSubclauses().add(sc);
        }
            
    	
        //compute new subclauses
        Vector<List<Literal>> ll = new Vector<List<Literal>>();
        for(CNFSubClause sc : expressionNegated.getSubclauses()) {
        	List<Literal> li = new ArrayList<>();
        	for(Literal l : sc.getLiterals()) {
        		li.add(l);
        	}
        	ll.add(li);
        }
        
        //if expression is only one clause, then we convert it like this:
        // NOT(a OR b OR c) ----> (NOT_a) AND (NOT_b) AND (NOT_c)
        // else we do the permutations process
        boolean oneClause = false;
        if(ll.size()==1){
        	oneClause = true;
        	
    		for (Literal l: expressionNegated.getSubclauses().get(0).getLiterals()){
    			CNFSubClause sc = new CNFSubClause();
    			sc.getLiterals().add(l);
    			expressionNegatedCaseSize1.getSubclauses().add(sc);
    		}
        	
        }
        else{
    		int depth = 0;
    		String current = "";
    		GeneratePermutations(ll, result, depth, current);
    		String resultAsString = listToString(result);
    		writer("expressionToBeProven.txt",resultAsString);
        }
        
		finalExpression = readFinalExpression("expressionToBeProven.txt");
        
		
		if(oneClause){
			//printNegationProcess(expression, expressionNegated, expressionNegatedCaseSize1, oneClause);
			ProofTreeWriter.writeNegationProcess(expression, expressionNegated, expressionNegatedCaseSize1, oneClause, filename);
			return expressionNegatedCaseSize1;
		}
		else{
			//printNegationProcess(expression, expressionNegated, finalExpression, oneClause);
			ProofTreeWriter.writeNegationProcess(expression, expressionNegated, finalExpression, oneClause, filename);
			return finalExpression;
		}
		
		
	}
	
    //basically an override of the toString method of a list
	//facilitates the writing of the negated expression into a txt file
    public static String listToString(List<String> list) {
	    String result2 = "(";
	    for (int i = 0; i < list.size(); i++) {
	    	if ((i+1)==list.size()) {
	    		result2 += list.get(i) + ")";
	    	}
	    	else {
	    		result2 += list.get(i) + ") AND (";
	    	}
	        
	    }
	    return result2;
	}
    
    //the premutation algorithm necessary
	public static void GeneratePermutations(Vector<List<Literal>> Lists, List<String> result, int depth, String current)
	{
	    if(depth == Lists.size())
	    {
	       result.add(current);
	       return;
	    }

	    for(int i = 0; i < Lists.get(depth).size(); ++i)
	    {
	    	if ( depth ==0) {
	    		GeneratePermutations(Lists, result, depth + 1, current + Lists.get(depth).get(i).printReturnAsString());
	    	}else {
	    		GeneratePermutations(Lists, result, depth + 1, current + " OR " + Lists.get(depth).get(i).printReturnAsString());
	    	}
	        
	    }
	}
	
	//read the CNF negation from the txt and return the final CNFClause constructed
	public static CNFClause readFinalExpression(String filename) {
		int lineCounter = 1;

        File f = null;
        BufferedReader TXTreader = null;
        String line;
        CNFClause expression =null;
        
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
    		
    		StringTokenizer st = new StringTokenizer(line.trim());
            String token = st.nextToken();
            CNFSubClause A = new CNFSubClause();
            expression = new CNFClause();
            
            while (st.hasMoreTokens()){
            	
            	if (token.equals("AND")){
            		expression.getSubclauses().add(A);
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
            	
            	if(!st.hasMoreTokens()) {
            		expression.getSubclauses().add(A);
            		break;
            	}
            	token = st.nextToken();
            	
            }
            
            lineCounter++;
    	
        	
        	return expression;

        } //try
        catch (IOException e) {
            System.err.println("Error reading line " + lineCounter + ".");
        }

        try {
            TXTreader.close();
        } catch (IOException e) {
            System.err.println("Error closing file.");
        }
		return null;

	}
	
	//write the negated expression on a txt
	public static void writer(String filename, String expression) {
    	
    	
        File f = null;
        PrintWriter writer = null;

        try {
            f = new File(filename);
        } catch (NullPointerException e) {
            System.err.println("File not found.");
        }

        try {
            writer = new PrintWriter(f, "UTF-8");
        } catch (FileNotFoundException e) {
            System.err.println("Error opening file!");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
        writer.println(expression);
        writer.close();
        
    }

	//prints the expression at each step of conversion
	public static void printNegationProcess(CNFClause expression, CNFClause expressionNegated, CNFClause finalExpression, boolean oneClause){
    	
		//test print of the initial non-negated expression
		System.out.println("\n\n--------------------------------------------------------------------------------------------------");
        if(!oneClause){
    		System.out.println("Initial Expression to be proven: ");
        	CNFClause.printLinear(expression);
        	
            //test print of the expression with each literal negated
            System.out.println("Negating all literals individually: ");
            CNFClause.printLinear(expressionNegated);
        }

        //test print of the final parsed expression
        System.out.println("Final Expression passed as CNFClause object to PL_Res Algorithm: ");
        CNFClause.printLinear(finalExpression);
	}

}
