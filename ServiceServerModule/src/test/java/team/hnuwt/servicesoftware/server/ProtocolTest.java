package team.hnuwt.servicesoftware.server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import team.hnuwt.servicesoftware.server.message.TCPMessageHandler;
import team.hnuwt.servicesoftware.server.util.ByteBuilder;

public class ProtocolTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        ByteBuilder bb = new ByteBuilder(
                "6850015001688800000000008C69000001070A0000006400000000010065000000010200660000000003006700000001040068000000000500690000000106006A0000000007006B0000000108006C0000000009006D000000010016");
        System.out.println(TCPMessageHandler.translate(bb, 0, new ByteBuilder(), null));
    }

}
