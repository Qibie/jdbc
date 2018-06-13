/**
 * 
 */
package cn.itcast.jdbc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import cn.itcast.jdbc.domain.User;

/**
 * Java反射技术入门
 * @author qibie
 * @createDate:2018-05-19
 * @ProjectName:jdbc
 */
public class ReflectTest {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// User user = (User) create(User.class);
		// System.out.println(user);
		Class clazz = User.class;
		// clazz = Bean.class;
		Object obj = create(clazz);
		System.out.println(obj);
		System.out.println("-----------------------");
		invoke1(obj, "showName");
		System.out.println("-----------------------");
		field(clazz);
		System.out.println("-----------------------");
		annon(clazz);
	}

	// 通过类构造实例
	static Object create(Class clazz) throws Exception {
		// 获取构造器中参数为String类型的构造方法
		Constructor conn = clazz.getConstructor(String.class);
		Object obj = conn.newInstance("test name");
		return obj;
	}

	// 通过方法名调用实例的方法
	static void invoke1(Object obj, String methodName) throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		// getDeclaredMethods()方法拿到当前类的公有私有方法,拿不到超类继承的方法
		Method[] ms = obj.getClass().getDeclaredMethods();
		// getMethods()方法拿到公共方法，包括从超类继承的方法
		ms = obj.getClass().getMethods();
		for (Method m : ms) {
			// System.out.println(m.getName());
			if (methodName.equals(m.getName())) {
				// 方法在方法区，实例在堆，实例传给方法取调用
				m.invoke(obj, null);
			}
		}

		// 精确的获取方法名，排除方法重载干扰
		Method m = obj.getClass().getMethod(methodName, null);
		m.invoke(obj, null);
	}
	
	// 获取属性Field
	static void field(Class clazz) throws Exception {
		// getDeclaredFields()方法拿到当前类的公有私有属性Field
		Field[] fs = clazz.getDeclaredFields();
		// getFields()方法拿到公有属性
		fs = clazz.getFields();
		for (Field f : fs) {
			System.out.println(f.getName());
		}
	}
	
	// 获取注解
	static void annon(Class clazz) throws Exception {
		Annotation[] as = clazz.getAnnotations();
		System.out.println(Arrays.toString(as));
	}
}
