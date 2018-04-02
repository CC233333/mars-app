package u.master.mars.wrapper;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;

import com.tencent.mars.BaseEvent;
import com.tencent.mars.app.AppLogic;
import com.tencent.mars.sdt.SdtLogic;
import com.tencent.mars.stn.StnLogic;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import u.master.mars.wrapper.profile.IMarsProfile;

import static com.tencent.mars.comm.PlatformComm.context;

/**
 * Created by master_c on 2017/5/25.
 */

public class MarsStub extends IMarsInterface.Stub implements StnLogic.ICallBack, SdtLogic.ICallBack, AppLogic.ICallBack {

    private Context mContext;
    private IMarsProfile mMarsProfile;
    private AppLogic.AccountInfo mAccountInfo;

    private static Map<Integer, ITaskInterface> sTaskMap = new ConcurrentHashMap<>();
    private static Map<ITaskInterface, Integer> sTaskIdMap = new ConcurrentHashMap<>();

    private ConcurrentLinkedQueue<IPushInterface> mPushInterfaces = new ConcurrentLinkedQueue<>();


    public MarsStub(Context context, IMarsProfile marsProfile) {
        mContext = context;
        mMarsProfile = marsProfile;
    }

    @Override
    public void sendTask(ITaskInterface taskInterface, Bundle taskBundle) throws RemoteException {
        StnLogic.Task task = new StnLogic.Task(StnLogic.Task.EShort, 0, "", null);
        String host = taskBundle.getString(MarsConstant.OPTIONS_HOST);
        String path = taskBundle.getString(MarsConstant.OPTIONS_PATH);
        task.shortLinkHostList = new ArrayList<>();
        task.shortLinkHostList.add(host);
        task.cgi = path;

        boolean shortSupport = taskBundle.getBoolean(MarsConstant.OPTIONS_CHANNEL_SHORT_SUPPORT, true);
        boolean longSupport = taskBundle.getBoolean(MarsConstant.OPTIONS_CHANNEL_LONG_SUPPORT, false);

        if (shortSupport && longSupport) {
            task.channelSelect = StnLogic.Task.EBoth;
        } else if (shortSupport) {
            task.channelSelect = StnLogic.Task.EShort;
        } else if (longSupport) {
            task.channelSelect = StnLogic.Task.ELong;
        } else {
            throw new RemoteException("Invalid Channel Strategy");
        }

        int cid = taskBundle.getInt(MarsConstant.OPTIONS_CID, -1);
        if (cid != -1) {
            task.cmdID = cid;
        }

        sTaskMap.put(task.taskID, taskInterface);
        sTaskIdMap.put(taskInterface, task.taskID);

        StnLogic.startTask(task);
    }

    @Override
    public void cancelTask(ITaskInterface taskInterface) throws RemoteException {
        if (taskInterface == null) {
            return;
        }
        Integer taskID = sTaskIdMap.get(taskInterface);
        if (taskID != null) {
            StnLogic.stopTask(taskID);
            sTaskMap.remove(taskID);
        }
    }

    @Override
    public void registerPush(IPushInterface pushInterface) throws RemoteException {
        mPushInterfaces.remove(pushInterface);
        mPushInterfaces.add(pushInterface);
    }

    @Override
    public void unregisterPush(IPushInterface pushInterface) throws RemoteException {
        mPushInterfaces.remove(pushInterface);
    }

    @Override
    public void setAccountInfo(long uin, String username) throws RemoteException {
        mAccountInfo.uin = uin;
        mAccountInfo.userName = username;
    }

    @Override
    public void setForeground(int isForeground) throws RemoteException {
        BaseEvent.onForeground(isForeground == 1);
    }

    @Override
    public boolean makesureAuthed() {
        return true;
    }

    @Override
    public String[] onNewDns(String host) {
        return null;
    }

    @Override
    public void onPush(int cmdid, byte[] data) {
        for (IPushInterface pushInterface : mPushInterfaces) {
            try {
                if (pushInterface.onReceive(cmdid, data)) {
                    break;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean req2Buf(int taskID, Object userContext, ByteArrayOutputStream reqBuffer, int[] errCode, int channelSelect) {
        ITaskInterface taskInterface = sTaskMap.get(taskID);
        if (taskInterface == null) {
            return false;
        }
        try {
            reqBuffer.write(taskInterface.request2Bytes());
            return true;
        } catch (IOException | RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int buf2Resp(int taskID, Object userContext, byte[] respBuffer, int[] errCode, int channelSelect) {
        ITaskInterface taskInterface = sTaskMap.get(taskID);
        if (taskInterface == null) {
            return StnLogic.RESP_FAIL_HANDLE_TASK_END;
        }
        try {
            return taskInterface.bytes2Response(respBuffer);
        } catch (RemoteException e) {
            e.printStackTrace();
            sTaskMap.remove(taskID);
        }
        return StnLogic.RESP_FAIL_HANDLE_TASK_END;
    }

    @Override
    public int onTaskEnd(int taskID, Object userContext, int errType, int errCode) {
        ITaskInterface taskInterface = sTaskMap.get(taskID);
        if (taskInterface == null) {
            return 0;
        }
        try {
            taskInterface.onTaskEnd(errType, errCode);
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            sTaskMap.remove(taskID);
        }
        return 0;
    }

    @Override
    public void trafficData(int send, int recv) {

    }

    @Override
    public void reportConnectInfo(int status, int longlinkstatus) {

    }

    @Override
    public int getLongLinkIdentifyCheckBuffer(ByteArrayOutputStream identifyReqBuf, ByteArrayOutputStream hashCodeBuffer, int[] reqRespCmdID) {
        return StnLogic.ECHECK_NEVER;
    }

    @Override
    public boolean onLongLinkIdentifyResp(byte[] buffer, byte[] hashCodeBuffer) {
        return false;
    }

    @Override
    public void requestDoSync() {

    }

    @Override
    public String[] requestNetCheckShortLinkHosts() {
        return new String[0];
    }

    @Override
    public boolean isLogoned() {
        return false;
    }

    @Override
    public void reportTaskProfile(String taskString) {

    }

    @Override
    public String getAppFilePath() {
        if (mContext == null) {
            return null;
        }

        try {
            File file = context.getFilesDir();
            if (!file.exists()) {
                file.createNewFile();
            }
            return file.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public AppLogic.AccountInfo getAccountInfo() {
        return mAccountInfo;
    }

    @Override
    public int getClientVersion() {
        return 0;
    }

    @Override
    public AppLogic.DeviceInfo getDeviceType() {
        return null;
    }

    @Override
    public void reportSignalDetectResults(String resultsJson) {

    }
}
