//
// Created by eugene.chen on 2019/4/4.
//

#include <cstring>
#include <cstdlib>
#include "encrypt.h"
#include "log_print.h"


char *generateKeyRAS(char *name) {
    //安全性检查
    if (NULL == name || strlen(name) > KEY_NAME_SIZE) {
        LOGE("function generateKey must have a ok name!\n");
        return NULL;
    }

    int index = 0;
    int loop = 0;
    char temp[KEY_SIZE] = {"\0"};

    //清空数组
    memset(temp, 0, sizeof(temp));
    //将传入进来的name拷贝到临时空间
    strcpy(temp, name);
    //进行通过name转化生成的key的逻辑
    for (int index = 0; index < KEY_SIZE - 1; ++index) {
        temp[index] = (char) (rand() % 20 + 80);
        LOGE("---------------temp[%d]=%c", index, temp[index]);
    }
    return temp;
}





