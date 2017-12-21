
package algorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import model.CNFClause;
import model.CNFSubClause;
import model.Literal;
import writer.ProofTreeWriter;
import writer.ResultsWriter;

public class ResolutionAlgorithm {
	
	public static int step = 0;
	
	//check if we have an expression or just a literal and print the results of the PL_Algorithm accordingly
	public static void run(Literal a, CNFClause expression, CNFClause KB, String filename){

        
        CNFClause finalExpression = null;
    	
    	//if there is an expression and not a single literal, calculate and return the negated CNF
        if (a==null) {
        	
        	finalExpression = ExpressionNegated.getNegativeCNFinCNF(expression, filename);

        }
         
        
//        System.out.println("\n--------------------------------------------------------------------------------------------------");
//        System.out.println();
        
        //write proofTree.txt
        ProofTreeWriter.writeProofTree(KB, a, expression, filename);
        
        //Running resolution
        boolean b = PL_Resolution(KB, a, finalExpression, expression, filename);
        
        //print result
        if(a!=null) {
        	
//        	System.out.print("\nResult: ");
//        	System.out.print(a.printReturnAsString() + " is " + b + "\n");
        	
        }
        else {
//        	System.out.print("\nResult: \n");
//        	CNFClause.printLinear(expression);
//        	System.out.print("is " + b + "\n");
        }
        
        ResultsWriter.writeOneResultResult(b);
	}
	
	//The resolution algorithm
    public static boolean PL_Resolution(CNFClause KB, Literal a, CNFClause finalExpression, CNFClause initialExpression, String filename)
    {
    	//needed in order to populate proofTree.txt
        File f = null;
        PrintWriter writer = null;

        try {
            f = new File(filename);
        } catch (NullPointerException e) {
            System.err.println("File not found.");
        }

        try {
            writer = new PrintWriter(new FileWriter(f,true));
        } catch (FileNotFoundException e) {
            System.err.println("Error opening file!");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		}
    	
    	
        //We create a CNFClause that contains all the clauses of our Knowledge Base
        CNFClause clauses = new CNFClause();
        clauses.getSubclauses().addAll(KB.getSubclauses());
        
        //...plus a clause containing the negation of the expresison/literal we want to prove
        if (a != null) {
        	Literal aCopy = new Literal(a.getName(), !a.getNeg());
            CNFSubClause aClause = new CNFSubClause();
            aClause.getLiterals().add(aCopy);
            clauses.getSubclauses().add(aClause);

            //System.out.println("PL_Res Algorithm will try to prove: " + a.printReturnAsString());
        }
        else {
        	clauses.getSubclauses().addAll(finalExpression.getSubclauses());
        	
        	//System.out.println();
        	//System.out.println("PL_Res Algorithm will try to prove...");
        	//CNFClause.printLinear(initialExpression);
        }

//    	System.out.println("Combined clauses inserted to algorithm: ");
//    	System.out.println(CNFClause.printReturnAsString(clauses));
    	
    	ResultsWriter.writeOneResult(clauses);
        
        writer.println("Initial clauses inside algorithm: ");
        for (CNFSubClause sc : clauses.getSubclauses()) {
        	writer.print("| " + sc.printReturnAsString(true) + " |");
        }
        
        
        boolean stop = false;
        step = 1;
        
        writer.println("\n\nNew clauses produced at Step " + step + ":");
        int stepChange = 2;
        
        //We will try resolution till either we reach a contradiction or cannot produce any new clauses
        while(!stop)
        {
            Vector<CNFSubClause> newsubclauses = new Vector<CNFSubClause>();
            Vector<CNFSubClause> subclauses = clauses.getSubclauses();

            //System.out.println("Step:" + step);
            step++;
            //For every pair of clauses Ci, Cj...
            for(int i = 0; i < subclauses.size(); i++)
            {
                CNFSubClause Ci = subclauses.get(i);
                
                for(int j = i+1; j < subclauses.size(); j++)
                {
                    CNFSubClause Cj = subclauses.get(j);
//                    System.out.println("ci is: " + Ci.printReturnAsString(true));
//                    System.out.println("cj is: " + Cj.printReturnAsString(true));
                    //...we try to apply resolution and we collect any new clauses
                    Vector<CNFSubClause> new_subclauses_for_ci_cj = CNFSubClause.resolution(Ci, Cj);

//                    System.out.println("Clauses produced from: "+ (i+1) + (j+1)+ ": " + new_subclauses_for_ci_cj.size());
                    //We check the new subclauses...
                    for(int k = 0; k < new_subclauses_for_ci_cj.size(); k++)
                    {
                        CNFSubClause newsubclause = new_subclauses_for_ci_cj.get(k);

                        //...and if an empty subclause has been generated we have reached contradiction; and the literal has been proved
                        if(newsubclause.isEmpty()) 
                        {   
//                            System.out.println("----------------------------------");
//                            System.out.println("Resolution between");
//                            Ci.print();
//                            System.out.println("and");
//                            Cj.print();
//                            System.out.println("produced:");
//                            System.out.println("Empty subclause!!!");
//                            System.out.println("----------------------------------");
                            writer.println("\n\n----------------------------------------------------------------------------");
                            writer.println("Produced empty subclause between " + Ci.printReturnAsString(true) +
                            		" and " + Cj.printReturnAsString(true) + ",");
                            if(a!=null) {
                            	writer.println("therefore " + a.printReturnAsString() +" is true!");
                            }
                            else {
                            	writer.println("therefore ||" + CNFClause.printReturnAsString(initialExpression) +"|| is true!");
                            }
                            
                        	writer.close();
                            return true;
                        }
                        
                        //All clauses produced that don't exist already are added
                        if(!newsubclauses.contains(newsubclause) && !clauses.contains(newsubclause))
                        {
//                            System.out.println("----------------------------------");
//                            System.out.println("Resolution between");
//                            Ci.print();
//                            System.out.println("and");
//                            Cj.print();
//                            System.out.println("produced: ");
//                            newsubclause.print();
//                            System.out.println("----------------------------------");
                        	if (stepChange<step) {
                        		stepChange++;
                        		writer.println("\n\nNew clauses produced at Step " + (step-1) + ":");
                        	}
                            writer.print("| " +newsubclause.printReturnAsString(true) + " |");
                            newsubclauses.add(newsubclause);
                            
                        }
                    }                           
                }
            }
            
            boolean newClauseFound = false;

            //Check if any new clauses were produced in this loop
            for(int i = 0; i < newsubclauses.size(); i++)
            {
                if(!clauses.contains(newsubclauses.get(i)))
                {
                    clauses.getSubclauses().addAll(newsubclauses);                    
                    newClauseFound = true;
                }                        
            }

            //If not then Knowledge Base does not logically infer the literal we wanted to prove
            if(!newClauseFound)
            {
                //System.out.println("Not found new clauses");
                stop = true;
            }   
        }
        writer.println("\n\n----------------------------------------------------------------------------");
        writer.println("Could not produce an empty subclause,");
        if(a!=null) {
        	writer.println("therefore " + a.printReturnAsString() +" is false!");
        }
        else {
        	writer.println("therefore " + CNFClause.printReturnAsString(initialExpression) +" is false!");
        }
        
        writer.close();
        return false;
    }    

}
