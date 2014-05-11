package mvows;

/**
 * Created by John on 4/30/2014.
 */
public class WebletConfigs {

    String url;
    Class cls;

    public WebletConfigs(String url, Class cls) {
        this.url = url;
        this.cls = cls;
    }

    static WebletConfigs[] webletConfigs = new WebletConfigs[]{
            new WebletConfigs("/Hello", myapps.HelloWorldMyWeblet.class),
            new WebletConfigs("/ProcessName", myapps.NameProcessor.class),
            new WebletConfigs("/SessionTest", myapps.SessionTest.class)
    };
}
