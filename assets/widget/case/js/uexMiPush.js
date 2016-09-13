/**
 * Created by ylt on 16/9/12.
 */


function registerCallback() {
    uexMiPush.onNotificationMessageClicked=function (data) {
        UNIT_TEST.log("-------------onNotificationMessageClicked------------------------");
    };
    uexMiPush.onReceivePassThroughMessage=function (data) {
        UNIT_TEST.log("-------------onReceivePassThroughMessage------------------------");
    };
    uexMiPush.onNotificationMessageArrived=function (data) {
        UNIT_TEST.log("-------------onNotificationMessageArrived------------------------");
    };
    uexMiPush.onCommandResult=function (data) {
        UNIT_TEST.log("-------------onCommandResult------------------------");
    };
    uexMiPush.onReceiveRegisterResult=function (data) {
        UNIT_TEST.log("-------------onReceiveRegisterResult------------------------");
    };
    uexMiPush.registerCallback();
}

if(UNIT_TEST){

    var uexMiPushCase = {
        "registerPush":function(){
            var params={
                appKey:"5421750944616",
                appId:"2882303761517509616"
            };
            uexMiPush.registerPush(JSON.stringify(params));
            UNIT_TEST.assertDelay(true,1000);
        },
        "getRegId":function () {
            var regId=uexMiPush.getRegId();
            UNIT_TEST.log(regId);
            UNIT_TEST.assertNotEqual(regId,null);
            registerCallback();
        },
        "setAlias":function () {
            uexMiPush.setAlias("test");
            alert("推送alias为'test'的消息");
            UNIT_TEST.assert(true);
        },
        "setUserAccount":function () {
            uexMiPush.setUserAccount("123456");
            alert('推送UserAccount为"123456"的消息');
            UNIT_TEST.assert(true);
        },
        "subscribe":function () {
            uexMiPush.subscribe("meizi");
            alert('推送频道为"meizi"的消息');
            UNIT_TEST.assert(true);
        },
        "getAllAlias":function () {
            uexMiPush.cbGetAllAlias=function (data) {
                UNIT_TEST.log(data);
                UNIT_TEST.assert(true);
            };
            uexMiPush.getAllAlias();
        },
        "getAllTopic":function () {
            uexMiPush.cbGetAllTopic=function (data) {
                UNIT_TEST.log(data);
                UNIT_TEST.assert(true);
            };
            uexMiPush.getAllTopic();
        },
        "setAcceptTime":function () {
            var params={
                startHour:1,
                startMin:0,
                endHour:23,
                endMin:0
            };
            uexMiPush.setAcceptTime(JSON.stringify(params));
            UNIT_TEST.assert(true);
        },
        "unsetAlias":function () {
            uexMiPush.unsetAlias("test");
            UNIT_TEST.assert(true);
        },
        "unsetUserAccount":function () {
            uexMiPush.unsetUserAccount("123456");
            UNIT_TEST.assert(true);
        },
        "unsubscribe":function () {
            uexMiPush.unsubscribe("meizi");
            UNIT_TEST.assert(true);
        }
        // ,
        // "unregisterPush":function () {
        //     uexMiPush.unregisterPush();
        //     UNIT_TEST.assert(true);
        // }
    };
    UNIT_TEST.addCase("uexMiPush",uexMiPushCase);


}
