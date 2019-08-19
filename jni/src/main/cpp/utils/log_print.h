//
// Created by eugene.chen on 2019/4/3.
//

#ifndef TEST_LOG_PRINT_H
#define TEST_LOG_PRINT_H

#include <android/log.h>

#define IS_DEBUG

#ifdef IS_DEBUG

#define LOG_TAG "NDK_JNI"

#define LOGV(...) ((void)__android_log_print(ANDROID_LOG_VERBOSE,LOG_TAG,__VA_ARGS__))

#define LOGD(...) ((void)__android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__))

#define LOGE(...) ((void)__android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__))

#else

#define LOGV(LOG_TAG,...) NULL

#define LOGD(LOG_TAG,...) NULL

#define LOGE(LOG_TAG,...) NULL

#endif //IS_DEBUG

#endif //TEST_LOG_PRINT_H
