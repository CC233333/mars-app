// IPushInterface.aidl
package u.master.mars.wrapper;

// Declare any non-default types here with import statements

interface IPushInterface {

    boolean onReceive(int cid, inout byte[] bytes);

}
