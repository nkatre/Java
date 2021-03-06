package redo;

public class PractiseThreadJoin {
	
	public static void main(String[] s) throws InterruptedException {
		MyR r = new MyR();
		Thread t1 = new Thread(r);
		Thread t2 = new Thread(r);
		t1.start();
		t2.start();
		t1.notify();
		t2.notify();
		t1.join();
		t2.join();
	}
	
	private static class MyR implements Runnable {
		public void run() {
			System.out.println(Thread.currentThread().getName() + " started at " + System.currentTimeMillis());
			this.notify();
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName() + " finished at " + System.currentTimeMillis());			
		}
	}
}
