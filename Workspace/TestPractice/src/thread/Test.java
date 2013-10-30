package thread;

public class Test  implements Runnable {
	
	
	public static void main(String[] args) {
		Test m = new Test();
		Thread t = new Thread(m);
		t.setName("t");
		Thread t1 = new Thread(m);
		t1.setName("t1");
		
		t.start();
		//Thread.yield();
		t1.start();
		//t.interrupt();
		try {
			t.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		t1.notify();
	}
	
	public void run() {
		System.out.println(Thread.currentThread().getName() + " is running");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			System.out.println(Thread.currentThread().getName() + " interrupted...");
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + " finished");
	}
	
}
