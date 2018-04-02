package u.master.mars.wrapper;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.tencent.mars.Mars;
import com.tencent.mars.app.AppLogic;
import com.tencent.mars.sdt.SdtLogic;
import com.tencent.mars.stn.StnLogic;

import u.master.mars.wrapper.profile.BaseMarsProfile;
import u.master.mars.wrapper.profile.IMarsProfile;
import u.master.mars.wrapper.profile.IMarsProfileFactory;

/**
 * Created by master_c on 2017/5/25.
 */

public class MarsService extends Service {

    private MarsStub mMarsStub;

    private static IMarsProfileFactory sProfileFactory = new IMarsProfileFactory() {
        @Override
        public IMarsProfile createMarsProfile() {
            return new BaseMarsProfile();
        }
    };

    public static void setProfileFactory(IMarsProfileFactory profileFactory) {
        sProfileFactory = profileFactory;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        IMarsProfile profile = sProfileFactory.createMarsProfile();
        mMarsStub = new MarsStub(this, profile);

        // set callback
        AppLogic.setCallBack(mMarsStub);
        StnLogic.setCallBack(mMarsStub);
        SdtLogic.setCallBack(mMarsStub);

        // Initialize the Mars PlatformComm
        Mars.init(getApplicationContext(), new Handler(Looper.getMainLooper()));

        // Initialize the Mars
        StnLogic.setLonglinkSvrAddr(profile.longLinkHost(), profile.longLinkPorts());
        StnLogic.setShortlinkSvrAddr(profile.shortLinkPort());
        StnLogic.setClientVersion(profile.productID());

        Mars.onCreate(true);

        StnLogic.makesureLongLinkConnected();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMarsStub;
    }

    @Override
    public void onDestroy() {
        Mars.onDestroy();

        super.onDestroy();
    }

}
