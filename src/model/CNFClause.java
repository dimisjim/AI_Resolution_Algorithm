
package model;

import java.util.Vector;

/*
 * A CNFClause consists of a conjunction of CNFSubClauses
 * And each CNFSubClause in turn consists of a disjunction of Literals
 */
public class CNFClause 
{
    public Vector<CNFSubClause> theClauses = new Vector<CNFSubClause>();
    
    public Vector<CNFSubClause> getSubclauses()
    {
        return theClauses;
    }
    
    public boolean contains(CNFSubClause newS)
    {
        for(int i = 0; i < theClauses.size(); i ++)
        {
            if(theClauses.get(i).getLiterals().equals(newS.getLiterals()))
            {
                return true;
            }
        }
        return false;
    }
    
    public static void printLinear(CNFClause expression){
    	int i = 0;
    	System.out.println("---------");
    	for (CNFSubClause s: expression.getSubclauses()) {
    		i++;
    		if (expression.getSubclauses().size()==i) {
    			s.printLinear(true);
    		}
    		else {
    			s.printLinear(false);
    		}
    	}
    	System.out.println("\n---------");
    }
    
    public static String printReturnAsString(CNFClause expression){
    	int i = 0;
    	String exp = "";
    	for (CNFSubClause s: expression.getSubclauses()) {
    		i++;
    		if (expression.getSubclauses().size()==i) {
    			exp = exp  + s.printReturnAsString(true);
    		}
    		else {
    			exp = exp  + s.printReturnAsString(false);
    		}
    	}
    	return exp;
    }
    
}
