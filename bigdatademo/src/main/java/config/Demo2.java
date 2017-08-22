package config;

import javax.sound.midi.Soundbank;
import java.util.Map;


/***
 * 测试JVM系统属性和程序输入参数
 * 1.idea 点击编辑配置
 * VM options 设置为　　-Djvm1=aaaa
 * argument 设置为　a1 a2 a3
 * 2.运行程序，可以看到打印信息
 */
public class Demo2 {

    public static void main(String[] args) {
        System.out.println("JVM系统属性");

        for (Map.Entry<Object, Object> entry : System.getProperties().entrySet()) {
            System.out.println(String.format("%s:%s" , entry.getKey() , entry.getValue()));
        }


        System.out.println("------------------");
        System.out.println("程序参数");

        for (String arg : args) {
            System.out.println(arg);
        }
    }
}
