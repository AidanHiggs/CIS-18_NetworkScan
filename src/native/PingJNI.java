public class PingJNI{
    static {
        System.loadLibrary("native");
    }
    public static void main(String[] args) {
        new PingJNI().ping(host.getIpAddress());
    }
    private native void ping(String target);
}
