package sony.sdk.cameraremote.utils.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by aaron on 7/10/16.
 */

public class ThreadPoolHelper {

    private static ThreadPoolHelper mThreadPoolHelper = null;
    private ExecutorService executorService = null;

    private ThreadPoolHelper(){
        executorService = Executors.newCachedThreadPool();
    }

    public static ThreadPoolHelper getInstance(){
        if(mThreadPoolHelper == null){
            mThreadPoolHelper = new ThreadPoolHelper();
        }
        return mThreadPoolHelper;
    }


    public ExecutorService getExecutorService() {
        return executorService;
    }

    /**
     * Shutdown the executor service and make sure the current thread shuts down.
     */
    public void shutdownNow(){
        executorService.shutdown();
        executorService.shutdownNow();
        Thread.currentThread().interrupt();
        executorService = null;
    }
}
