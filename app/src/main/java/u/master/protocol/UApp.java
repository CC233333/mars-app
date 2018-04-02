package u.master.protocol;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.tencent.mars.app.AppLogic;
import com.tencent.mars.xlog.Log;
import com.tencent.mars.xlog.Xlog;

import java.util.Random;

import u.master.mars.wrapper.MarsService;
import u.master.mars.wrapper.MarsServiceProxy;
import u.master.mars.wrapper.profile.BaseMarsProfile;
import u.master.mars.wrapper.profile.IMarsProfile;
import u.master.mars.wrapper.profile.IMarsProfileFactory;

/**
 * Created by master_c on 2017/5/26.
 */

public class UApp extends Application {

    public static final String BASE_URL = "192.168.1.131";

//    public static final String BASE_URL = "marsopen.cn";

    private static Context sContext;

    private static AppLogic.AccountInfo sAccountInfo = new AppLogic.AccountInfo(
            new Random(System.currentTimeMillis() / 1000).nextInt(), "anonymous");

    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;

        System.loadLibrary("stlport_shared");
        System.loadLibrary("marsxlog");
        openXlog();

        MarsService.setProfileFactory(new IMarsProfileFactory() {
            @Override
            public IMarsProfile createMarsProfile() {
                return new UProfile();
            }
        });

        MarsServiceProxy.init(this, null);
        MarsServiceProxy.setAccountInfo(sAccountInfo);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.appenderClose();
    }

    public static void openXlog() {
        final String SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath();
        final String logPath = SDCARD + "/mars/log";

        if (BuildConfig.DEBUG) {
            Xlog.appenderOpen(Xlog.LEVEL_VERBOSE, Xlog.AppednerModeAsync, "", logPath, "MarsLog");
            Xlog.setConsoleLogOpen(true);
        } else {
            Xlog.appenderOpen(Xlog.LEVEL_INFO, Xlog.AppednerModeAsync, "", logPath, "MarsLog");
            Xlog.setConsoleLogOpen(false);
        }
        Log.setLogImp(new Xlog());
    }

    private static class UProfile extends BaseMarsProfile {
        @Override
        public String longLinkHost() {
            return BASE_URL;
        }


    }


    private static void createLogImp() {

    }
    No signature of method: org.gradle.api.internal.tasks.DefaultTaskInputs.source() is applicable for argument types: (com.google.protobuf.gradle.ProtobufSourceDirectorySet_Decorated) values: [main Proto source]
    Possible solutions: collect(), use([Ljava.lang.Object;)

}
