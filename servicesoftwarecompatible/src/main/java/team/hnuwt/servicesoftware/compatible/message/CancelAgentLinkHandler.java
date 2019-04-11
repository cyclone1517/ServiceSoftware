package team.hnuwt.servicesoftware.compatible.message;

import team.hnuwt.servicesoftware.compatible.util.CompatibleUtil;

public class CancelAgentLinkHandler implements Runnable{

    private String[] cacenlList;

    public CancelAgentLinkHandler(String[] cacenlList){
        this.cacenlList = cacenlList;
    }

    @Override
    public void run() {
        for (String i: cacenlList){
            CompatibleUtil.remove(Long.parseLong(i));
        }
    }
}
