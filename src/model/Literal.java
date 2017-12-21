

package model;

/*
 * Represents a literal; a variable
 */
public class Literal implements Comparable<Literal>
{
    //The name of the literal
    private String Name;
    //Whether or not the literal is negated; if negation is true then it is negated
    private boolean negation;
    private boolean visited;
    
    public Literal(String n, boolean neg)
    {
        this.Name = n;
        this.negation = neg;
    }
    
    public Literal(String n, boolean neg, boolean visited)
    {
        this.Name = n;
        this.negation = neg;
        this.visited = visited;
    }
    
    public void print()
    {
        if(negation)
            System.out.println("NOT_" + Name);
        else
            System.out.println(Name);
    }
    
    public String printReturnAsString() {
    	if(negation)
            return "NOT_" + Name;
        else
            return Name;
    }
    
    public void printLinear()
    {
        if(negation)
            System.out.print("NOT_" + Name);
        else
            System.out.print(Name);
    }
    
    public void setName(String n)
    {
        this.Name = n;
    }
    
    public String getName()
    {
        return this.Name;
    }
    
    public void setNeg(boolean b)
    {
        this.negation = b;
    }
    
    public boolean getNeg()
    {
        return this.negation;
    }
    
    public boolean getVisited() {
		return visited;
	}
	
    public void setVisited(boolean visited) {
		this.visited = visited;
	}
	
    //Override
    public boolean equals(Object obj)
    {
        Literal l = (Literal)obj;

        if(l.getName().compareTo(this.Name) ==0 && l.getNeg() == this.negation)
        {
            return true;
        }
        else
        {
            return false;
        }

    }
	
    //@Override
    public int hashCode()
    {
            if(this.negation)
            {
                return this.Name.hashCode() + 1;
            }
            else
            {
                return this.Name.hashCode() + 0;                        
            }
    }
	
    //@Override
    public int compareTo(Literal x)
    {
            int a = 0;
            int b = 0;
            
            if(x.getNeg())
                a = 1;
            
            if(this.getNeg())
                b = 1;
            
            return x.getName().compareTo(Name) + a-b;
    }    
}
