package com.personaxio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;


public class DatabaseHelper {

	private static String VxConnectionString =  "";//"jdbc:sqlite:vx.db";
	private static String MxConnectionString =  "";
	
	
	public DatabaseHelper()
	{
		Properties properties = new Properties();
//		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		InputStream is;
		try {
			is = new FileInputStream("xio.properties");
			try {
				properties.load(is);
			 	is.close();
			 } catch (IOException ioe) {
				 System.out.print(ioe.getMessage());
			 }
			catch (Exception e){
				System.out.print(e.getMessage());
			}
			
			VxConnectionString= properties.getProperty("VxConnectionString");
			MxConnectionString= properties.getProperty("MxConnectionString");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}// this.getClass().getClassLoader().getResourceAsStream("com/personaxio/xio.properties");

	//	System.out.println(connectionString);
	}
	
	public int CreateVaultUserBatch(List<VaultUser> users) 
	{
		int recAdded = 0;
		String retval =  null;
		Connection con =  null;
		
		java.sql.PreparedStatement psVaultUser =  null;
		java.sql.PreparedStatement psLoginData =  null;
		String psVaultUserSql = "INSERT OR IGNORE INTO USERS (" +
				"UserID,"+
				"Email,"+
				"ForeName,"+
				"LastName,"+
				"Password,"+
				"DOB,"+
				"CCNumber,"+
				"CCExpiresMonth," +
				"CCExpiresYear," +
				"CCVNum," +
				"CCVendor,"+
				"BillingAddress1,"+
				"BillingAddress2,"+
				"Town,"+
				"County,"+
				"PostCode,"+
				"Country,"+
				"MobileNumber,"+
				"Consent," +
				"RegistrationDate)" +
				"Values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		String psAddLoginDataSql = "INSERT OR IGNORE INTO LoginData (" +
				"UserID," +
				"LoginSuccess," +
				"LoginAttempts ) " +
				"Values (?,?,?)";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat fmt =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			Class.forName("org.sqlite.JDBC");
			try {
				con = DriverManager.getConnection(VxConnectionString);
				con.setAutoCommit(false);
				
				psVaultUser =  con.prepareStatement(psVaultUserSql);
				psLoginData = con.prepareStatement(psAddLoginDataSql);
				for(VaultUser vaultUser : users)
				{
					psVaultUser.setString(1,vaultUser.getUserID());
					psVaultUser.setString(2,vaultUser.geteMail());
					psVaultUser.setString(3,vaultUser.getForeName());
					psVaultUser.setString(4,vaultUser.getLastName());
					psVaultUser.setString(5,vaultUser.getPassword());
					psVaultUser.setString(6,format.format(vaultUser.getDob()));
					psVaultUser.setString(7,vaultUser.getCreditCardNumber());
					psVaultUser.setInt(8,Integer.valueOf(vaultUser.getCcExpiresMonth()));
					psVaultUser.setInt(9,Integer.valueOf(vaultUser.getCcExpiresYear()));
					psVaultUser.setInt(10,Integer.valueOf(vaultUser.getCCVNumber()));
					psVaultUser.setString(11,vaultUser.getCcProvider());
					psVaultUser.setString(12,vaultUser.getAddress1());
					psVaultUser.setString(13,vaultUser.getAddress2());
					psVaultUser.setString(14,vaultUser.getTown());
					psVaultUser.setString(15,vaultUser.getCounty());
					psVaultUser.setString(16,vaultUser.getPostCode());
					psVaultUser.setString(17,vaultUser.getCountry());
					psVaultUser.setString(18,vaultUser.getMobileNumber());
					psVaultUser.setBoolean(19,vaultUser.isConsent());
					psVaultUser.setObject(20,fmt.format(vaultUser.getRegistrationDate()));
					psVaultUser.addBatch();
					

					psLoginData.setString(1,vaultUser.getUserID());
					psLoginData.setBoolean(2,true);
					psLoginData.setInt(3,0);
					psLoginData.addBatch();
				}
				
				int [] usersAdded = psVaultUser.executeBatch();
				int[] loginsAdded = psLoginData.executeBatch();
				for(int i= 0; i < usersAdded.length ; i++)
					if(usersAdded[i] == 1)
						recAdded++;
				con.commit();
				
			}
			catch (SQLException sqlExc)
			{

				try {
					con.rollback();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			finally
			{
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		
		return recAdded;
	}

	
	public int createExchangeUsersBatch(List<VaultUser> users)
	{
		int recAdded =  0;
		Connection con =  null;
		
		java.sql.PreparedStatement psExchangeUser =  null;
		java.sql.PreparedStatement psLoginData =  null;
		String psVaultUserSql = "INSERT INTO Users (" +
				"UserID,"+
				"Email,"+
				"ForeName,"+
				"LastName,"+
				"Password) " +
				"Values (?,?,?,?,?)";
		
		String psAddLoginDataSql = "INSERT INTO LoginData (" +
				"UserID," +
				"LoginSuccess," +
				"LoginAttempts ) " +
				"Values (?,?,?)";
		try {
			Class.forName("org.sqlite.JDBC");
			try {
				con = DriverManager.getConnection(MxConnectionString);
				con.setAutoCommit(false);
				psExchangeUser =  con.prepareStatement(psVaultUserSql);
				psLoginData = con.prepareStatement(psAddLoginDataSql);
				for(VaultUser vaultUser : users)
				{
					psExchangeUser.setString(1,vaultUser.getUserID());
					psExchangeUser.setString(2,vaultUser.geteMail());
					psExchangeUser.setString(3,vaultUser.getForeName());
					psExchangeUser.setString(4,vaultUser.getLastName());
					psExchangeUser.setString(5,vaultUser.getPassword());
					psExchangeUser.addBatch();
					

					psLoginData.setString(1,vaultUser.getUserID());
					psLoginData.setBoolean(2,true);
					psLoginData.setInt(3,0);
					psLoginData.addBatch();					
				}
				int [] usersAdded = psExchangeUser.executeBatch();
				int[] loginsAdded = psLoginData.executeBatch();
				for(int i= 0; i < usersAdded.length ; i++)
					if(usersAdded[i] == 1)
						recAdded++;
				con.commit();				
			}
			catch (SQLException sqlExc)
			{
				System.out.println("Sql exception" + sqlExc.getMessage());
				try {
					con.rollback();
				} catch (SQLException e) {
					System.out.println("Sql exception on Rollback" + e.getMessage());
				}
			}
			finally
			{
				try {
					con.close();
				} catch (SQLException e) {
					System.out.println("Sql exception on Close" + e.getMessage());
				}
			}			
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("SClass NOt Found Exception" + e.getMessage());
		}
		return recAdded;
		
	}
	
	public int AddOrdersBatch(List<MatchProps> mpList) throws SQLException
	{
		int recAdded = 0;
		Connection con =  null;
		
		java.sql.PreparedStatement psOffers =  null;
	//	java.sql.PreparedStatement psUpdateTradeStatus =  null;
		java.sql.PreparedStatement psMatchPropVals =  null;
		java.sql.PreparedStatement psGetLastAutoInc = null;
		
		String psOffersSQL= "INSERT INTO order_props(TradeDateTime,ExpireDateTime,BuyCpt," + 
				"SellCpt, " +
				"TradeStatus, " +
				"TradeType," +
				"Asset , " + 
				"Amount," +
				"Price," +
				"MatchTradeId," +
				"Vault,"+
				"mprop1,"+
				"mprop2,"+
				"mprop3,UserId)" +
				"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		String psMatchPropValsSQL = "INSERT INTO order_propvals (PropId,TradeId,PropName,PropVal) VALUES (?,?,?,?)";
		String psGetLAstAutoIncSQL = "SELECT seq from SQLITE_SEQUENCE WHERE name='order_props'";
		
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance(); 
		Date myTimestamp = calendar.getTime();
		String timeStamp = format.format(myTimestamp);
		
		try {
			Class.forName("org.sqlite.JDBC");
			try {
				con = DriverManager.getConnection(MxConnectionString);
				
				con.setAutoCommit(false);
				psOffers = con.prepareStatement(psOffersSQL);
				psMatchPropVals = con.prepareStatement(psMatchPropValsSQL);
				
				int autoIncValue = -1; // the value of the last autoincrement
				
				// Gets the resultset which contains the generated keys
				psGetLastAutoInc = con.prepareStatement(psGetLAstAutoIncSQL);
				
				ResultSet rs = psGetLastAutoInc.executeQuery();
				
				if(rs.next()) 
				{
					// this is the TradeId from the MatchProps table
				   autoIncValue = rs.getInt(1);
				   if(autoIncValue ==0)
					   autoIncValue= 1;
				   else
					   autoIncValue ++;
				}
				psGetLastAutoInc.close();
				
				for(MatchProps mp : mpList)
				{
					if(mp.TradeDateTime == null)
						timeStamp= format.format(myTimestamp);
					else
						timeStamp = format.format(mp.TradeDateTime);
					
					
					psOffers.setString(1,timeStamp);
					psOffers.setString(2,format.format(mp.ExpireDateTime));
					psOffers.setString(3, mp.BuyCpt);
					psOffers.setString(4, mp.SellCpt);
					psOffers.setString(5, mp.TradeStatus);
					psOffers.setString(6, mp.TradeType);
					psOffers.setString(7, mp.Asset);
					psOffers.setLong(8, mp.Amount);
					psOffers.setDouble(9, mp.Price.doubleValue());
					psOffers.setLong(10, mp.MatchTradeId);
					psOffers.setString(11, mp.Vault);
					psOffers.setString(12, mp.mprop1);
					psOffers.setString(13, mp.mprop2);
					psOffers.setString(14, mp.mprop3);
					psOffers.setString(15, mp.UserId);
					psOffers.addBatch();
					
					if(autoIncValue != -1)	
						{
							psMatchPropVals.setLong(1,autoIncValue);
							psMatchPropVals.setLong(2,autoIncValue);
							psMatchPropVals.setString(3,"LONDON 5");
							psMatchPropVals.setString(4,"");
							
							psMatchPropVals.addBatch();
							autoIncValue++;
					}
				}	

				int [] usersAdded = psOffers.executeBatch();
				int[] loginsAdded = psMatchPropVals.executeBatch();
				for(int i= 0; i < usersAdded.length ; i++)
					if(usersAdded[i] == 1)
						recAdded++;
				con.commit();
			}
			catch(SQLException e)
			{	
				con.rollback();
				e.printStackTrace();
			}
			finally
			{
				if(psOffers !=null)
					psOffers.close();
				if(psMatchPropVals != null)
					psMatchPropVals.close();
				if(psGetLastAutoInc != null)
					psGetLastAutoInc.close();
				if(con != null)
					con.close();
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return recAdded;
	}
	
	public int AddTradesBatchToVault(List<MatchProps> mProps, String tradeDateTime) throws SQLException
	{
		int retval = 0;
		Connection con =  null;
		SimpleDateFormat df =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		java.sql.PreparedStatement psTrades =  null;
		String psTradesSQL= "INSERT OR IGNORE INTO trades(TradeDateTime,ExpireDateTime,BuyCpt," + 
				"SellCpt, " +
				"TradeStatus, " +
				"TradeType," +
				"Asset , " + 
				"Amount," +
				"Price," +
				"MatchTradeId," +
				"Vault,"+
				"mprop1,"+
				"mprop2,"+
				"mprop3,UserId)" +
				"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			Class.forName("org.sqlite.JDBC");
			try {
					con = DriverManager.getConnection(VxConnectionString);
					
					con.setAutoCommit(false);
					psTrades = con.prepareStatement(psTradesSQL);
					
					for(MatchProps mp : mProps)
					{
						psTrades.setString(1, tradeDateTime);
						psTrades.setString(2,df.format(mp.ExpireDateTime));
						psTrades.setString(3, mp.BuyCpt);
						psTrades.setString(4, mp.SellCpt);
						psTrades.setString(5, mp.TradeStatus);
						psTrades.setString(6, mp.TradeType);
						psTrades.setString(7, mp.Asset);
						psTrades.setLong(8, mp.Amount);
						psTrades.setDouble(9, mp.Price.doubleValue());
						psTrades.setLong(10, mp.MatchTradeId);
						psTrades.setString(11, mp.Vault);
						psTrades.setString(12, mp.mprop1);
						psTrades.setString(13, mp.mprop2);
						psTrades.setString(14, mp.mprop3);
						psTrades.setString(15, mp.UserId);
						psTrades.addBatch();
					}
					
					int[]  recAdded = psTrades.executeBatch();
					con.commit();
					for(int i = 0; i < recAdded.length; i++)
						if(recAdded[i]==1)
							retval++;
				
				}
				catch (SQLException e)
				{
					con.rollback();
					System.out.println(e.getMessage());
				}
				finally 
				{
					if(psTrades != null)
						psTrades.close();
					if(con != null)
						con.close();
				}
			}
			catch (ClassNotFoundException cnfe)
			{
				System.out.println(cnfe.getMessage());	
			}
		
		return retval;
	}
}
