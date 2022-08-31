package info.paysyslabs.plugins.intent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.paysyslabs.plugin.activity.QrScannerActivity;
import com.paysyslabs.plugin.activity.RemoteActivity;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;


/**
 * This class echoes a string called from JavaScript.
 * Created By: Moeen Channa 01-07-2022
 */
public class CordovaIntentPlugin extends CordovaPlugin {

    private static final String TAG = "CordovaIntentPlugin";

    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;
    public static String result;
    public static String qrcData;
    public static String actionCall;
    public static String authToken, authUsername, cvm, deviceId, expiryDate,
            pan, sdkProperties, useCaseIndicator, walletId,enrolId,virtualToken;
    public static String cardStateAction;
    private CallbackContext callback;
    private Context context;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        context = cordova.getActivity().getApplicationContext();
        callback = callbackContext;
        actionCall = action;

        Log.d(TAG, "execute: ");

        switch (action) {
            case "authToken":

                MethodsHandling.authToken(args);
                openNewActivity(context);

                return true;
            case "tokenProvision":

                MethodsHandling.tokenProvision(args);
                openNewActivity(context);

                return true;
            case "cardStateUpdate":

                MethodsHandling.cardStateUpdate(args);
                openNewActivity(context);

                return true;

            case "qrScan":

                MethodsHandling.qrScan(args);
                openNewActivity(context);

                return true;

            case "getAllActiveTokensId":
            case "getDefaultTokenId":
            case "getAllActiveCards":
            case "lukCount":
            case "isTokenValidForProcessing":
            case "tapAndPay":
            case "checkNfc":
            case "cdvcmStatus":
                openNewActivity(context);

                return true;


        }

        return false;
    }

    private void cordovaMethods(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
            Log.d(TAG, "callbackContext: ");
        } else {
            Log.d(TAG, "Expected one non-empty string argument.");
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    public void openNewActivity(Context context) {

        switch (actionCall) {
            case "qrScan":
                if (!qrcData.isEmpty()) {
                    Intent intent = new Intent(context, QrScannerActivity.class);
                    intent.putExtra("qrData", qrcData);
                    cordova.startActivityForResult(this, intent, SECOND_ACTIVITY_REQUEST_CODE);
                }
                break;
            case "authToken": {
                Intent intent = new Intent(context, RemoteActivity.class);
                intent.putExtra("actionCall", actionCall);
                intent.putExtra("authToken", authToken);
                intent.putExtra("authUsername", authUsername);
                cordova.startActivityForResult(this, intent, SECOND_ACTIVITY_REQUEST_CODE);
                break;
            }
            case "tokenProvision": {
                Intent intent = new Intent(context, RemoteActivity.class);
                intent.putExtra("actionCall", actionCall);
                intent.putExtra("cvm", cvm);
                intent.putExtra("deviceId", deviceId);
                intent.putExtra("expiryDate", expiryDate);
                intent.putExtra("pan", pan);
                intent.putExtra("sdkProperties", sdkProperties);
                intent.putExtra("useCaseIndicator", useCaseIndicator);
                intent.putExtra("walletId", walletId);
                //TODO Add MORE PARAMETERS LATER
                cordova.startActivityForResult(this, intent, SECOND_ACTIVITY_REQUEST_CODE);
                break;
            }
            case "cardStateUpdate": {
                Intent intent = new Intent(context, RemoteActivity.class);
                intent.putExtra("actionCall", actionCall);
                intent.putExtra("cardStateAction", cardStateAction);
                //TODO Add MORE PARAMETERS LATER
                cordova.startActivityForResult(this, intent, SECOND_ACTIVITY_REQUEST_CODE);
                break;
            }
            case "getAllActiveTokensId":
            case "getDefaultTokenId":
            case "getAllActiveCards":
            case "lukCount":
            case "isTokenValidForProcessing":
            case "tapAndPay":
            case "checkNfc":
            case "cdvcmStatus": {
                Intent intent = new Intent(context, RemoteActivity.class);
                intent.putExtra("actionCall", actionCall);
                cordova.startActivityForResult(this, intent, SECOND_ACTIVITY_REQUEST_CODE);
                break;
            }


        }
    }

    // This method is called when the second activity finishes
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check that it is the SecondActivity with an OK result
        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) { // Activity.RESULT_OK
                result = data.getStringExtra("screenData");
                Log.i(TAG, "onActivityResult: " + result);

                this.cordovaMethods(result, callback);

            }
        }
    }

    public static class MethodsHandling {

        public static void authToken(JSONArray args) throws JSONException {
            authToken = args.getString(0);
            authUsername = args.getString(1);
        }

        public static void tokenProvision(JSONArray args) throws JSONException {
            useCaseIndicator = args.getString(0);
            deviceId = args.getString(1);
            expiryDate = args.getString(2);
            pan = args.getString(3);
            sdkProperties = args.getString(4);
            cvm = args.getString(5);
            walletId = args.getString(6);
        }

        public static void cardStateUpdate(JSONArray args) throws JSONException {
            deviceId = args.getString(0);
            enrolId = args.getString(1);
            virtualToken = args.getString(2);
            cardStateAction = args.getString(3);
            walletId = args.getString(4);
        }

        public static void qrScan(JSONArray args) throws JSONException {
            qrcData = args.getString(0);
            Log.d(TAG, "qrcData: " + qrcData);
        }

    }
}
