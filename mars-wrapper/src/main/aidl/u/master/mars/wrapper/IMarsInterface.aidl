// IMarsInterface.aidl
package u.master.mars.wrapper;

// Declare any non-default types here with import statements
import u.master.mars.wrapper.ITaskInterface;
import u.master.mars.wrapper.IPushInterface;

interface IMarsInterface {

    void sendTask(ITaskInterface taskInterface, in Bundle taskBundle);

    void cancelTask(ITaskInterface taskInterface);

    void registerPush(IPushInterface pushInterface);

    void unregisterPush(IPushInterface pushInterface);

    void setAccountInfo(in long uin, in String username);

    void setForeground(in int isForeground);

}
