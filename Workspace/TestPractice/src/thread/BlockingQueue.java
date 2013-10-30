package thread;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

/**
 * Done with BALaji
 * @author ksjeyabarani
 *
 */
public class BlockingQueue {

	public static Logger log = Logger.getLogger(BlockingQueue.class.getName());
	
	private final int size;

	private List list = new LinkedList();

	private volatile int listSize = 0;

	public BlockingQueue(int size) {
		this.size = size;
	}

	public synchronized void enQueue(Object obj) {
		if (null == obj)
			return;

		while(listSize >= size) {
			try {
				this.wait();
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		list.add(obj);
		listSize++;
		notify();
	}

	public synchronized  Object deQueue() {
		
		while(listSize <= 0) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		listSize--;
		Object o = list.remove(0);
		
		notify();
		
		return o;
	}

	public static void main(String[]  args) {
		BlockingQueue q = new BlockingQueue(2);
		Thread t1 = new Thread();
		
	}
	
	
}


