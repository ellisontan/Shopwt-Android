#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring
JNICALL
Java_top_yokey_shopwt_base_BaseApplication_getServerAesKey(JNIEnv *env, jobject) {
    std::string key = "shopwtserverkeys";
    return env->NewStringUTF(key.c_str());
}

extern "C"
JNIEXPORT jstring
JNICALL
Java_top_yokey_shopwt_base_BaseApplication_getLocalAesKey(JNIEnv *env, jobject) {
    std::string key = "shopwtslocalkeys";
    return env->NewStringUTF(key.c_str());
}
