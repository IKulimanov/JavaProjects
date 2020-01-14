import org.slf4j.Logger;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;

import static org.slf4j.LoggerFactory.getLogger;


/**
 * Класс реализующий рефлекию
 * @author Kulimanov Ivan
 * @version 1.0
 */
public class MyReflection{
    /*Буфер хранящий всю информацию о исследуемом классе*/
    StringBuffer bufReflect;
    int lengthBuf;
    /*Получение буфера*/
    public StringBuffer getBufReflect() {
        return bufReflect;
    }
    /*Добавление строки в конец буфера*/
    public void setBufReflect(String str) {
        isertBuf(str);
    }
    /*Конструктор*/
    public MyReflection() {
        this.bufReflect = new StringBuffer("");
        this.lengthBuf = 0;
    }
/**
 *Поиск модификатора класса, функция добавляет в буфер модификатор
 * @param numModificator - модификатор типа int
 * @see MyReflection#MyReflection()
 */
    private void choise_modifier(int numModificator)
    {
        if (Modifier.isPublic(numModificator)){
            isertBuf("public");
        }
        if (Modifier.isPrivate(numModificator)) {
            isertBuf("private");
        }
        if (Modifier.isProtected(numModificator)) {
            isertBuf("protected");
        }
        if (Modifier.isFinal(numModificator)){
            isertBuf("final");
        }
        if (Modifier.isStatic(numModificator)){
            isertBuf("static");
        }
    }
/**
* Функция добавляет в стринг буфер строку и возвращает смещение
* @param newStr - строка которую необходимо добавить
* @return  true - записано в буфер, false - не записано в буфер
 */
    private boolean isertBuf(String newStr)
    {
        if ((newStr.indexOf(' ') == -1)&&(newStr != "\n")&&(newStr !="(")){
            newStr = newStr + ' ';
        }
        try {
            this.bufReflect.insert(this.lengthBuf, newStr);
        }catch(IndexOutOfBoundsException e){
            /*Ошибка индекса*/
            return false;
            }
        this.lengthBuf = this.bufReflect.length();
        return true;
    }
/**
 *Поиск наследуемого класса
 *@param  CurClass - класс у которого ищется интерфейс
 *@return  Возвращает предка типа Class
 */
    private Class getSuper(Class CurClass)
    {
        Class Super = null;
        Super = CurClass.getSuperclass();
        if (Super != null)
        {
            if (Super.getName() != "java.lang.Object") {
                isertBuf("extends");
                isertBuf(Super.getName());
            }
        }
        return Super;
    }

/**
 *Поиск интерфейса у класса
 *@param  CurClass - класс у которого ищется интерфейс
 *@return  Возвращает массив интерфейсов Class[]
 */
    private Class[] getInterf(Class CurClass)
    {
        Class[] InterfaceT = new Class[0];
        InterfaceT = CurClass.getInterfaces();
        if (InterfaceT.length>0) {
            if (CurClass.isInterface())
            {
                isertBuf("extends");
            }
            else
            {
                isertBuf("implements");
            }
            for ( int i=0;InterfaceT.length>i;i++ ) {
                if (i>0)
                {
                    isertBuf(",");
                }
                isertBuf(InterfaceT[i].getName());
            }
        }
        return InterfaceT;
    }
/**
* Получение полей класса
*@param  CurClass - класс у которого ищутся поля
 */
    private void getFieldsClass(Class CurClass)
    {
        Field[] FieldsClass;
        FieldsClass = CurClass.getDeclaredFields();
        for ( int i=0;FieldsClass.length>i;i++ ) {
            isertBuf("  ");
            choise_modifier(FieldsClass[i].getModifiers());
            isertBuf(FieldsClass[i].getType().getSimpleName());
            isertBuf(FieldsClass[i].getName());
            isertBuf(";\n");
        }
    }

/**
 * Поиск методов в классе
 *@param  CurClass - класс у которого ищутся методы
 */
    private void getMeth(Class CurClass)
    {
        Method[] MethodsT;
        MethodsT = CurClass.getDeclaredMethods();
        for(int i = 0; i< MethodsT.length; i++) {
            isertBuf("  ");
            getAnnot(MethodsT[i]);
            choise_modifier(MethodsT[i].getModifiers());
            isertBuf(MethodsT[i].getReturnType().getSimpleName());
            isertBuf(MethodsT[i].getName());
            getParamType(MethodsT[i]);
        }
    }
/**
 *Поиск параметров метода
 *@param  Meth -  метод параметры которого будут добавленны в буфер
 */
    private void getParamType(Method Meth)
    {
        Parameter[] Param = Meth.getParameters();
        isertBuf("(");
        if(Meth.getParameterCount() > 0) {
            for (int i = 0; i < Meth.getParameterCount(); i++) {
                isertBuf(Param[i].getType().getSimpleName());
                if ((Meth.getParameterCount() - 1) > i) {
                    isertBuf(Param[i].getName() + ",");
                } else
                    isertBuf(Param[i].getName() + ")\n");
            }
        }
        else
        {
            isertBuf(")\n");
        }
    }
/**
 *Поиск аннтотаций
 *@param  Meth - метод у которого ищется аннотация и записывается в буфер
 */
    private void getAnnot( Method Meth)
    {
        Annotation[] Annot;
        Annot=Meth.getAnnotations();
        for(int i = 0; i<Annot.length; i++) {
            isertBuf("@" + Annot[i].annotationType().getName());
            isertBuf("\n");
        }
    }

/**
* Функция выводит в консоль класс, все его поля и методы, а так же интерфейс и классы которые он наследует
*@param  BaseClass - класс который необходимо исследовать
 */
    private void get_classInfo(Class BaseClass)
    {

        Class Super = null;
        Class[] InterfaceT = new Class[0];
        //Ищем модификатор класса
        choise_modifier(BaseClass.getModifiers());
        //Ищем предка если исследуем класс
        if (!BaseClass.isInterface()) {
            //Выводим название класса
            isertBuf("class");
            isertBuf(BaseClass.getName());
            Super = getSuper(BaseClass);
        }
        else
        {
            //Выводим название интерфейса
            isertBuf("interface");
            isertBuf(BaseClass.getName());
        }
        //Ищем интерфейс
        InterfaceT = getInterf(BaseClass);
        isertBuf("{\n");
        //Ищем поля класса
        getFieldsClass(BaseClass);
        //Ищем методы класса
        getMeth(BaseClass);
        isertBuf("}\n");
        //Если есть родитель то ищем все его поля, методы и т.д.
        if (Super != null) {
            if (Super.getName() != "java.lang.Object") {
                get_classInfo(Super);
            }
         }
        //Если у интерфейса есть предок то ищем все его поля
        if (InterfaceT.length != 0)
        {
            boolean asd = InterfaceT[0].isInterface();
            for(int i = 0; i<InterfaceT.length; i++)
                get_classInfo(InterfaceT[i]);
        }
    }

    /**
     * Запуск рефлексии результат будет выведен логом в консоль
     * @param BaseClass - класс который удет подвержен рефлексии
     */

    public void startReflect(Class BaseClass)
    {
        Logger logger = getLogger(MyReflection.class);
        logger.info("Start Reflection");
        get_classInfo(BaseClass);
        logger.info("\n " + this.bufReflect.toString());
        logger.info("Stop Reflection");
    }

}
