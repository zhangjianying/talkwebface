package com.talkweb.face;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;

import com.talkweb.FaceRecognitionUtils;
import com.talkweb.activity.FaceRecognitionPermissionsActivity;
import com.talkweb.bean.ResponseBean;
import com.talkweb.utils.Confing;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.app.Activity.RESULT_OK;

public class TalkwebFacePrinter extends CordovaPlugin {
    private final static int cameraAreaCode = 1231;
    private CallbackContext callbackContext;
    private JSONObject params;

/*    private class MyThread extends Thread {
        @Override
        public void run() {
            mUsbPrinter = new UsbPrinter(getApplicationContext());
        }
    }*/

    @Override
    public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        this.callbackContext = callbackContext;
        this.params = args.getJSONObject(0);
        //启动AI人脸识别
        if (action.equals("startFace")) {
            try {
                //System.out.println("获取到设置参数:"+this.params.toString());
                if(!this.params.isNull("serverUrl")){
                    String serverUrl = this.params.getString("serverUrl");
                    if (serverUrl != null && !"".equals(serverUrl)) {
                        //System.out.println("设置serverUrl:"+serverUrl);
                        FaceRecognitionUtils.setFileUpdateUrl(serverUrl);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (!this.params.isNull("playMusic")) {
                    boolean playMusic = params.getBoolean("playMusic");
                    FaceRecognitionUtils.setPlayMusic(playMusic);//是否播放提示音
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (!this.params.isNull("vibrator")) {
                    boolean playMusic = params.getBoolean("vibrator");
                    FaceRecognitionUtils.setVibrator(playMusic);//是否震动提示
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            String userid = null;
            try {
                if(!this.params.isNull("userid")){
                    userid = this.params.getString("userid");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


//            Activity activity = cordova.getActivity();
//            FaceRecognitionUtils.start(activity, cameraAreaCode, userid);


            Confing.dataMap.clear();
            if (userid != null && !"".equals(userid.trim())) {
                Confing.dataMap.put("userid", userid);
            }

            Intent intent = new Intent(cordova.getContext(), FaceRecognitionPermissionsActivity.class);
            if (Build.VERSION.SDK_INT >= 20) {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
            cordova.startActivityForResult(this,intent, cameraAreaCode);

        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == cameraAreaCode) {
            ResponseBean bean = null;
            if (resultCode == RESULT_OK) {
                bean = (ResponseBean) intent.getSerializableExtra(
                        FaceRecognitionUtils.RESULT_NAME);

            } else {
                bean = new ResponseBean(-1, "未识别到");
            }
            JSONObject ret = new JSONObject();
            try {
                ret.put("code", bean.getCode());
                ret.put("desc", bean.getDesc());
                ret.put("obj", bean.getObj());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //System.out.println("识别结果返回:"+ret.toString());
            callbackContext.success(ret);
        }
    }
}
