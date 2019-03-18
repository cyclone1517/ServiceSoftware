package team.hnuwt.servicesoftware.synchronizer.handler;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CheckOfflineHandler implements ResultHandler {

    private List<String> offlineList = new ArrayList<>();

    @Override
    public void handleResult(ResultContext context) {
        Map<String, Object> resultObj = (Map<String, Object>) context.getResultObject();
        String collectorId = (String) resultObj.get("collectorId");
        offlineList.add(collectorId);
    }

    public List<String> getOfflineList(){
        return offlineList;
    }
}
