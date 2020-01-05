import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import org.apache.commons.lang3.ArrayUtils;


public class DevicesSearch {
	
	private String Device_ID, Device_type;
	private int Item_count;
	private String Group_name;
	private String Item_Names[] = new String [Item_count];
	

	public DevicesSearch(String device_ID, String device_type, int item_count,
			String group_name, String[] item_Names) {
		super();
//		System.out.println("hello");
		Device_ID = device_ID;
		setDevice_type(device_type);
		Item_count = item_count;
		Group_name = group_name;
		Item_Names = item_Names;
	}


	public String getDevice_ID() {
		return Device_ID;
	}


	public void setDevice_ID(String device_ID) {
		Device_ID = device_ID;
	}


	public int getItem_count() {
		return Item_count;
	}


	public void setItem_count(int item_count) {
		Item_count = item_count;
	}


	public String getGroup_name() {
		return Group_name;
	}


	public void setGroup_name(String group_name) {
		Group_name = group_name;
	}


	public String[] getItem_Names() {
		return Item_Names;
	}


	public void setItem_Names(String[] item_Names) {
		Item_Names = item_Names;
	}
	
	public void FileValidationCreation(String ID, String Device_type, int item_count, String group, String[] item) {
		// TODO Auto-generated method stub
		File items = new File("/etc/openhab2/items/Devices.items");
		if(items.exists()){
//			System.out.println("File Exist");
			additems(ID, Device_type, item_count, group, item);
		}else{
			try {
				items.createNewFile();
				RandomAccessFile raf = new RandomAccessFile("/etc/openhab2/items/Devices.items", "rw");
				raf.writeBytes("//MainGroup\n");
				raf.writeBytes("//Rooms\n");
				raf.writeBytes("//Channels\n");
				raf.close();
				additems(ID, Device_type, item_count, group, item);
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void additems(String ID, String Device_type, int item_count, String group, String[] item){
		String temp = null;

		int flag =0;
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile("/etc/openhab2/items/Devices.items", "rw");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			temp = raf.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
//		System.out.println(temp);
		if(temp.equals("//MainGroup")){
			while(true){
				try {
					temp=raf.readLine();
					if(temp.startsWith("Group")){
						flag++;
						break;
					}else if(temp.equals("//Rooms")){
						
						break;
					}else if(temp.equals("//Channels")){
						
						break;
					}else{
						
						break;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} 
		else{
			System.out.println("incorrect file");
		}
//		System.out.println(flag);
		if(flag>=1){
//			System.out.println("Home configuration exist");
			try {
				raf.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			additemsexistingfile(ID, Device_type, item_count, group, item);
		}else{
			try {
				raf.seek(0);
				raf.readLine();
				/*writer = new FileWriter("Devices.items", true);
				reader = new FileReader("Devices.items");
				br = new BufferedReader(reader);
				int i = br.readLine().length();*/
				raf.writeBytes("Group\tHome\t\"Home\"\t<house>\n");
				raf.writeBytes("//Rooms\n");
				/*raf.writeBytes("//Channels\n");
				raf.close();
				raf=new RandomAccessFile("Devices.items", "rw");
				raf.seek(0);
				while(true){
					//System.out.println(raf.readLine());
					String temp1= raf.readLine();
					if(temp1.equals("//Rooms\n")){
						break;
					}else{
						continue;
					}
				}*/
				temp=group;
				temp.replace(" ", "");
				raf.writeBytes("Group\t"+group+"\t"+"\""+temp+"\"\t<office>\t(Home)\n");
				raf.writeBytes("//Channels\n");
				switch(Device_type){
				case "Lighting":
					for(int i=0; i<item.length; i++){
						if(item[i].isEmpty()){
							continue;
						}else{
							String itemtemp = item[i];
							itemtemp.replace(" ", "");
							String channeltemp;
							channeltemp = "Switch\t"+itemtemp+ "\t\"" + item[i] + "\"\t<light>\t("+temp+",g"+itemtemp+")\t[\""+Device_type+"\"]\t{mqtt=\">[mosquitto:/"+ID+"/:command:OFF:L"+Integer.toString(i+1)+"0],>[mosquitto:/"+ID+"/:command:ON:L"+Integer.toString(i+1)+"1]\"}\n";
							raf.writeBytes(channeltemp);
						}
					}
					raf.close();
					break;
				}
				/*int j=temp.length();
				writer.write(temp, i-1, j-1);*/
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sitemapfile(group, "");
		}

		return;
	}
	
	public void additemsexistingfile(String ID, String Device_type, int item_count, String group, String[] item){
		FileReader reader; 
		//FileWriter writer = null;
		RandomAccessFile raf = null;
		BufferedReader br = null;
		String readtemp = "", temp=null;
		String[] readtempfinal;
		String groupfile = null;
		int startindex = 0, endindex = 0;
		int flag=0, i=0;
		try {
			reader = new FileReader("/etc/openhab2/items/Devices.items");
			br=new BufferedReader(reader);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		try {
			while((temp=br.readLine()) != null){
				if(flag==0){
					readtemp = temp;
					flag=1;
				}else{
					readtemp = readtemp + ";" + temp;
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		readtempfinal = readtemp.split(";");
		try {
			br.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
//		System.out.println(ID);
//		System.out.println(readtempfinal.length);
		flag=0;
		for(i=0;i<readtempfinal.length;i++){
			if(readtempfinal[i].contains(ID)){
				startindex = readtempfinal[i].indexOf("(")+1;
				endindex = readtempfinal[i].indexOf(",");
				groupfile = readtempfinal[i].substring(startindex, endindex);
				flag++;
			}
		}
		sitemapfile(group, groupfile);
		if(flag>0){
			while(true){
				if(flag>0){
					for(i=0;i<readtempfinal.length;i++){
						if(readtempfinal[i].contains(ID)){
							readtempfinal = ArrayUtils.remove(readtempfinal, i);
							flag--;
							break;
						}else{
					continue;
						}
				}
			}else{
				for(i=0;i<readtempfinal.length;i++){
					if(readtempfinal[i].contains(groupfile)){
						readtempfinal = ArrayUtils.remove(readtempfinal, i);
					}
				}
				break;
			}
			}
			for(i=0;i<readtempfinal.length;i++){
//				System.out.println(readtempfinal[i]);
			}
			PrintWriter writer = null;
			try {
				writer = new PrintWriter("/etc/openhab2/items/Devices.items");
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			writer.print("");
			writer.close();
			try {
				raf = new RandomAccessFile("/etc/openhab2/items/Devices.items", "rw");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(i=0;i<readtempfinal.length;i++){
				if(readtempfinal[i].equals("//Channels") && i==readtempfinal.length-1){
					try {
						temp=group;
						temp.replace(" ", "");
						raf.writeBytes("Group\t"+group+"\t"+"\""+temp+"\"\t<office>\t(Home)\n");
						raf.writeBytes(readtempfinal[i]);
						raf.writeBytes("\n");
						for(int j=0; j<item.length; j++){
							if(item[j].isEmpty()){
								continue;
							}else{
								String itemtemp = item[j];
								itemtemp.replace(" ", "");
								String channeltemp;
								channeltemp = "Switch\t"+itemtemp+ "\t\"" + item[j] + "\"\t<light>\t("+temp+",g"+itemtemp+")\t[\""+Device_type+"\"]\t{mqtt=\">[mosquitto:/"+ID+"/:command:OFF:L"+Integer.toString(j+1)+"0],>[mosquitto:/"+ID+"/:command:ON:L"+Integer.toString(j+1)+"1]\"}\n";
								try {
									raf.writeBytes(channeltemp);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else if(readtempfinal[i].equals("//Channels")){
					temp=group;
					temp.replace(" ", "");
					try {
						raf.writeBytes("Group\t"+group+"\t"+"\""+temp+"\"\t<office>\t(Home)\n");
						raf.writeBytes(readtempfinal[i]);
						raf.writeBytes("\n");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}else if(i==readtempfinal.length-1){
					try {
						raf.writeBytes(readtempfinal[i]);
						raf.writeBytes("\n");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					for(int j=0; j<item.length; j++){
						if(item[j].isEmpty()){
							continue;
						}else{
							String itemtemp = item[j];
							itemtemp.replace(" ", "");
							String channeltemp;
							channeltemp = "Switch\t"+itemtemp+ "\t\"" + item[j] + "\"\t<light>\t("+temp+",g"+itemtemp+")\t[\""+Device_type+"\"]\t{mqtt=\">[mosquitto:/"+ID+"/:command:OFF:L"+Integer.toString(j+1)+"0],>[mosquitto:/"+ID+"/:command:ON:L"+Integer.toString(j+1)+"1]\"}\n";
							try {
								raf.writeBytes(channeltemp);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}else{
					try {
						raf.writeBytes(readtempfinal[i]);
						raf.writeBytes("\n");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			try {
				raf.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			PrintWriter writer = null;
			try {
				writer = new PrintWriter("/etc/openhab2/items/Devices.items");
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			writer.print("");
			writer.close();
			try {
				raf = new RandomAccessFile("/etc/openhab2/items/Devices.items", "rw");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(i=0;i<readtempfinal.length;i++){
				if(readtempfinal[i].equals("//Channels")){
					try {
						temp=group;
						temp.replace(" ", "");
						raf.writeBytes("Group\t"+group+"\t"+"\""+temp+"\"\t<office>\t(Home)\n");
						raf.writeBytes(readtempfinal[i]);
						raf.writeBytes("\n");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else if(i==readtempfinal.length-1){
					try {
						raf.writeBytes(readtempfinal[i]);
						raf.writeBytes("\n");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					for(int j=0; j<item.length; j++){
						if(item[j].isEmpty()){
							continue;
						}else{
							String itemtemp = item[j];
							itemtemp.replace(" ", "");
							String channeltemp;
							channeltemp = "Switch\t"+itemtemp+ "\t\"" + item[j] + "\"\t<light>\t("+temp+",g"+itemtemp+")\t[\""+Device_type+"\"]\t{mqtt=\">[mosquitto:/"+ID+"/:command:OFF:L"+Integer.toString(j+1)+"0],>[mosquitto:/"+ID+"/:command:ON:L"+Integer.toString(j+1)+"1]\"}\n";
							try {
								raf.writeBytes(channeltemp);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}else{
					try {
						raf.writeBytes(readtempfinal[i]);
						raf.writeBytes("\n");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			try {
				raf.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sitemapfile(group, "");
		}
		return;
	}


	public String getDevice_type() {
		return Device_type;
	}


	public void setDevice_type(String device_type) {
		Device_type = device_type;
	}
	
	public void sitemapfile(String group, String groupfile){
		String[] sitemaptemp;
		FileReader reader = null;
		BufferedReader br;
		String temp = null, sitemapstring = null;
		File sitemap = new File("/etc/openhab2/sitemaps/Home.sitemap");
		int flag = 0;
		RandomAccessFile raf = null;
		if(sitemap.exists()){
			try {
				reader = new FileReader("/etc/openhab2/sitemaps/Home.sitemap");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			br = new BufferedReader(reader);
			try {
				while((temp=br.readLine()) != null){
					if(flag==0){
						sitemapstring = temp;
						flag=1;
					}else{
						sitemapstring = sitemapstring + ";" + temp;
					}

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sitemaptemp = sitemapstring.split(";");
			System.out.println(sitemaptemp.length);
			for(int i = 0; i<sitemaptemp.length; i++){
				if(groupfile.equals("")){
					continue;
				}else if(sitemaptemp[i].contains(groupfile)){
					sitemaptemp = ArrayUtils.remove(sitemaptemp, i);
				}
			}
			try {
				raf = new RandomAccessFile("/etc/openhab2/sitemaps/Home.sitemap", "rw");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			flag =0;
			PrintWriter writer = null;
			try {
				writer = new PrintWriter("/etc/openhab2/sitemaps/Home.sitemap");
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			writer.print("");
			writer.close();
			for(int i=0; i< sitemaptemp.length; i++){
				if(flag==0){
					if(sitemaptemp[i].contains("}")){
						try {
							raf.writeBytes("        Group item="+group+"\n");
							raf.writeBytes(sitemaptemp[i]);
							raf.writeBytes("\n");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						flag=1;
					}else{
					try {
						raf.writeBytes(sitemaptemp[i]);
						raf.writeBytes("\n");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}else{
				try {
					raf.writeBytes(sitemaptemp[i]);
					raf.writeBytes("\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
			try {
				raf.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			try {
				sitemap.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				raf = new RandomAccessFile("/etc/openhab2/sitemaps/Home.sitemap", "rw");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				raf.writeBytes("sitemap Home label=\"Home\" {\n");
				raf.writeBytes("    Frame {\n");
				raf.writeBytes("        Group item="+group+"\n"); 
				raf.writeBytes("    }\n}");
				} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				raf.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return;
	}
	
}
