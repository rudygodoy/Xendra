package org.simoes.util;

import org.compiere.util.CLogger;

/**
 * The ThreadPool will process all requests thrown into the queue.
 * If keepRunning is set to false, the Threads will continue to run until
 * the queue is depleted.
 * 
 * @author Chris Simoes
 */
public class ThreadPoolThread extends Thread  {
    // all files need a static Category object for error logging
	static CLogger log = CLogger.getCLogger(ThreadPoolRequest.class);
    private final static String className = "ThreadPoolThread ";

    private int poolObjectsSize;
    private boolean keepRunning = true;
        
    	
    // protected package variables //
    ThreadPool poolRef;
    int threadNumber;
	boolean eventsStillFiring = true;
		
	ThreadPoolThread(ThreadPool pool, int i) {
		super(className + i);
		this.poolRef = pool;
		this.threadNumber = i;
	}

    public void run() {
        final String METHOD_NAME = "Thread[" + threadNumber + "] run():";
		ThreadPoolRequest obj = null;
		while (keepRunning) {
			try {
				synchronized(poolRef.objects) {
					poolObjectsSize = poolRef.objects.size();
					if(poolObjectsSize > 0) {
						obj = (ThreadPoolRequest) poolRef.objects.elementAt(0);
						poolRef.objects.removeElementAt(0);
					} else {
                        try  {
                            if (eventsStillFiring || (poolObjectsSize > 0))  {
                                //if pool.objects is empty wait until it has something in it
	                            poolRef.objects.wait(); 
                            }
                        } catch(InterruptedException e) {
                            log.warning(METHOD_NAME + e.getMessage());
                        }
					}
				}
			}
			catch (ClassCastException e) {
			    log.severe(METHOD_NAME + "ThreadPool contined an object other than ThreadPoolRequest.");
                log.severe(METHOD_NAME + e.getMessage());
				obj = null;
			}

			if(obj != null)  {
			   obj.target.run();
			}

			if (!eventsStillFiring && (poolObjectsSize == 0))  {
			   keepRunning = false;
			   return;
			}
			obj = null;
		}
	}
	
	protected final void setEventsStillFiring(boolean b)  {
	   eventsStillFiring = b;
	}
}
