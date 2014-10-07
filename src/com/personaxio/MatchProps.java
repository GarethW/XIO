package com.personaxio;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A class to hold the values for a match_prop
 * @author Gareth White
 * @version 1.0.0
 */
public class MatchProps  {

	public int TradeId;
	public Timestamp TradeDateTime;
	public Timestamp ExpireDateTime; 
	public String BuyCpt;
	public String SellCpt;
	public String TradeStatus;
	public String TradeType;
	public String Asset;
	public int Amount;
	public BigDecimal Price;
	public int MatchTradeId;
	public String Vault;
	public String mprop1;
	public String mprop2;
	public String mprop3;
	public String mprop4;
	public String mprop5;
	public String UserId;
	
	
	public MatchProps()
	{
		
	}
	
	/**
	 * Clones this MatchProps
	 */
/*    public MatchProps clone() throws CloneNotSupportedException {
        return (MatchProps) super.clone();
    }*/

	
	 /**
	  * Creates a string built up from the Fields and their values.  Each field/value pair is separated by a colon on a seperate line. 
	  */
/*	  @Override 
	  public String toString() 
	  {
		    StringBuilder result = new StringBuilder();
		    String newLine = System.getProperty("line.separator");
	

		    result.append("{");
		    //determine fields declared in this class only (no fields of superclass)
		    Field[] fields = this.getClass().getDeclaredFields();
		
		    //print field names paired with their values
		    for ( Field field : fields  ) 
		    {
			      result.append(" ");
			      try {
						result.append( field.getName() );
						result.append(": ");
			        
				        // Some of the fields are Date so these need to be formatted
				        Object o = field.get(this);
				        if(o != null)
					        {
					        if(o.getClass().equals(java.util.Date.class))
						        {
					        		SimpleDateFormat fmt =  new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
						        	result.append( fmt.format(o) ).append(",");
						        }
					        else
					        	result.append(o).append(",");
				        }
				        else 
				        	result.append("NULL");
				        //requires access to private field:
				        //result.append( field.get(this) );
			      }
			      catch ( IllegalAccessException ex ) {
			        System.out.println(ex);
			      }
			     // result.append(newLine);
		    }
		    result.append("}");
		   
		    return result.toString();
	  }*/

/*	@Override
	public int compareTo(MatchProps other) {
		// TODO Auto-generated method stub
		return this.TradeDateTime.compareTo(other.TradeDateTime);
	}
*/

	 

}
