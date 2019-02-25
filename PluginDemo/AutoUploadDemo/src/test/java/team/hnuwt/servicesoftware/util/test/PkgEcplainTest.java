package team.hnuwt.servicesoftware.util.test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.model.EncodeFormat;
import team.hnuwt.servicesoftware.util.PkgExpUtil;

public class PkgEcplainTest {

    Logger logger = LoggerFactory.getLogger("PkgEcplainTest");

    @Test
    public void runPkgExplain(){
        Long id = new Long("30081548684");
        PkgExpUtil.getBusiName(id);
        String[] fieldList = PkgExpUtil.getFiledName(id);
        Integer[] lenList = PkgExpUtil.getFiledLen(id);
        EncodeFormat[] ecdList = PkgExpUtil.getFiledCode(id);
    }
}
