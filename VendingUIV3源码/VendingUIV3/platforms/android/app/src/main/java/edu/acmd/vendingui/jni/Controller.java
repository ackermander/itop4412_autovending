package edu.acmd.vendingui.jni;

/**
 * Created by ACMDIWATYO on 2018/3/17.
 */

public class Controller {
    static {
        System.loadLibrary("ctrl");
    }

    public static final native int openLed();
    public static final native int closeLed();
    public static final native int ledCtrl(int num, int en);
    public static final native int openSerial();
    public static final native int closeSerial();
    private static final native int sendBySerial(int[] buf, int buflen);

    public static final int writeToSerial(String text){
        char[] cs = text.toCharArray();
        byte[] bytes = text.getBytes();

        int[] c2i = new int[cs.length];
        for(int i = 0; i < cs.length; i++){
            c2i[i] =(int) cs[i];
        }
        int fd = sendBySerial(c2i, c2i.length);
        return fd;
    }
}
