package redo;

public class PractiseDeadLockUsingThread {
	
	
	private static class R implements Runnable {
		Object o1;
		Object o2;
		
		public R(Object o1, Object o2) {
			this.o1 = o1;
			this.o2 = o2;
		}
		
		@Override
		public void run() {
			synchronized (o1) {
				System.out.println(Thread.currentThre)��[uio0-];
			}
			synchronized (o2) {
				System.out.println(Thread.);
			}
		}
		
		
	}

}
