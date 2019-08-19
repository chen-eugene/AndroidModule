//
// Created by eugene.chen on 2019/4/3.
//

#ifndef TEST_REGISTER_H
#define TEST_REGISTER_H

#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif


/*
 *Signature:()Ljava/lang/String;
 *
 * 静态注册
 */
JNIEXPORT jstring JNICALL Java_com_example_test_utils_EncryptUtil_getVipString(JNIEnv *env, jclass type);

/*
 * Signature:(Ljava/lang/String;)Ljava/lang/String;
 *
 * 静态注册
 */
JNIEXPORT jstring JNICALL Java_com_example_test_utils_EncryptUtil_generateKey(JNIEnv *, jclass type, jobject);

/*
 * Signature:(Ljava/lang/String;)Ljava/lang/String;
 *
 * 动态注册
 */
JNIEXPORT jstring JNICALL native_dynamic_key(JNIEnv *env, jobject obj, jstring name);


#ifdef __cplusplus
}
#endif


#endif //TEST_REGISTER_H
