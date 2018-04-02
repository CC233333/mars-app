package u.master.mars.wrapper;

import android.os.RemoteException;

import com.google.protobuf.nano.MessageNano;
import com.tencent.mars.stn.StnLogic;

/**
 * Created by master_c on 2017/5/25.
 */

public abstract class NanoTaskStub<T extends MessageNano, R extends MessageNano> extends AbstractTaskStub {

    protected T mRequest;
    protected R mResponse;

    public NanoTaskStub(T request, R response) {
        super();
        mRequest = request;
        mResponse = response;
    }

    @Override
    public byte[] request2Bytes() throws RemoteException {
        try {
            onPreEncode(mRequest);
            return MessageNano.toByteArray(mRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    @Override
    public int bytes2Response(byte[] bytes) throws RemoteException {
        try {
            mResponse = MessageNano.mergeFrom(mResponse, bytes);
            onPostDecode(mResponse);
            return StnLogic.RESP_FAIL_HANDLE_NORMAL;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return StnLogic.RESP_FAIL_HANDLE_TASK_END;
    }

    @Override
    public void onTaskEnd(int type, int code) throws RemoteException {

    }

    public abstract void onPreEncode(T request);

    public abstract void onPostDecode(R response);

}
