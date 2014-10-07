package com.personaxio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


public class XIO {
	static DatabaseHelper helper ;
	public static List<Path> filesToProcess =  new ArrayList<Path>();
	public static String xioFolder = "";
	public static String userFolder = "../personamx/PersonaMxWeb/Users";

	public static String Service = "";
	public static String VaultUserFolder = "";
	
	public static void main(String[] args) {

		OptionBuilder.withArgName("Service");
		OptionBuilder.hasArg();
		OptionBuilder.withDescription("Service type Exchange (MX) or Vault (DX)");
		Option numRecs = OptionBuilder.create("s");

		
		Options options = new Options();
		
		options.addOption(numRecs);
		
		CommandLineParser parser = new PosixParser();
		try {
			CommandLine line = parser.parse( options, args);
			if(line.hasOption("s"))
			{

				Service = line.getOptionValue('s');
			}
			else
			{
				Quit(options);
			}
		}
		catch (ParseException e)
		{
			System.out.println("Parse Exception : " + e.getMessage());
		}
		
		
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
			
			VaultUserFolder= properties.getProperty("VaultUserFolder");
			xioFolder = properties.getProperty("xioFolder");
			
			// Added 24 Sept 2014 
			// First make sure the xio folder structure is in place
			new File(xioFolder + "/loaded").mkdirs();
			new File(xioFolder + "/send").mkdirs();
			new File(xioFolder + "/sent").mkdirs();
			new File(VaultUserFolder).mkdirs();
			
		} catch (FileNotFoundException e1) {
			System.out.println(e1.getMessage());
			
		}
		
		
		helper =  new DatabaseHelper();
		filesToProcess = listFilesForFolder(xioFolder + "/sent");
		if(filesToProcess.isEmpty())
		{
			System.out.println("No Files to process");
		}
		else
		{
			for(Path p : filesToProcess)
			{
				try {
					processInputFile(p);
				} catch (IOException e) {	
					System.out.println(e.getMessage());
				}
			}
		}
		
	}
	private static List<Path> listFilesForFolder(String folder)
	{
		
		final List<Path>filesT4Processing = new ArrayList<Path>();
		try {
			Files.walkFileTree(Paths.get(folder), new SimpleFileVisitor<Path>() {
			    @Override
			    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			    	filesT4Processing.add( file);
			        return FileVisitResult.CONTINUE;
			    }
			});
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return filesT4Processing;
	}
	
	private static void processInputFile(Path path ) throws IOException
	{
		List<Assets> assetList =  new ArrayList<Assets>();
		List<VaultUser> vaultUsers =  new ArrayList<VaultUser>();
		List<MatchProps> trades =  new ArrayList<MatchProps>();
		List<MatchProps> initialOrders =  new ArrayList<MatchProps>();
		
		String dateString = "";
		try {
			BufferedReader reader =  Files.newBufferedReader(path, StandardCharsets.UTF_8);
			StringBuffer stringBuffer = new StringBuffer();
			String line = null;
			 
			while((line =reader.readLine())!=null){
			   stringBuffer.append(line).append("\n");
			}
			String json = stringBuffer.toString();
			reader.close();
			try {
				Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
				Map<String,Map<String,List<?>>> map  = new HashMap<String,Map<String,List<?>>>();
				map =  gson.fromJson(json, map.getClass());
				if(Service.contains("DX"))
				{
					// Processing is for  a Vault
					if(map.containsKey("Assets"))
					{
						System.out.println("Asset Files are not processed in the Vault - XIOQ will transfer them");
/*						Type typeOfHashMap=new TypeToken<Map<String,Map<String, List<Assets>>>>(){}.getType();
						Map<String,Map<String, List<Assets>>> deserializedMap = gson.fromJson(json,typeOfHashMap);
						for(String key: deserializedMap.keySet()){
	                    	Type typeOfHmVU = new TypeToken<Map<String,List<Assets>>>(){}.getType();
	                    	Map<String,List<Assets>> deserializedMap2 = deserializedMap.get(key);
	                    	for(String k2 : deserializedMap2.keySet())
	                    	{
	                    		dateString = k2.replace(":", "");
	                    		assetList.addAll((List<Assets>)deserializedMap2.get(k2));
	                    	}
						}*/
					}
					else if (map.containsKey("Users")){
						
						Type typeOfHashMap=new TypeToken<Map<String,Map<String, List<VaultUser>>>>(){}.getType();
						Map<String,Map<String, List<VaultUser>>> deserializedMap = gson.fromJson(json,typeOfHashMap);
						for(String key: deserializedMap.keySet()){
	                    	//Type typeOfHmVU = new TypeToken<Map<String,List<VaultUser>>>(){}.getType();
	                    	Map<String,List<VaultUser>> deserializedMap2 = deserializedMap.get(key);
	                    	for(String k2 : deserializedMap2.keySet())
	                    	{
	                    		dateString = k2.replace(":", "");
	                    		vaultUsers.addAll(deserializedMap2.get(k2));
	                    	}
						}
						if(vaultUsers.size() > 0)
						{
							//int retval =  helper.createExchangeUsersBatch(vaultUsers);
							for (VaultUser vUser : vaultUsers)
							{
								System.out.println("Creating Vault Folders for " + vUser.geteMail());
								String filePath= VaultUserFolder+ "/" + vUser.getUserID() + "/Data/Demographics";
								new File(filePath).mkdirs();
								filePath= VaultUserFolder+ "/" + vUser.getUserID() + "/Data/Uploads";
								new File(filePath).mkdirs();
								filePath=VaultUserFolder+ "/" + vUser.getUserID() + "/Statements";
								new File(filePath).mkdirs();

							}
						}
					}
					else if (map.containsKey("Trades"))
					{
						Type typeOfHashMap=new TypeToken<Map<String,Map<String, List<MatchProps>>>>(){}.getType();
						Map<String,Map<String, List<MatchProps>>> deserializedMap = gson.fromJson(json,typeOfHashMap);
						for(String key: deserializedMap.keySet()){
	                    	//Type typeOfHmVU = new TypeToken<Map<String,List<MatchProps>>>(){}.getType();
	                    	Map<String,List<MatchProps>> deserializedMap2 = deserializedMap.get(key);
	                    	for(String k2 : deserializedMap2.keySet())
	                    	{
	                    		dateString = k2.replace(":", "");
	                    		trades.addAll(deserializedMap2.get(k2));
	                    		int retval = helper.AddTradesBatchToVault(trades, k2);
	                    		System.out.println("Added " + retval + " Trades to the Vault");	                    		
	                    	}
						}
						if(trades.size() > 0)
						{
							Calendar cal =  Calendar.getInstance();
							SimpleDateFormat format =  new SimpleDateFormat("dd-MM-yyyy");
							for(MatchProps mp : trades)
							{
								String text = "Sold " + mp.Amount + " of " + mp.Asset + " to " + mp.BuyCpt + " for " + mp.Price;
                        		try {
                        			//new File(VaultUserFolder + "/Statements/" + mp.UserId).mkdirs();
                        			File f =  new File(VaultUserFolder +"/"+ mp.UserId + "/Statements/Statement" + format.format(cal.getTime()) + ".txt");
                        			Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f.getCanonicalPath()), "utf-8")); 
                        		
                        		    writer.write(text);
                        		    writer.flush();
                        		    writer.close();
                        		    System.out.println("Statement created for " + mp.UserId );

                        		} catch (IOException ex){
                        		    System.out.println(ex.getMessage());
                        		} 								
								System.out.println("Notifying " + mp.UserId+ " of a Trade match");
							}
						}
					}
					
				}
				// This is the processing for the Exchange 
				else if (Service.contains("MX"))
				{
					if(map.containsKey("Assets"))
					{
						Type typeOfHashMap=new TypeToken<Map<String,Map<String, List<Assets>>>>(){}.getType();
						Map<String,Map<String, List<Assets>>> deserializedMap = gson.fromJson(json,typeOfHashMap);
						for(String key: deserializedMap.keySet()){
	                    	//Type typeOfHmVU = new TypeToken<Map<String,List<Assets>>>(){}.getType();
	                    	Map<String,List<Assets>> deserializedMap2 = deserializedMap.get(key);
	                    	for(String k2 : deserializedMap2.keySet())
	                    	{
	                    		dateString = k2.replace(":", "");
	                    		assetList.addAll(deserializedMap2.get(k2));
	                    		
	                    	}
						}
						if(assetList.size() > 0)
						{
							for(Assets a : assetList)
							{
								String uid =  a.prosumerId;
								String asset = a.Asset;
								Map<String,List<String>> userMap =  new HashMap<String,List<String>>();
                            	List<String> products =  new ArrayList<String>();
                            	products.add(asset);
                            	userMap.put("Assets", products);
                            	gson =  new Gson();
                            	String jsonProd =  gson.toJson(userMap);
                        		try {
                        			new File(userFolder + "/" + uid).mkdirs();
                        			File f =  new File(userFolder + "/" + uid + "/assets.json");
                        			Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f.getCanonicalPath()), "utf-8")); 
                        		
                        		    writer.write(jsonProd);
                        		    writer.flush();
                        		    writer.close();
                        		    System.out.println("Assets file Created in " + f.getCanonicalPath());

                        		} catch (IOException ex){
                        		    System.out.println(ex.getMessage());
                        		} 								
							}
						}
					}	
					else if(map.containsKey("Orders"))
					{
						// This is an initial offer to be put in the Exchange - comes from the PersonaMX UI.
						Type typeOfHashMap=new TypeToken<Map<String,Map<String, List<MatchProps>>>>(){}.getType();
						Map<String,Map<String, List<MatchProps>>> deserializedMap = gson.fromJson(json,typeOfHashMap);
						for(String key: deserializedMap.keySet()){
	                    	//Type typeOfHmVU = new TypeToken<Map<String,List<MatchProps>>>(){}.getType();
	                    	Map<String,List<MatchProps>> deserializedMap2 = deserializedMap.get(key);
	                    	for(String k2 : deserializedMap2.keySet())
	                    	{
	                    		dateString = k2.replace(":", "");
	                    		initialOrders.addAll(deserializedMap2.get(k2));
	                    		int retval = helper.AddOrdersBatch(initialOrders);
	                    		System.out.println("Added " + retval + " initial orders to Exchange");
	                    	}
						}							
					}
					else if(map.containsKey("Users"))
					{
						Type typeOfHashMap=new TypeToken<Map<String,Map<String, List<VaultUser>>>>(){}.getType();
						Map<String,Map<String, List<VaultUser>>> deserializedMap = gson.fromJson(json,typeOfHashMap);
						for(String key: deserializedMap.keySet()){
	                    	//Type typeOfHmVU = new TypeToken<Map<String,List<VaultUser>>>(){}.getType();
	                    	Map<String,List<VaultUser>> deserializedMap2 = deserializedMap.get(key);
	                    	for(String k2 : deserializedMap2.keySet())
	                    	{
	                    		dateString = k2.replace(":", "");
	                    		vaultUsers.addAll(deserializedMap2.get(k2));
	                    	}
						}
						if(vaultUsers.size() > 0)
						{
							int retval =  helper.createExchangeUsersBatch(vaultUsers);
							System.out.println(retval +" new users created in the Exchange database");
						}						
					}
					else if (map.containsKey("Trades"))
					{
						System.out.println("Trades are not processed on the Exchange - they will be moved by XIOQ for processing");
					}
				}
				
			}
			catch (Exception e)
			{
				System.out.println(e.getMessage());
			}
			
		}
		catch (FileNotFoundException e) {
			System.out.println(e.getLocalizedMessage());
		}
	}

	
	private static void Quit(Options options)
	{
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp( "XIO", options , true);
		
		System.exit(1);
	}
}
