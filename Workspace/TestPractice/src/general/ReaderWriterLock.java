package general;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
	
	private Queue<Writer> writerQueue;
	private Queue<Reader> readerQueue;
	
	private static Resource resource;
	private static Lock lock;
	
	private ResourceLockWraper resourceLock;
	
	public ReaderWriterLock(Resource r) {
		super();
		resource = r;
		lock = new Lock();  //VB : Use reentrant lock
		resourceLock = new ResourceLockWraper(resource, lock);
		writerQueue = new Queue<Writer>();
		readerQueue = new Queue<Reader>();
	}

	
	public void addProcess(Process process) {
		
		if(process.getClass().equals(Writer.class)) {
			writerQueue.enQueue((Writer) process);
		} else if (process.getClass().equals(Reader.class)) {
			readerQueue.enQueue((Reader) process);
		}
	}
	
	public boolean seekLock(Process requestor) {
		//VB: YOU should synchronize
		if(requestor.getClass().equals(Reader.class)) {
			
			//readerQueue.enQueue((Reader) requestor);
			
			if(isAvailableForLocking()) {
				return handOverLockToReaders();
			} else {
				return false;
			}
			
		} else if(requestor.getClass().equals(Writer.class)) {
			
			//writerQueue.enQueue((Writer) requestor);
			if(isAvailableForLocking()) {
				return handOverLockToNextWriter();
			} else {
				return false;
			}
			
		} else {
			System.out.println("request for either read or write lock. the vaue passed is NONE");
			return false;
		}
	}


	public boolean releaseLock(Process requestor) {
		// VB same here. all methods shud be protected
		// VB : YOU should chck if requeter really has the lock. 
		// VB : LOOk at example of ReaderWriterLock usage to uunderstand what interface to use.
		// VB : THis interface is not correct but given the interface, the implementation seems reasonable
		if(requestor.getClass().equals(Writer.class)) {
			
			resourceLock.lock.writeholder = null;
			resourceLock.lock.type = Lock.lockType.NONE;
			
			handOverLockToReaders();
			 
			return true;
			
		} else if(requestor.getClass().equals(Reader.class)) {
			
			List<Process> rholders = resourceLock.lock.readholders;
			
			if(rholders.contains(requestor)){
				rholders.remove(requestor);
				resourceLock.lock.readholders = rholders;
				
				if (resourceLock.lock.readholders.isEmpty()) {
					resourceLock.lock.type = Lock.lockType.NONE;
					handOverLockToNextWriter();
					return true;
				}
				return true;
				
			} else {
				return false;
			}
		}
		
		return false;
	}

	private boolean isAvailableForLocking() {
		if((resourceLock.lock.type == Lock.lockType.NONE)
				&& (null == resourceLock.lock.writeholder)
				&& (resourceLock.lock.readholders.isEmpty())) {
			return true;
		}
		return false;
	}
	
	private boolean handOverLockToNextWriter() {
		
		if(isAvailableForLocking()) {
			if(writerQueue.size()==0) {
				return false;
			} 
			Process writer = writerQueue.poll();
			resourceLock.lock.writeholder = writer;
			resourceLock.lock.type = Lock.lockType.WRITER;
			return true;
		}
		
		return false;
	}
	
	private boolean handOverLockToReaders() {
		
		if (isAvailableForLocking()) {
			
			if(readerQueue.size()==0)
				return false;
			
			resourceLock.lock.type = Lock.lockType.READER;
			
			while(readerQueue.size() !=0) {
				resourceLock.lock.readholders.add(readerQueue.poll());	
			}
			return true;
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return "ReaderWriterLock [writerQueue=" + writerQueue
				+ ", readerQueue=" + readerQueue + ", resourceLock="
				+ resourceLock + "]";
	}


	public static void main(String[] args) {
		Resource r = new Resource(1);
		ReaderWriterLock  rwl = new ReaderWriterLock(r);
		System.out.println(rwl + "\n");
		
		Reader r1 = rwl.new Reader(1);
		Reader r2 = rwl.new Reader(2);
		Writer w1 = rwl.new Writer(1);
		
		rwl.addProcess(r1);
		rwl.addProcess(r2);
		rwl.addProcess(w1);
		
		System.out.println(rwl + "\n");
		
		/*
		// test case 1
		System.out.println(rwl.seekLock(r1) + "-->" + rwl + "\n");
		System.out.println(rwl.releaseLock(r1) + "-->" + rwl + "\n");
		System.out.println(rwl.releaseLock(r2) + "-->" + rwl + "\n");
		System.out.println(rwl.releaseLock(w1) + "-->" + rwl + "\n");
		*/
		
		// test case 2
		System.out.println(rwl.seekLock(w1) + "-->" + rwl + "\n");
		System.out.println(rwl.seekLock(r1) + "-->" + rwl + "\n");
		System.out.println(rwl.releaseLock(w1) + "-->" + rwl + "\n");
		System.out.println(rwl.releaseLock(r1) + "-->" + rwl + "\n");
		System.out.println(rwl.seekLock(w1) + "-->" + rwl + "\n");
		System.out.println(rwl.releaseLock(r2) + "-->" + rwl + "\n");
		rwl.addProcess(w1);
		System.out.println(rwl.seekLock(w1) + "-->" + rwl + "\n");
	}

	private static class Lock {
	    
	    enum lockType { WRITER, READER, NONE }

		private lockType type = lockType.NONE;
	    private Process writeholder;
	    private List<Process> readholders = new LinkedList<Process>();
	    
	    private Lock() {
			super();
		}
		
		@Override
		public String toString() {
			return "Lock [type=" + type + ", holders=" + writeholder + ":" + readholders + "]";
		}

		public lockType getType() {
			return type;
		}

	}
	
	private static class ResourceLockWraper {
		private Resource resource;
		private Lock lock;
		
		public ResourceLockWraper(Resource resource, Lock lock) {
			super();
			this.resource = resource;
			this.lock = lock;
		}

		@Override
		public String toString() {
			return "ResourceLockWraper [resource=" + resource + ", lock="
					+ lock + "]";
		}
		
	}

	private static class Resource {
		private int id;
		
		public Resource(int id) {
			super();
			this.id = id;
		}

		public int getId() {
			return id;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + id;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Resource other = (Resource) obj;
			if (id != other.id)
				return false;
			return true;
		}
		

		@Override
		public String toString() {
			return "Resource [id=" + id + "]";
		}
		
		
	}

	private static interface Process{
		int getId();
		void setId(int id);
	}

	private class Writer implements Process {
		
		private int id;
		
		Writer(int id) {
			super();
			setId(id);
		}
	
		@Override
		public int getId() {
			return id;
		}
	
		@Override
		public void setId(int id) {
			this.id = id;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + id;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Writer other = (Writer) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (id != other.id)
				return false;
			return true;
		}

		private ReaderWriterLock getOuterType() {
			return ReaderWriterLock.this;
		}

		@Override
		public String toString() {
			return "Writer[" + id + "]";
		}

	}

	private class Reader implements Process {
		
		private int id;
		
		Reader (int id) {
			super();
			setId(id);
		}
	
		@Override
		public int getId() {
			return id;
		}
	
		@Override
		public void setId(int id) {
			this.id = id;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + id;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Reader other = (Reader) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (id != other.id)
				return false;
			return true;
		}

		private ReaderWriterLock getOuterType() {
			return ReaderWriterLock.this;
		}

		@Override
		public String toString() {
			return "Reader[" + id + "]";
		}
	}
}
