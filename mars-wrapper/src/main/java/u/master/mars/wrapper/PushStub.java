package u.master.mars.wrapper;

import android.os.RemoteException;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by master_c on 2017/5/25.
 */

public class PushStub extends IPushInterface.Stub {

    private ConcurrentHashMap<Integer, PushCallback> mHashMap = new ConcurrentHashMap<>();

    @Override
    public boolean onReceive(int cid, byte[] bytes) throws RemoteException {
        PushCallback callback = mHashMap.get(cid);
        if (callback != null) {
            PushObject object = new PushObject(cid, bytes);
            callback.process(object);
            return true;
        }
        return false;
    }

}
