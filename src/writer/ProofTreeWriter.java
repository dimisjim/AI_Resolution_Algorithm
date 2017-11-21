package writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import model.CNFClause;
import model.Literal;

public class ProofTreeWriter {
	
	private static int line = 1;
	private static String currentFileName ="";
	private static boolean folderCreated = true;
	
	//instantiates each output result file
	public static String writeInitial(String inputFilename) {
		
		//detect change of inputfile in order to generate the correct filename for the individual results txts
		if (!currentFileName.equals(inputFilename)){
			line=1;
			currentFileName = inputFilename;
		}
		
		//check if folder "Results" is created only once
		if (folderCreated) {
	    	File theDir = new File("Results");
	    	// if the directory does not exist, create it
	    	if (!theDir.exists()) {
	    	    //System.out.println("creating directory: " + theDir.getName());
	    	    boolean result = false;

	    	    try{
	    	        theDir.mkdir();
	    	        result = true;
	    	    } 
	    	    catch(SecurityException se){
	    	        //handle it
	    	    }        
	    	    if(result) {    
	    	        //System.out.println("DIR created");  
	    	    }
	    	}
	    	folderCreated = false;
		}

		
		String filename = createFilename(inputFilename);
		
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
        
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String time = dateFormat.format(Calendar.getInstance().getTime());
        
        writer.println("-------------------------------------- " + time+  " -----------------------------------------");
        writer.println("-------------- Below are the detailed results of " + inputFilename +", line:"+line +" --------------\n\n");
    	
        line++;
    	writer.close();
    	return filename;
	}
	
	//sets the filename depending on input filename and line number 
	public static String createFilename(String inputFilename) {
		
        int i = 0;
        String s = System.getProperty("user.dir").concat("\\Results\\") 
        		+ inputFilename.substring(0, inputFilename.length()-4) +"_" + line + ".txt";
        //System.out.println(s);
        
    	int length  = s.length();
    	for (i =0 ; i<length;i++) {
    		
    		if (s.substring(i,i+1).equals("\\")) {
    			s = s.substring(0, i+1) + "\\" +s.substring(i+1, s.length());
    			length++;
    			i++;
    		}
    	}
    	//System.out.println(s);
		
		return s;
	}
	
	//writes the proof tree in each output result file
    public static void writeProofTree(CNFClause KB, Literal a, CNFClause expression, String filename){
    	
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
        
    	
    	if(a==null){
    		writer.println("Proof Tree of: " + CNFClause.printReturnAsString(KB) + ": " + CNFClause.printReturnAsString(expression));
    		writer.println("----------------------------------------------------------------------------\n");
    	}
    	else{
    		writer.println("Proof Tree of: " + CNFClause.printReturnAsString(KB) + ": " + a.printReturnAsString());
    		writer.println("----------------------------------------------------------------------------\n");
    	}
    	
    	writer.close();
    }

    //writes the negation process in each output file
	public static void writeNegationProcess(CNFClause expression, CNFClause expressionNegated, CNFClause finalExpression, boolean oneClause, String filename){
    	
		
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
		
		//test print of the initial non-negated expression
        writer.println("Negation Process");
		writer.println("--------------------------------------------------------------------------------------------------");
        if(!oneClause){
    		writer.println("Initial Expression to be proven: ");
    		writer.println(CNFClause.printReturnAsString(expression));
        	
            //test print of the expression with each literal negated
            writer.println("Negating all literals individually: ");
            writer.println(CNFClause.printReturnAsString(expressionNegated));
        }

        //test print of the final parsed expression
        writer.println("Final Expression passed as CNFClause object to PL_Res Algorithm: ");
        writer.println(CNFClause.printReturnAsString(finalExpression));
        
        writer.println("--------------------------------------------------------------------------------------------------\n");
        writer.close();
	}
    
}
