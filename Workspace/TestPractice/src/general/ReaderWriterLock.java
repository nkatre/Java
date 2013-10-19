package general;

import java.util.LinkedList;
import java.util.List;

/* Write a thread safe data structure such that there could be only one writer 
 * at a time but there could be n readers reading the data. 
 * You can consider that incrementing or decrementing a variable is an atomic operation.
 * If more than one threads try to write simultaneously then just select one randomly and let others wait
 * 
 * A readers-writer lock is like a mutex, in that it controls access to a shared resource, 
 * allowing concurrent access to multiple threads for reading but restricting access to a single thread for writes 
 * (or other changes) to the resource. 
 * A common use might be to control access to a data structure in memory that can't be updated atomically and 
 * isn't valid (and shouldn't be read by another thread) until the update is complete.
 * 
 */
public class ReaderWriterLock {
	
	List<String> writerQueue = new LinkedList<String>();
	List<String> readerQueue = new LinkedList<String>();
	
	private static Object resource;

	public List<String> getWriterQueue() {
		return writerQueue;
	}

	public void setWriterQueue(List<String> writerQueue) {
		this.writerQueue = writerQueue;
	}

	public List<String> getReaderQueue() {
		return readerQueue;
	}

	public void setReaderQueue(List<String> readerQueue) {
		this.readerQueue = readerQueue;
	}
	
	
	

}