// ITaskInterface.aidl
package u.master.mars.wrapper;

// Declare any non-default types here with import statements

interface ITaskInterface {

     Bundle getProperties();

     byte[] request2Bytes();

     int bytes2Response(in byte[] bytes);

     void onTaskEnd(in int type, in int code);

}
