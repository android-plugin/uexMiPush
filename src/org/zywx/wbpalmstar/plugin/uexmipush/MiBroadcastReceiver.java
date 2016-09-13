package org.zywx.wbpalmstar.plugin.uexmipush;

import android.content.Context;

import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import org.zywx.wbpalmstar.base.BDebug;
import org.zywx.wbpalmstar.engine.DataHelper;
import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.plugin.uexmipush.vo.MiPushMessageVO;

import java.util.ArrayList;
import java.util.List;

import static org.zywx.wbpalmstar.engine.universalex.EUExBase.SCRIPT_HEADER;

/**
 * Created by ylt on 16/9/12.
 */

public class MiBroadcastReceiver extends PushMessageReceiver {

    public static EBrowserView sEBrowserView;
    private static List<MiPushMessageVO> mMiPushMessageVOs=new ArrayList<MiPushMessageVO>();

    public static void setEBrowserView(EBrowserView eBrowserView){
        sEBrowserView=eBrowserView;

        //回调缓存的消息
        if (!mMiPushMessageVOs.isEmpty()){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (MiPushMessageVO miPushMessageVO:mMiPushMessageVOs) {
                        callBackJs(miPushMessageVO.callbackName,DataHelper.gson.toJsonTree(miPushMessageVO));
                        try {
                            Thread.sleep(100);//防止回调过快
                        } catch (InterruptedException e) {
                            if (BDebug.DEBUG) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).start();
        }
    }

    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage miPushMessage) {
        super.onReceivePassThroughMessage(context, miPushMessage);
        BDebug.i(miPushMessage.toString());
        handleMessage(transMessage(JsConst.ON_RECEIVE_PASS_THROUGH_MESSAGE,miPushMessage));
    }

    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage miPushMessage) {
        super.onNotificationMessageClicked(context, miPushMessage);
        BDebug.i(miPushMessage.toString());
        handleMessage(transMessage(JsConst.ON_NOTIFICATION_MESSAGE_CLICKED,miPushMessage));
    }

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage miPushMessage) {
        super.onNotificationMessageArrived(context, miPushMessage);
        BDebug.i(miPushMessage.toString());
        handleMessage(transMessage(JsConst.ON_NOTIFICATION_MESSAGE_ARRIVED,miPushMessage));
    }

    @Override
    public void onCommandResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        super.onCommandResult(context, miPushCommandMessage);
        BDebug.i(miPushCommandMessage.toString());
        handleMessage(transCommandMessage(JsConst.ON_COMMAND_RESULT,miPushCommandMessage));
    }

    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        super.onReceiveRegisterResult(context, miPushCommandMessage);
        BDebug.i(miPushCommandMessage.toString());
        handleMessage(transCommandMessage(JsConst.ON_RECEIVE_REGISTER_RESULT,miPushCommandMessage));
    }

    private void handleMessage(MiPushMessageVO messageVO){
        if (sEBrowserView!=null){
            callBackJs(messageVO.callbackName, DataHelper.gson.toJsonTree(messageVO));
        }else{
            mMiPushMessageVOs.add(messageVO);
        }
    }


    public static void callBackJs(String methodName, Object value){
        String js = SCRIPT_HEADER + "if(" + methodName + "){"
                + methodName + "(" + value + ");}else{console.log('function "+methodName +" not found.')}";
        sEBrowserView.addUriTask(js);
    }

    public MiPushMessageVO transMessage(String methodName,MiPushMessage miPushMessage){
        MiPushMessageVO messageVO=new MiPushMessageVO();
        messageVO.callbackName=methodName;
        messageVO.messageId=miPushMessage.getMessageId();
        messageVO.messageType=miPushMessage.getNotifyType();
        messageVO.content=miPushMessage.getContent();
        messageVO.alias=miPushMessage.getAlias();
        messageVO.topic=miPushMessage.getTopic();
        messageVO.userAccount=miPushMessage.getUserAccount();
        messageVO.passThrough=miPushMessage.getPassThrough();
        messageVO.notifyType=miPushMessage.getNotifyType();
        messageVO.notifyId=miPushMessage.getNotifyId();
        messageVO.isNotified=miPushMessage.isNotified();
        messageVO.description=miPushMessage.getDescription();
        messageVO.title=miPushMessage.getTitle();
        messageVO.category=miPushMessage.getCategory();
        messageVO.arrived=miPushMessage.isArrivedMessage();
        messageVO.extra=miPushMessage.getExtra();
        return messageVO;
    }

    public MiPushMessageVO transCommandMessage(String methodName,MiPushCommandMessage miPushCommandMessage){
        MiPushMessageVO messageVO=new MiPushMessageVO();
        messageVO.callbackName=methodName;
        messageVO.command=miPushCommandMessage.getCommand();
        messageVO.resultCode=miPushCommandMessage.getResultCode();
        messageVO.reason=miPushCommandMessage.getReason();
        messageVO.commandArguments=miPushCommandMessage.getCommandArguments();
        messageVO.category=miPushCommandMessage.getCategory();
        return messageVO;
    }
}
