package team.hnuwt.servicesoftware.distdevice;

import org.junit.Test;

/**
 * 用于检查校验和
 */
public class CsCalcuTest {

    @Test
    public void RunCalcuCS(){
        StringBuilder ck_archive_download = new StringBuilder();
        ck_archive_download.append("AA585808005A00381C00000000");

        String cs_ck_archive_download = calcuCs(ck_archive_download);

        System.out.println(cs_ck_archive_download);
    }

    public static String calcuCs(StringBuilder sb){
        //String csStr = sb.toString().substring(12);
        String csStr = sb.toString();

        int sum = 0;
        for (int i = 0; i < csStr.length(); i += 2)
        {
            sum += Integer.parseInt(csStr.substring(i, i + 2), 16);
        }

        int myNum = sum % 256;
        return Integer.toHexString(myNum);
    }
}
