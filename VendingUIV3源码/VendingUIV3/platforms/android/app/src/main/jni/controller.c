#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <errno.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <string.h>
#include <stdint.h>
#include <termios.h>
#include <android/log.h>
#include <sys/ioctl.h>
#include <jni.h>
int fd=0;
int sfd = 0;
JNIEXPORT jint JNICALL Java_edu_acmd_vendingui_jni_Controller_openLed
(JNIEnv *env, jobject obj)
{
    if(fd<=0)fd = open("/dev/leds", O_RDWR|O_NDELAY|O_NOCTTY);
    if(fd <=0 )__android_log_print(ANDROID_LOG_INFO, "serial", "open /dev/leds Error");
        else __android_log_print(ANDROID_LOG_INFO, "serial", "open /dev/leds Sucess fd=%d",fd);
    return fd;
}

JNIEXPORT jint JNICALL Java_edu_acmd_vendingui_jni_Controller_closeLed
(JNIEnv *env, jobject obj)
{
    if(fd > 0)close(fd);
    return fd;
}

JNIEXPORT jint JNICALL Java_edu_acmd_vendingui_jni_Controller_ledCtrl
(JNIEnv *env, jobject obj, jint num, jint en)
{
    ioctl(fd, en, num);
    return fd;
}
JNIEXPORT jint JNICALL Java_edu_acmd_vendingui_jni_Controller_closeSerial
(JNIEnv *env, jobject obj)
{
    if(sfd > 0) close(sfd);
    sfd = 0;
    __android_log_print(ANDROID_LOG_INFO, "serial", "serial close");
    return sfd;
}
JNIEXPORT jint JNICALL Java_edu_acmd_vendingui_jni_Controller_openSerial
(JNIEnv *env, jobject obj)
{
    if(sfd <= 0){
        //open("/dev/ttySAC0",O_RDWR|O_NDELAY|O_NOCTTY);
        //open("/dev/ttySAC1",O_RDWR|O_NDELAY|O_NOCTTY);
        //open("/dev/ttySAC2",O_RDWR|O_NDELAY|O_NOCTTY);
        sfd = open("/dev/ttySAC3",O_RDWR|O_NDELAY|O_NOCTTY);
        __android_log_print(ANDROID_LOG_INFO, "serial", "serial open ok sfd=%d", sfd);
        //
        struct termios ios;
        tcgetattr(sfd, &ios);
        //OPSOT:打开输出处理功能 ONLCR:将输出中的换行符转换为回车符 OCRNL:将回车符转换为换行符
        //ONOCR:第０行不输出回车符
        //ONLRET:不输出回车符
        //NLDLY:换行符延时选择
        //CRDLY:回车符延时
        //TABDLY:制表符延时
        ios.c_oflag &= ~(OPOST);
        //
        //ios.c_iflag &= ~(ICRNL | IXON);
       // ios.c_iflag &= ~(INLCR|IGNCR|ICRNL);
       // ios.c_iflag &= ~(ONLCR|OCRNL);
        tcflush(sfd,TCIFLUSH);
        cfsetospeed(&ios, B9600);
        ios.c_cflag |= (CLOCAL | CREAD);
        //ios.c_cflag &= ~PARENB;
        //ios.c_cflag &= ~CSTOPB;
        ios.c_cflag &= ~CSIZE;
        ios.c_cflag |= CS8;
        ios.c_lflag = 0;
        tcsetattr( sfd, TCSANOW, &ios);
    }else{
        __android_log_print(ANDROID_LOG_INFO, "serial", "serial open error sfd= %d", sfd);
    }
    return sfd;
}

JNIEXPORT jint JNICALL Java_edu_acmd_vendingui_jni_Controller_sendBySerial
(JNIEnv *env, jobject obj, jcharArray buf, unsigned int buflen)
{
    //jsize len = buflen;
    size_t len = buflen;
    if(len <= 0)
        return -1;

    //jintArray array = (*env)-> NewIntArray(env, len);

    //if(array == NULL){ array=NULL;return -1;}

    //jint *body = (*env)->GetIntArrayElements(env, buf, 0);
    jchar *body = (*env) -> GetCharArrayElements(env, buf, 0);

    jint i = 0;
    char num[len];

    for (; i < len + 1; i++){
        num[i] = body[i];
        __android_log_print(ANDROID_LOG_INFO, "serial", "loop");
    }

    write(sfd, num, len);
    __android_log_print(ANDROID_LOG_INFO, "serial", "send: %s %d", num, len);
    //array = NULL;

    return 0;

}




