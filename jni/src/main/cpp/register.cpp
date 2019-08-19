#include <jni.h>
#include <string>
#include "register.h"
#include "utils/log_print.h"
#include "utils/encrypt.h"

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_test_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

//=============静态注册============


JNIEXPORT jstring JNICALL Java_com_example_test_utils_EncryptUtil_getVipString(JNIEnv *env, jclass type) {

}


JNIEXPORT jstring JNICALL Java_com_example_test_utils_EncryptUtil_generateKey(JNIEnv *env, jclass type, jstring name) {

}


//=============动态注册=============

JNIEXPORT jstring JNICALL native_dynamic_key(JNIEnv *env, jobject obj, jstring name) {
    char key[KEY_SIZE] = {0};

    memset(key, 0, sizeof(key));

    char temp[KEY_NAME_SIZE] = {0};

    const char *pName = env->GetStringUTFChars(name, NULL);

    if (NULL != name) {
        strcpy(temp, pName);
        char *ret = generateKeyRAS(temp);

        if (NULL != ret) {
            strcpy(key, ret);
        }
        //java的name对象不需要再使用,通知虚拟机回收name
        env->ReleaseStringUTFChars(name, pName);
    }

    return env->NewStringUTF(key);
}


/*
 * typedef struct {
 *
 * const char* name; //Java中函数的名字
 *
 * const char* signature;//符号签名，描述了函数的参数和返回值
 *
 * void* fnPtr;//函数指针，指向一个被调用的函数
 *
 * } JNINativeMethod;
 */
static JNINativeMethod methods[] = {
        {"dynamicGenerateKey", "(Ljava/lang/String;)Ljava/lang/String;", (void *) native_dynamic_key}
};

static int registerNativeMethods(JNIEnv *env, const char *className, JNINativeMethod *gMethods, int numMethods) {

    jclass clazz;
    clazz = env->FindClass(className);
    if (NULL == clazz) {
        return JNI_FALSE;
    }

    if (env->RegisterNatives(clazz, gMethods, numMethods) < 0) {
        return JNI_TRUE;
    }

    return JNI_TRUE;
}

jint registerNatives(JNIEnv *env) {
    const char *className = "com/example/test/utils/EncryptUtil";
    return registerNativeMethods(env, className, methods, sizeof(methods) / sizeof(methods[0]));
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    LOGE("---------------JNI_OnLoad----------\n");

    JNIEnv *env = NULL;
    jint result = -1;

    if (vm->GetEnv((void **) &env, JNI_VERSION_1_4) != JNI_OK) {
        return -1;
    }

    assert(env != NULL);

    if (!registerNatives(env)) {
        return -1;
    }

    return JNI_VERSION_1_4;
}









