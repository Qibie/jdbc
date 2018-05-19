import java.lang.reflect.*;
import java.util.*;

public class Reflection {
}

/**
 * Class类
 * @Author:qibie
 * @Date:2018-05-19
 */
class ClassDemo {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        // 第一种获取Class类对象
        Random generator = new Random();
        // Object类中的getClass( )方法将会返回一个Class类型的实例
        Class c1 = generator.getClass();
        // 最常用的Class方法是getName(), 这个方法将返回类的名字
        String name = c1.getName();
        System.out.println(name);

        // 第二种获取Class类对象
        // 调用静态方法Class.forName()获得类名对应的Class对象
        // 前提：如果类名保存在字符串中，并可在运行中改变，就可以使用这个方法
        // 传递一个ClassNotFoundException异常
        String className = "java.util.Random";
        Class c2 = Class.forName(className);
        System.out.println(c2.getName());

        // 第三种获取Class类对象
        // 如果T是任意的Java类型(或void关键字)，T.class将代表匹配的类对象
        // ps:一个Class 对象实际上表示的是一个类型，而这个类型未必一定是一种类
        // 例如 ，int不是类,但int.class是一个 Class类型的对象
        Class cl1 = Random.class;
        Class cl2 = int.class;
        Class cl3 = Double[].class;

        // 鉴于历史原因，getName方法在应用于数组类型的时候会返回一个很奇怪的名字
        // [Ljava.lang.Double;
        System.out.println(Double[].class.getName());
        // [I
        System.out.println(int[].class.getName());

        // 可以利用 == 运算符实现两个类对象比较的操作。
        if (generator.getClass() == Random.class) {
            System.out.println(true);
        }

        // 还有一个很有用的方法newlnstance(),可以用来动态地创建一个类的实例
        // 需要处理IllegalAccessException, InstantiationException异常
        // newlnstance方法调用默认的构造器(没有参数的构造器)初始化新创建的对象。如果这个类没有默认的构造器，就会抛出一个异常
        Object obj = generator.getClass().newInstance();

