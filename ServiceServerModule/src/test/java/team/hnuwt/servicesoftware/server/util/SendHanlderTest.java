package team.hnuwt.servicesoftware.server.util;

import org.junit.Test;

import java.nio.channels.SocketChannel;

public class SendHanlderTest {

    @Test
    public void testIdCalcu(){
        String pkg = "689100910068700000000000847B1100010001000800080104151101" +
                "33000010010000000401000000000800080104151101330000100100000004010000000036353433323139383736353433323130CE16";

        ByteBuilder b = new ByteBuilder(pkg);
        long id = FieldPacker.getId(b);
        System.out.println(id);
    }
}
