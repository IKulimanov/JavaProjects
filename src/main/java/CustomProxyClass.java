import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

//public class CustomProxyClass {
    interface If {
        void originalMethod(String s);
    }

    class Original implements If {
        public void originalMethod(String s) {
            System.out.println(s);
        }
    }

     class Handler implements InvocationHandler {
        private final If original;

        public Handler(If original) {
            this.original = original;
        }

        public Object invoke(Object proxy, Method method, Object[] args)
                throws IllegalAccessException, IllegalArgumentException,
                InvocationTargetException {
            System.out.println("BEFORE");
            method.invoke(original, args);
            System.out.println("AFTER");
            return null;
        }
    }
//}
