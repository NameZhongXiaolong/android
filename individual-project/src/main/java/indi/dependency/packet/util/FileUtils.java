package indi.dependency.packet.util;

import android.annotation.SuppressLint;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import priv.xiaolong.app.basics.test.Employee;

/**
 * Created by ZhongXiaolong on 2017/8/18 16:17.
 */
public class FileUtils {

    /**
     * 对象转成流保存到本地
     */
    @SuppressLint("NewApi")
    public static void save(File file) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            ArrayList<Employee> list = new ArrayList<>();
            list.add(new Employee(18, "张三"));
            list.add(new Employee(19, "李四"));
            list.add(new Employee(20, "王五"));
            list.add(new Employee(21, "赵六"));
            list.add(new Employee(22, "田七"));
            list.add(new Employee(33, "王八"));
            oos.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 从本地读取流对象
     */
    @SuppressLint("NewApi")
    public static void read(File file) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            ArrayList<Employee> list = (ArrayList<Employee>) ois.readObject();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                sb.append(list.get(i).toString());
            }
            System.out.println(sb.toString());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
