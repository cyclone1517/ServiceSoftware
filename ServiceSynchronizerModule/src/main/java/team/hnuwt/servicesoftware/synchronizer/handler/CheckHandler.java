package team.hnuwt.servicesoftware.synchronizer.handler;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CheckHandler implements ResultHandler {

    private List<String> offlineList = new ArrayList<>();

    @Override
    public void handleResult(ResultContext context) {
        offlineList = (List<String>) context.getResultObject();
        return;
    }

    public List<String> getOfflineList(){
        return offlineList;
    }
}
