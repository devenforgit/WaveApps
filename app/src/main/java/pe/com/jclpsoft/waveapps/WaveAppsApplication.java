package pe.com.jclpsoft.waveapps;

import pe.com.jclpsoft.waveapps.models.WaveAppsService;

public class WaveAppsApplication extends com.orm.SugarApp{
    private WaveAppsService waveAppsService=new WaveAppsService();

    public WaveAppsService getWaveAppsService() {
        return waveAppsService;
    }

    public void setWaveAppsService(WaveAppsService waveAppsService) {
        this.waveAppsService = waveAppsService;
    }
}
