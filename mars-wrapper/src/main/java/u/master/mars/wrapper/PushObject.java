package u.master.mars.wrapper;

/**
 * Created by master_c on 2017/5/25.
 */

public class PushObject {

    private int mCid;
    private byte[] mBytes;

    public PushObject(int cid, byte[] bytes) {
        mCid = cid;
        mBytes = bytes;
    }

    public int getCid() {
        return mCid;
    }

    public byte[] getBytes() {
        return mBytes;
    }

    @Override
    public String toString() {
        return "PushObject{" +
                "mCid=" + mCid +
                '}';
    }
}
