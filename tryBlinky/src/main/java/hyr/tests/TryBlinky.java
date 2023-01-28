package hyr.tests;

import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 让自定义的MyTest注解起作用
 * 通过反射，扫描TestJunit类中有哪些方法上方加了MyTest注解
 * 如果加@MyTest注解的则执行它
 * 如果没有加@MyTest注解的则不做任何处理
 */
public class TryBlinky {
    public static void main(String[] args) {
        /**
         * 1.获取TestJunit类对应的Class对象
         */
        Class<TestJunit> junitClass = TestJunit.class;
        /**
         * 获取TestJunit类中所有的方法对象
         */
        Method[] methods = junitClass.getMethods();

        /**
         * 遍历所有方法对象查找有与没有@MyTest注解，并做出响应处理
         */
        for (Method method : methods) {
            boolean present = method.isAnnotationPresent(Test.class);
            /**
             * TestJunit类中有@MyTest注解的执行该方法
             */
            if (present) {
                try {
                    method.invoke(junitClass.newInstance());
                } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                    e.printStackTrace();
                }
            } else {
                /**
                 * TestJunit类中没有@MyTest注解的不做任何操作
                 * 此else分支冗余，只是为了做标记，让你们好理解
                 */
            }
        }
        print233();
    }

    public static void print233(){
        System.out.println("233");
    }
}
