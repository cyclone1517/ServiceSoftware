package team.hnuwt.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import team.hnuwt.message.Protocol;

public class ProtocolTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        String pkg = "6823002300688803130100008C6900000107"
                + "0300" 
                + "01000080040000"
                + "02000000000000"
                + "03000009670000"
                + "F716";
        System.out.println(Protocol.normalProtocol(new StringBuilder(pkg), 0, new StringBuilder()));
        pkg = "6823";
        System.out.println(Protocol.normalProtocol(new StringBuilder(pkg), 0, new StringBuilder()));
        pkg = "002300688803130100008C6900000107";
        System.out.println(Protocol.normalProtocol(new StringBuilder(pkg), 4, new StringBuilder("6823")));
        pkg = "0300" + "01000080040000" + "02000000000000" + "03000009670000" + "F716";
        System.out.println(Protocol.normalProtocol(new StringBuilder(pkg), -51, new StringBuilder("6823002300688803130100008C6900000107")));
        
        pkg = "6823002300688803130100008C69000001070300000000000000000100010000000102000200000000F716";
        System.out.println(Protocol.normalProtocol(new StringBuilder(pkg), 0, new StringBuilder()));
    }

}
