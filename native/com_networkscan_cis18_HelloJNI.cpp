#include "com_networkscan_cis18_HelloJNI.h"
#include <iostream>

JNIEXPORT void Java_com_networkscan_cis18_HelloJNI_sayHello(JNIEnv *, jobject) {
    std::cout << "Hello, Adrienne!" << std::endl;
}