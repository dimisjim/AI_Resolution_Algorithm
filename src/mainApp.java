
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

import loader.Loader;
import writer.ResultsWriter;

public class mainApp 
{ 
	
    public static void main(String[] args) 
    {
        
    	
        //Specify file containing the KB and the literal/expression to be proved
        Scanner reader = new Scanner(System.in);  
        System.out.println("-------------------------------------- Resolution Algorithm --------------------------------------");
        System.out.println("--------------------------------------------------------------------------------------------------");
        System.out.println("*Example of input file (1st with one literal only, 2nd with an expression):");
        System.out.println("*1) (NOT_P21 OR B11) AND (P12 OR NOT_B11 OR P21) AND (NOT_B11): NOT_P12");
        System.out.println("*2) (NOT_P21 OR B11) AND (P12 OR NOT_B11 OR P21) AND (NOT_B11): (NOT_P12 OR P11) AND (B11 OR P11)");        
        System.out.println("--------------------------------------------------------------------------------------------------");
        
        
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String time = dateFormat.format(Calendar.getInstance().getTime());
        
        //instantiates the results.txt file
        ResultsWriter.writeResultsInitial(time);
        
        while(true){
        	System.out.println("\n**************************************************************************************************");
        	System.out.println("**************************************************************************************************");
            System.out.println("Specify filename containing the CNF KB and the to-be-infered expression: ");
            System.out.println("(Type and enter 0 to exit application.)");
            String filename = reader.nextLine();
            if (filename.equals("0")){
            	reader.close();
            	System.out.println("Program was terminated!");
            	break;
            }else{
            	
            	//appends the header for the selected input file
            	time = dateFormat.format(Calendar.getInstance().getTime());
            	ResultsWriter.writeResults(filename, time);
            	
            	//loads the txt containing the KB and the literal/expression to be proved
                Loader.fileLoader(filename);
                
                System.out.println("\nResults of " + filename + " where appended into results.txt");
                System.out.println("For detailed results of each line check 'Results' folder.");
            }
           
        }
        
    }

}
