package edu.acmd.vendingui.common;

import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import edu.acmd.vendingui.jni.Controller;
/**
 * Created by ACMDIWATYO on 2018/3/6.
 */

public class TestPlugin extends CordovaPlugin {

    public static final String LOG_TAG = "TEST_PLUGIN";

    @Override
    protected void pluginInitialize() {
        super.pluginInitialize();
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        switch (action){
            case "testaction": {
                Log.i(LOG_TAG, args.toString());
                callbackContext.success(args);
                return true;
            }
            case "led":{
                Log.i(LOG_TAG, args.getInt(0) + "");
                Log.i(LOG_TAG, args.getInt(1) + "");
                Controller.openLed();
                Controller.ledCtrl(args.getInt(0), args.getInt(1));
                return true;
            }
            case "serial":{
                Controller.openSerial();
                Controller.writeToSerial(args.getString(0));
                Controller.closeSerial();
                return true;
            }
        }
        return true;
    }
}
