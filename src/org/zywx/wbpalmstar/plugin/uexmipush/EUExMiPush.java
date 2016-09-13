package org.zywx.wbpalmstar.plugin.uexmipush;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.os.Process;

import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

import org.zywx.wbpalmstar.base.BDebug;
import org.zywx.wbpalmstar.engine.DataHelper;
import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.engine.universalex.EUExBase;
import org.zywx.wbpalmstar.plugin.uexmipush.vo.InitVO;
import org.zywx.wbpalmstar.plugin.uexmipush.vo.SetAcceptTimeVO;

import java.util.List;

public class EUExMiPush extends EUExBase {

    private static final String BUNDLE_DATA = "data";

    public EUExMiPush(Context context, EBrowserView eBrowserView) {
        super(context, eBrowserView);
    }

    @Override
    protected boolean clean() {
        return false;
    }
    

    @Override
    public void onHandleMessage(Message message) {
        if(message == null){
            return;
        }
        Bundle bundle=message.getData();
        switch (message.what) {

        default:
                super.onHandleMessage(message);
        }
    }

    public void registerPush(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        String json = params[0];
        InitVO initVO= DataHelper.gson.fromJson(json,InitVO.class);
        init(initVO.appId,initVO.appKey);
    }

    public void unregisterPush(String[] params) {
        MiPushClient.unregisterPush(mContext.getApplicationContext());
    }

    public void setAlias(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        MiPushClient.setAlias(mContext,params[0],null);
    }

    public void unsetAlias(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        MiPushClient.unsetAlias(mContext,params[0],null);
    }

    public void setUserAccount(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        MiPushClient.setUserAccount(mContext,params[0],null);
    }

    public void unsetUserAccount(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        MiPushClient.unsetUserAccount(mContext,params[0],null);
    }

    public void subscribe(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        MiPushClient.subscribe(mContext,params[0],null);
    }

    public void unsubscribe(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        MiPushClient.unsubscribe(mContext,params[0],null);
    }

    public String getRegId(String[] params) {
        return MiPushClient.getRegId(mContext);
    }

    public void getAllAlias(String[] params) {
        List<String> alias=MiPushClient.getAllAlias(mContext);
        callBackPluginJs(JsConst.CALLBACK_GET_ALL_ALIAS, DataHelper.gson.toJson(alias));
    }

    public void getAllTopic(String[] params) {
        List<String> topics=MiPushClient.getAllTopic(mContext);
        callBackPluginJs(JsConst.CALLBACK_GET_ALL_TOPIC, DataHelper.gson.toJson(topics));
    }

    public void registerCallback(String[] params){
        MiBroadcastReceiver.setEBrowserView(mBrwView);
    }

    public void setAcceptTime(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        String json = params[0];
        SetAcceptTimeVO timeVO=DataHelper.gson.fromJson(json,SetAcceptTimeVO.class);
        MiPushClient.setAcceptTime(mContext,timeVO.startHour,
                timeVO.startMin,timeVO.endHour,timeVO.endMin,null);
    }

    private void callBackPluginJs(String methodName, String jsonData){
        String js = SCRIPT_HEADER + "if(" + methodName + "){"
                + methodName + "('" + jsonData + "');}";
        onCallback(js);
    }

    private void init(String appId,String appKey){
        if(shouldInit()) {
            MiPushClient.checkManifest(mContext);
            MiPushClient.registerPush(mContext.getApplicationContext(), appId, appKey);
        }
        //打开Log
        LoggerInterface newLogger = new LoggerInterface() {

            @Override
            public void setTag(String tag) {
                // ignore
            }

            @Override
            public void log(String content, Throwable t) {
                BDebug.d(content, t);
            }

            @Override
            public void log(String content) {
                BDebug.d(content);
            }
        };
        Logger.setLogger(mContext.getApplicationContext(), newLogger);
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = mContext.getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

}
