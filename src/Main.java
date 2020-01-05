
public class Main implements Runnable {

	public static void main(String[] args) {
		DeviceConnection dc = new DeviceConnection();
		Thread t1 = new Thread(dc);
		t1.start();
		// TODO Auto-generated constructor stub
	}

	public void run() {
		// TODO Auto-generated method stub
		
	}

}
