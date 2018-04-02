package u.master.mars.wrapper.profile;

/**
 * Created by master_c on 2017/5/25.
 */

public interface IMarsProfile {

    short magic();

    short productID();

    String longLinkHost();

    int[] longLinkPorts();

    int shortLinkPort();

}
