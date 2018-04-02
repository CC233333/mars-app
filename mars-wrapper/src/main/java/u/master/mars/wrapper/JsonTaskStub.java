package u.master.mars.wrapper;

import android.os.RemoteException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tencent.mars.stn.StnLogic;

/**
 * Created by master_c on 2017/5/25.
 */

public abstract class JsonTaskStub extends AbstractTaskStub {

    protected JsonObject mRequest;
    protected JsonObject mResponse;

    public JsonTaskStub(JsonObject request, JsonObject response) {
        super();
        mRequest = request;
        mResponse = response;
    }

    @Override
    public byte[] request2Bytes() throws RemoteException {
        try {
            onPreEncode(mRequest);
            return mRequest.toString().getBytes("utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    @Override
    public int bytes2Response(byte[] bytes) throws RemoteException {
        try {
            mResponse = new JsonParser().parse(new String(bytes, "utf-8")).getAsJsonObject();
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

    public abstract void onPreEncode(JsonObject request);

    public abstract void onPostDecode(JsonObject response);

}
