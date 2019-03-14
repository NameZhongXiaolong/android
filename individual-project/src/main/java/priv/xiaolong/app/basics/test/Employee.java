package priv.xiaolong.app.basics.test;

import java.io.Serializable;

public class Employee implements Serializable {

	public int num;
	public String name;

	public Employee(int num, String name) {
		this.num = num;
		this.name = name;
	}

	@Override
	public String toString() {
		return "Employee{" +
				"num=" + num +
				", name='" + name + '\'' +
				'}';
	}
}
