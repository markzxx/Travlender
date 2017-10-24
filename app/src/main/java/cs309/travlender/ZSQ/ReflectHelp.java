package cs309.travlender.ZSQ;

import com.googlecode.openbeans.IntrospectionException;
import com.googlecode.openbeans.PropertyDescriptor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/**
 * Created by Administrator on 2017/10/23.
 */


public class ReflectHelp {


    public static Object getObjectByConstructor(String className, Class[] intArgsClass, Object[] intArgs) {

        Object returnObj = null;
        try {
            Class classType = Class.forName(className);
            Constructor constructor = classType.getDeclaredConstructor(intArgsClass); //找到指定的构造方法
            constructor.setAccessible(true);//设置安全检查，访问私有构造函数必须
            returnObj = constructor.newInstance(intArgs);
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return returnObj;
    }


    public static void modifyFileValue(Object object, String filedName,
                                       Object filedValue) {
        Class classType = object.getClass();
        Field fild = null;
        try {
            fild = classType.getDeclaredField(filedName);
            fild.setAccessible(true);//设置安全检查，访问私有成员变量必须
            fild.set(object, filedValue);
        } catch (NoSuchFieldException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 访问类成员变量
     *
     * @param object    访问对象
     * @param filedName 指定成员变量名
     * @return Object 取得的成员变量的值
     */
    public static Object getFileValue(Object object, String filedName) {
        Class classType = object.getClass();
        Field fild = null;
        Object fildValue = null;
        try {
            fild = classType.getDeclaredField(filedName);
            fild.setAccessible(true);//设置安全检查，访问私有成员变量必须
            fildValue = fild.get(object);

        } catch (NoSuchFieldException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return fildValue;
    }

    /**
     * 调用类的有参数方法，包括私有
     *
     * @param object     访问对象
     * @param methodName 指定成员变量名
     * @param type       方法参数类型
     * @param value      方法参数指
     * @return Object 方法的返回结果对象
     */
    public static Object useMethod(Object object, String methodName,
                                   Class[] type, Object[] value) {
        Class classType = object.getClass();
        Method method = null;
        Object fildValue = null;
        try {
            method = classType.getDeclaredMethod(methodName, type);
            method.setAccessible(true);//设置安全检查，访问私有成员方法必须
            fildValue = method.invoke(object, value);

        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return fildValue;
    }
    /**
     * 调用类的get方法
     *
     * @param object     访问对象
     * @param methodName 指定成员变量名
     * @return Object 方法的返回结果对象
     */
    public static Object useGetMethod(Object object, String methodName) {
        Class classType = object.getClass();
        Method method = null;
        Object fildValue = null;
        try {
            PropertyDescriptor pd = new PropertyDescriptor(methodName, classType);
            method = pd.getReadMethod();//获得get方法
            if (pd != null) {
                fildValue = method.invoke(object);//执行get方法返回一个Object\

            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return fildValue;
    }
    /**
     * 调用类的set方法
     *
     * @param object     访问对象
     * @param methodName 指定成员变量名
     * @param content 设置的内容
     * @return Object 方法的返回结果对象
     */
    public static void useSetMethod(Object object, String methodName,Object content) {
        Class classType = object.getClass();
        Method method = null;
        try {
            PropertyDescriptor pd = new PropertyDescriptor(methodName, classType);
            method = pd.getWriteMethod();//获得get方法
            if (pd != null) {
                method.invoke(object,content);//执行get方法返回一个Object
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}


