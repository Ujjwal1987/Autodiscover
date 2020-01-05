import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.Semaphore;

public class DeviceConnection implements Runnable {
	
	private Thread run;
	ServerSocket Dev_sock;
	BufferedReader br;
	BufferedWriter bw;
	String []dev_data;
	static Semaphore semaphore = new Semaphore(1);

	
	public DeviceConnection() {
		try {
			Dev_sock = new ServerSocket(4001);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		run = new Thread(this, "running");
		run.start();
	}



	public void run() {
		// TODO Auto-generated method stub
		Socket Dev_sock_input = null;
		while(true){
			try {
				Dev_sock_input = Dev_sock.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		Receive_String(Dev_sock_input);
		}		
	}



	private void Receive_String(Socket dev_sock_input) {
		// TODO Auto-generated method stub
		String dev_data_temp = null;
		int item_count=0;
		try {
			br=new BufferedReader(new InputStreamReader(dev_sock_input.getInputStream()));
			dev_data_temp=br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(dev_data_temp);
		dev_data = dev_data_temp.split(";");
		String ID = dev_data[0];
		String Group = dev_data[1];
		String Device_type = dev_data[2];
//		System.out.println(dev_data.length);
		for(int i=2; i<dev_data.length; i++){
			if(dev_data[i].isEmpty()){
				continue;
			}else{
				item_count++;
			}
		}
		String []item = null;
		item = (String[]) Arrays.copyOfRange(dev_data, 3, dev_data.length);
		DevicesSearch ds = new DevicesSearch(ID,Device_type,item_count, Group, item);
		try {
			semaphore.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ds.FileValidationCreation(ID, Device_type, item_count, Group, item);
		semaphore.release();
//		Thread.currentThread().interrupt();
	}


}

