package redo;

public class PractiseSimpleProducerConsumer {
	
	private static Object o =  new Object();
	
	public static void main(String[] args)  {
		Producer p = new Producer(o);
		Consumer c1 = new Consumer(o);
		Consumer c2 = new Consumer(o);
		
		Thread producer = new Thread(p);
		Thread consumer1 = new Thread(c1);
		Thread consumer2 = new Thread(c2);
		
		consumer1.start();
		consumer2.start();
		producer.start();
		producer = new Thread(p);
		producer.start();
	}
	
	private static class Producer implements Runnable {
		
		Object o;
		
		public Producer(Object oo) {
			o = oo;
		}

		@Override
		public void run() {
			synchronized(o) {
				System.out.println(Thread.currentThread().getName() + " produced at and notified at: " +  System.currentTimeMillis());
				o.notify();
			}
		}
		
	}
	
	private static class Consumer implements Runnable {

		Object o;
		
		public Consumer(Object oo) {
			o = oo;
		}
		
		@Override
		public void run() {
			synchronized(o) {
				try {
					o.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(Thread.currentThread().getName() + " consumed " + o + " at: " +  System.currentTimeMillis());
			}
			
		}
		
	}
}
