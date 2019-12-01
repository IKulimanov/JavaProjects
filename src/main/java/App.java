
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.slf4j.LoggerFactory.getLogger;
import javassist.*;


public class App {
    static public void main (String args[]) throws NotFoundException, CannotCompileException, IllegalAccessException, InstantiationException {

        /*Тест аннотации*/
    AnnotationDemo ad = new AnnotationDemo();
    ad.getID();

        /*Тест загрузчика классов*/
    ClassPool cp = ClassPool.getDefault();
    CtClass cc = cp.get("ForTest");
    CtMethod m = cc.getDeclaredMethod("funcTestTwo");
    m.insertBefore("{ System.out.println(\"ForTest.funcTestTwo():\"); }");
    Class c = cc.toClass();
    ForTest cls = (ForTest)c.newInstance();

        /*Запуск рефлексии*/
    MyReflection Ref = new MyReflection();
    Ref.startReflect(ForTestTwo.class,"class");

    }

}


