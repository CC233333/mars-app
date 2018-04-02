package u.master.mars.wrapper;

import android.os.Bundle;
import android.os.RemoteException;

/**
 * Created by master_c on 2017/5/25.
 */

public abstract class AbstractTaskStub extends ITaskInterface.Stub {

    private Bundle mProperties = new Bundle();

    public AbstractTaskStub() {
        TaskProperty taskProperty = this.getClass().getAnnotation(TaskProperty.class);
        if (taskProperty != null) {
            setCid(taskProperty.cid());
            setHostPath(taskProperty.host(), taskProperty.path());
            setShortChannelSupport(taskProperty.shortChannelSupport());
            setLongChannelSupport(taskProperty.longChannelSupport());
        }
    }

    @Override
    public Bundle getProperties() throws RemoteException {
        return mProperties;
    }

    public AbstractTaskStub setCid(int cid) {
        mProperties.putInt(MarsConstant.OPTIONS_CID, cid);
        return this;
    }

    public AbstractTaskStub setHostPath(String host, String path) {
        mProperties.putString(MarsConstant.OPTIONS_HOST, ("".equals(host) ? null : host));
        mProperties.putString(MarsConstant.OPTIONS_PATH, path);
        return this;
    }

    public AbstractTaskStub setShortChannelSupport(boolean support) {
        mProperties.putBoolean(MarsConstant.OPTIONS_CHANNEL_SHORT_SUPPORT, support);
        return this;
    }

    public AbstractTaskStub setLongChannelSupport(boolean support) {
        mProperties.putBoolean(MarsConstant.OPTIONS_CHANNEL_LONG_SUPPORT, support);
        return this;
    }

}
