package writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import model.CNFClause;

public class ResultsWriter {
	
	private static int lines = 1;
	
	//writes the initial header for the results.txt file
    public static void writeResultsInitial(String time){
    	
        File f = null;
        PrintWriter writer = null;

        try {
            f = new File("results.txt");
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
        
        writer.println("----------------------------- " + time+  " --------------------------------");
        writer.println("-------------- Below are the results of each input file: --------------");
    	
    	writer.close();
    }
	
    //writes the header for each input file
    public static void writeResults(String inputFile, String time){
    	
        File f = null;
        PrintWriter writer = null;

        try {
            f = new File("results.txt");
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
        
    	lines=1;
        writer.println("\n\nResults of input file " + inputFile + " (" +time+ ") are:");
    	writer.close();
    }
    
    //write the clauses to be used for the computation...
    public static void writeOneResult(CNFClause clauses){
    	
        File f = null;
        PrintWriter writer = null;

        try {
            f = new File("results.txt");
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
        
    	
    	writer.print("\n" + lines + ")  ||" + CNFClause.printReturnAsString(clauses) + "||");
    	
    	writer.close();
    }
    
    //... and write in the same line the result of the computation
    public static void writeOneResultResult(boolean result){
    	
        File f = null;
        PrintWriter writer = null;

        try {
            f = new File("results.txt");
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
        
    	
    	writer.print(" is " + result);
    	lines++;
    	
    	writer.close();
    }

}
