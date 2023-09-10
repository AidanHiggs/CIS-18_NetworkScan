package JNItest;
public class HelloJNI {

    //static block loads our native library in memory
    static {
        System.loadLibrary("native");
    }

    // Declare a native method sayHello() that receives no arguments and returns void
    //Once JNI process is established this will call ping function
    private native void sayHello();
    public static void main(String[] args) {
        new HelloJNI().sayHello();
    }


}