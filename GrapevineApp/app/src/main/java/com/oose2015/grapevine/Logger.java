package com.oose2015.grapevine;

import android.util.Log;
import android.widget.Toast;

/**
 * Created by venussoontornprueksa on 11/11/15.
 */
public class Logger {

    /**
     * Makes a debug log
     * @param message
     */
    public static void debugLog(String message) {
        Log.d("gv", "" + message);
    }

    /**
     * Makes a short toast
     * @param message
     */
    public static void shortToast(String message) {
        Toast.makeText(Grapevine.getGrapevineContext(), message + "", Toast.LENGTH_SHORT).show();
    }

    /**
     * Makes a long toast
     * @param message
     */
    public static void longToast(String message) {
        Toast.makeText(Grapevine.getGrapevineContext(), message + "", Toast.LENGTH_LONG).show();
    }
}
