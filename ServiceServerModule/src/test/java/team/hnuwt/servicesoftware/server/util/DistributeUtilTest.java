package team.hnuwt.servicesoftware.server.util;

import org.junit.Test;

public class DistributeUtilTest {

    @Test
    public void runUtil(){
        String pkg = "6845004500687036070000008C70000001070001000800ba16";
        ByteBuilder b = new ByteBuilder(pkg);
        long id = b.BINToLong(7, 12);
        System.out.println(id);
    }

}
