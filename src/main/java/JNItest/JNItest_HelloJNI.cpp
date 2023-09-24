#include <jni.h>
//A simple hello world function to test the JNI process, will be replaced with ping functionality
JNIEXPORT void JNICALL Java_JNItest_HelloJNI_sayHello
  (JNIEnv* env, jobject thisObject) {
    std::cout << "Hello from C++ !!" << std::endl;
}
