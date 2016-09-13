package org.zywx.wbpalmstar.plugin.uexmipush.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by ylt on 16/9/13.
 */

public class MiPushMessageVO implements Serializable {

    public String callbackName;

    public String command;
    public long resultCode;
    public String reason;
    public List<String> commandArguments;

    public String messageId;
    public int messageType;
    public String content;
    public String alias;
    public String topic;
    public String userAccount;
    public int passThrough;
    public int notifyType;
    public int notifyId;
    public boolean isNotified;
    public String description;
    public String title;
    public String category;
    public boolean arrived = false;
    public Map<String, String> extra;
    
}