        // 将forName与newlnstance配合起来使用，可以根据存储在字符串中的类名创建一个对象
        try {
            String s = "java.util.Random";
            Object m = Class.forName(s).newInstance();
            System.out.println(m);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/**
 * 程序显示如何打印一个类的全部信息的方法。这个程序将提醒用户输入类名，然后输出类中所有的方法和构造器的签名，以及全部域名 。
 * 这个程序可以分析 Java 解释器能够加载的任何类，而不仅仅是编译程序时可以使用的类。
 * 在下一章中，还将使用这个程序查看 Java 编译器自动生成的内部类 。
 */
class ReflectionTest {
    public static void main(String[] args) {
        // read class name from command line args or user input
        String name;
        if (args.length > 0) {
            name = args[0];
        } else {
            Scanner in = new Scanner(System.in);
            System.out.println("Enter class name(e.g. java.util.Date): ");
            name = in.next();
        }

        try {
            // print class name and superclass name (if != Object)
            Class cl = Class.forName(name);
            Class supercl = cl.getSuperclass();
            String modifiers = Modifier.toString(cl.getModifiers());
            if (modifiers.length() > 0) {
                System.out.print(modifiers + " ");
            }
            System.out.print("class " + name);
            if (supercl != null && supercl != Object.class) {
                System.out.print(" extends " + supercl.getName());
            }
            System.out.print("\n{\n");
            printConstructors(cl);
            System.out.println();
            printMethods(cl);
            System.out.println();
            printFields(cl);
            System.out.println("}");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    /**
     * Prints all constructors of a class
     * @param cl a class
     */
    public static void printConstructors(Class cl) {
        Constructor[] constructors = cl.getDeclaredConstructors();

        for (Constructor c : constructors) {
            String name = c.getName();
            System.out.print("  ");
            String modifiers = Modifier.toString(c.getModifiers());
            if (modifiers.length() > 0) {
                System.out.print(modifiers + " ");
            }
            System.out.print(name + "(");

            // print parameter types
            Class[] paramTypes = c.getParameterTypes();
            for (int j = 0; j < paramTypes.length; j++) {
                if (j > 0) {
                    System.out.print(", ");
                }
                System.out.print(paramTypes[j].getName());
            }
            System.out.println(");");
        }
    }

    /**
     * Prints all methods of a class
     * @param cl a class
     */
    public static void printMethods(Class cl) {
        Method[] methods = cl.getDeclaredMethods();

        for (Method m : methods) {
            Class retType = m.getReturnType();
            String name = m.getName();

            System.out.println("  ");
            // print modifiers, return type and method name
            String modifiers = Modifier.toString(m.getModifiers());
            if (modifiers.length() > 0) {
                System.out.print(modifiers + " ");
            }
            System.out.print(retType.getName() + " " + name + "(");
            // print parameter types
            Class[] paramTypes = m.getParameterTypes();
            for (int j = 0; j < paramTypes.length; j++) {
                if (j > 0) {
                    System.out.print(", ");
                }
                System.out.print(paramTypes[j].getName());
            }
            System.out.println(");");
        }
    }

    /**
     * Prints all fields of a class
     * @param cl a class
     */
    public static void printFields(Class cl) {
        Field[] fields = cl.getDeclaredFields();

        for (Field f : fields) {
            Class type = f.getType();
            String name = f.getName();
            System.out.println("  ");
            String modifiers = Modifier.toString(f.getModifiers());
            if (modifiers.length() > 0) {
                System.out.print(modifiers + " ");
            }
            System.out.println(type.getName() + " " + name + ";");
        }
    }
}

class ObjectAnalyzerTest {
    public static void main(String[] args) {
        ArrayList<Integer> squares = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            squares.add(i * i);
            System.out.println(new ObjectAnalyzer().toString(squares));
        }
    }
}

/**
 * 在运行时使用反射分析对象
 */
class ObjectAnalyzer {
    private ArrayList<Object> visited = new ArrayList<>();

    /**
     * Converts an object to a string representation that lists all fields.
     * @param obj an object
     * @return a string with the object's calss name and all field names and values
     */
    public String toString(Object obj) {
        if (obj == null) {
            return "null";
        }
        if (visited.contains(obj)) {
            return "...";
        }
        visited.add(obj);
        Class cl = obj.getClass();
        if (cl == String.class) {
            return (String) obj;
        }
        if (cl.isArray()) {
            String r = cl.getComponentType() + "[]{";
            for (int i = 0; i < Array.getLength(obj); i++) {
                if (i > 0) {
                    r += ",";
                }
                Object val = Array.get(obj, i);
                if (cl.getComponentType().isPrimitive()) {
                    r += val;
                } else {
                    r += toString(val);
                }
            }
            return r + "}";
        }

        String r = cl.getName();
        // inspect the fields of this class and superclasses
        do {
            r += "{";
            Field[] fields = cl.getDeclaredFields();
            AccessibleObject.setAccessible(fields, true);
            // get the names and values of all fields
            for (Field f : fields) {
                if (!Modifier.isStatic(f.getModifiers())) {
                    if (!r.endsWith("[")) {
                        r += ",";
                    }
                    r += f.getName() + "=";
                    try {
                        Class t = f.getType();
                        Object val = f.get(obj);
                        if (t.isPrimitive()) {
                            r += val;
                        } else {
                            r += toString(val);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            r += "]";
            cl = cl.getSuperclass();
        } while (cl != null);
        return r;
    }
}

/**
 * 使用反射编写泛型数组代码
 * 显示了两个扩展数组的方法
 * 将badCopyOf的返回值进行类型转换将会抛出一个异常
 */
class CopyOfTest {
    public static void main(String[] args) {
        int[] a = {1, 2, 3};
        a = (int[]) goodCopyOf(a, 10);
        System.out.println(Arrays.toString(a));

        String[] b = {"Tom", "Dick", "Harry"};
        b = (String[]) goodCopyOf(b, 10);
        System.out.println(Arrays.toString(b));

        System.out.println("The following call will generate an exception.");
        b = (String[]) badCopyOf(b, 10);
    }

    /**
     * This method attempts to grow an array by allocating a new array and copying all elements.
     * @ param a the array to grow
     * @ param newLength the new length
     * return a larger array that contains all elements of a . However , the returned array has
     * type ObjectQ , not the same type as a
    */
    // not useful
    public static Object[] badCopyOf(Object[] a, int newLength) {
        Object[] newArray = new Object[newLength];
        System.arraycopy(a, 0 , newArray, 0, Math.min(a.length, newLength));
        return newArray;
    }
    /**
     * This method grows an array by allocating a new array of the same type and
     * copying all elements .
     * @ param a the array to grow . This can be an object array or a primitive
     * type array
     * return a larger array that contains all elements of a .
     */
    public static Object goodCopyOf(Object a, int newLength) {
        Class cl = a.getClass();
        if (!cl.isArray()) {
            return null;
        }
        Class componentType = cl.getComponentType();
        int length = Array.getLength(a);
        Object newArray = Array.newInstance(componentType, newLength);
        System.arraycopy(a, 0, newArray, 0, Math.min(length, newLength));
        return newArray;
    }
}