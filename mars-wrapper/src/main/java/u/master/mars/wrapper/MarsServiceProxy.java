package u.master.mars.wrapper;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.tencent.mars.app.AppLogic;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by master_c on 2017/5/25.
 */

public class MarsServiceProxy implements ServiceConnection {

    private static final String SERVICE_CLASSNAME = "u.master.mars.wrapper.MarsService";

    private static MarsServiceProxy sInstance;
    private static Context sContext;

    private static String sPackageName;
    private static String sClassName;

    private LinkedBlockingQueue<ITaskInterface> mQueue = new LinkedBlockingQueue<>();

    private IMarsInterface mMarsInterface;

    private IPushInterface mPushInterface = new PushStub();

    private AppLogic.AccountInfo mAccountInfo;

    private MarsServiceProxy() {
        WorkThread thread = new WorkThread();
        thread.start();
    }

    public static void init(Context context, String packageName) {
        if (sInstance != null) {
            return;
        }
        sContext = context.getApplicationContext();
        sPackageName = packageName == null ? context.getPackageName() : packageName;
        sClassName = SERVICE_CLASSNAME;
        sInstance = new MarsServiceProxy();
    }

    public static void send(ITaskInterface taskInterface) {
        sInstance.mQueue.offer(taskInterface);
    }

    public static void cancel(ITaskInterface taskInterface) {
        sInstance.removeTask(taskInterface);
    }

    public static void setForeground(boolean isForeground) {
        if (sInstance.mMarsInterface == null) {
            Intent intent = new Intent().setClassName(sPackageName, sClassName);
            sContext.startService(intent);
            sContext.bindService(intent, sInstance, Service.BIND_AUTO_CREATE);
            return;
        }
        try {
            sInstance.mMarsInterface.setForeground(isForeground ? 1 : 0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void setAccountInfo(AppLogic.AccountInfo accountInfo) {
        sInstance.mAccountInfo = accountInfo;
    }

    private void removeTask(ITaskInterface taskInterface) {
        if (mQueue.remove(taskInterface)) {
            try {
                taskInterface.onTaskEnd(-1, -1);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            try {
                mMarsInterface.cancelTask(taskInterface);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mMarsInterface = IMarsInterface.Stub.asInterface(service);
        try {
            mMarsInterface.registerPush(mPushInterface);
        } catch (RemoteException e) {
            e.printStackTrace();
            mMarsInterface = null;
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        try {
            mMarsInterface.unregisterPush(mPushInterface);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mMarsInterface = null;
    }

    private void processTask() {
        if (mMarsInterface == null) {
            Intent intent = new Intent().setClassName(sPackageName, sClassName);
            sContext.startService(intent);
            sContext.bindService(intent, sInstance, Service.BIND_AUTO_CREATE);
            return;
        }
        ITaskInterface taskInterface = null;
        try {
            taskInterface = mQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (taskInterface == null) {
            return;
        }
        try {
            mMarsInterface.sendTask(taskInterface, taskInterface.getProperties());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static class WorkThread extends Thread {
        @Override
        public void run() {
            while (true) {
                sInstance.processTask();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
