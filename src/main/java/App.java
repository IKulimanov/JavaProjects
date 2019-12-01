
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
        Logger logger = getLogger(GenericT.class);
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
    Ref.startReflect(ForTestTwo.class);

    logger.info("--------------------------------");
        /*Generic*/
    // Создаём Gen-ссылку для Integer
    GenericT<Integer> iOb;

    //Создаём объект Gen<Integer>
    iOb = new GenericT<Integer>(77);

    // Показать тип данных, используемый iOb
    logger.info(iOb.showType());

    // Получить значение iOb
    int value = iOb.getob();
    logger.info("Значение " + value);

    // Создадим объект Gen для String
    GenericT<String> strOb = new GenericT<String>("Обобщённый текст");

    // Показать тип данных, используемый strOb
    logger.info(strOb.showType());


    // Получить значение strOb
    String str = strOb.getob();
    logger.info("Значение: " + str);
    logger.info("--------------------------------");
    }
}


