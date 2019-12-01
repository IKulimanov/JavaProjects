
import javassist.*;

import java.io.IOException;

public class MyCustomClassLoader extends ClassLoader {
    /* Call MyApp.main().
     */
    public static void main(String[] args) throws Throwable {
        MyCustomClassLoader s = new MyCustomClassLoader(new String[]{"."});
        Class c = s.loadClass("App");
        c.getDeclaredMethod("main", new Class[] { String[].class })
                .invoke(null, new Object[] { args });
    }

    private ClassPool pool;

    public MyCustomClassLoader(String[] strings) throws NotFoundException {
        pool = new ClassPool();
        pool.insertClassPath("App.class"); // MyApp.class must be there.
    }

    /* Finds a specified class.
     * The bytecode for that class can be modified.
     */
    protected Class findClass(String name) throws ClassNotFoundException {
        try {
            CtClass cc = pool.get(name);
            // modify the CtClass object here
            byte[] b = cc.toBytecode();
            return defineClass(name, b, 0, b.length);
        } catch (NotFoundException e) {
            throw new ClassNotFoundException();
        } catch (IOException e) {
            throw new ClassNotFoundException();
        } catch (CannotCompileException e) {
            throw new ClassNotFoundException();
        }
    }
}