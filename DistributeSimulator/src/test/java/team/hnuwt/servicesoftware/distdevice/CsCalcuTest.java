package team.hnuwt.servicesoftware.distdevice;

import org.junit.Test;

/**
 * 用于检查校验和
 */
public class CsCalcuTest {

    @Test
    public void RunCalcuCS(){
        StringBuilder ck_archive_download = new StringBuilder();
        ck_archive_download.append("68D100D10068")
                .append("703607EC030084740000010001000300180910810100330000100100000000000000030436353433323139383736353433323130");

        StringBuilder my_archive_download = new StringBuilder();
        my_archive_download.append("688101810168")
                .append("700000a40f00847B11000100030001000801041511013300001001000000040100000000020063000609000033000010010000000401000000000300010000000000330000100100000004010000000036353433323139383736353433323130");

        StringBuilder ck_heartbeat = new StringBuilder();
        ck_heartbeat.append("683500350068")
                .append("C90000EC030002701000040012");

        String cs_ck_archive_download = calcuCs(ck_archive_download);
        String cs_my_archive_download = calcuCs(my_archive_download);
        String cs_ck_heartbeat = calcuCs(ck_heartbeat);

        System.out.println(cs_ck_archive_download);
    }

    public static String calcuCs(StringBuilder sb){
        String csStr = sb.toString().substring(12);

        int sum = 0;
        for (int i = 0; i < csStr.length(); i += 2)
        {
            sum += Integer.parseInt(csStr.substring(i, i + 2), 16);
        }

        int myNum = sum % 256;
        return Integer.toHexString(myNum);
    }
}
