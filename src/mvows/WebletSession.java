package mvows;

import java.util.HashMap;
import java.util.Objects;

/**
 * 保存 sessionid 和 session 对象之间的关系
 * Created by John on 5/11/2014.
 */
public class WebletSession {
    HashMap<String, Object> data = new HashMap<>();
    long lastUsed = System.currentTimeMillis();

    public void setAttribute(String key, Object value){
        data.put(key,value);
    }

    public Object getAttribute (String key) {
        return data.get(key);
    }


}
