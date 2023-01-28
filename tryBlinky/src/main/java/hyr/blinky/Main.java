package hyr.blinky;

public class Main {
    public static void main(String[] args) {
        print233();
//        System.out.println(getVersion());
    }

    public static void print233() {
        System.out.println("233");
//        printStackTrace();
    }

    public static void printStackTrace(){
        Thread.dumpStack();
    }

//    public static int getVersion() {
//        String version = System.getProperty("java.version");
//        if (version.startsWith("1.")) {
//            version = version.substring(2, 3);
//        } else {
//            int dot = version.indexOf(".");
//            if (dot != -1) {
//                version = version.substring(0, dot);
//            }
//        }
//        return Integer.parseInt(version);
//    }
}
