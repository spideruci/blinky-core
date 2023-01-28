package hyr.tests;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import org.junit.Test;

public class TryBlinky2 {
    static ArrayList<String> classPath = new ArrayList<String>();
    static ArrayList<String> className = new ArrayList<>();
//    static ArrayList<String> classPath = new ArrayList<String>() {
//        {
//            add("/Users/hyr/Code/IdeaProjects/TestRunner/target/classes/");
//            add("/Users/hyr/Code/IdeaProjects/ToyProject/target/classes/");
//            add("/Users/hyr/Code/IdeaProjects/ToyProject/target/test-classes/");
//        }
//    };
//
//    static ArrayList<String> className = new ArrayList<>() {
//        {
//            add("TestJunit");
////    loadClassNames.add("TestJunit2")
////    loadClassNames.add("TestJunit3")
////    loadClassNames.add("CalculatorTest")
//        }
//    };


    public static void main(String[] args) throws NoSuchMethodException, MalformedURLException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        classPath.add("/Users/hyr/Code/IdeaProjects/TestRunner/target/classes/");
        classPath.add("/Users/hyr/Code/IdeaProjects/ToyProject/target/classes/");
        classPath.add("/Users/hyr/Code/IdeaProjects/ToyProject/target/test-classes/");
        className.add("TestJunit");

        URL[] urls = new URL[classPath.size()];
        int cnt = 0;
        for (String s : classPath) {
            File f = new File("/Users/hyr/Code/IdeaProjects/ToyProject/target/classes");
            urls[cnt] = f.toURI().toURL();
            cnt++;
        }


        ClassLoader cl = new URLClassLoader(urls);

        for (String s : className) {
            Class cls = cl.loadClass(s);
            for (Method m : cls.getDeclaredMethods()) {
                boolean present = m.isAnnotationPresent(Test.class);
                if (present) {
                    try {
                        m.invoke(cls.newInstance());
                    } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}



