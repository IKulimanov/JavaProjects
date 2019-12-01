import org.slf4j.Logger;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;

import static org.slf4j.LoggerFactory.getLogger;



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
/*
 *Поиск модификатора класса, функция добавляет в буфер модификатор
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
/*
* Функция добавляет в стринг буфер строку и возвращает смещение
* newStr - строка которую необходимо добавить
* возвращает удачность выполнения
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
/*
 *Поиск наследуемого класса
 * CurClass - класс у которого ищется интерфейс
 * Возвращает предка
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

/*
 *Поиск интерфейса у класса
 * CurClass - класс у которого ищется интерфейс
 * clorit - строка если в которой записано либо слово "class" либо "interface"
 * Возвращает массив интерфейсов
 */
    private Class[] getInterf(Class CurClass, String clorit)
    {
        Class[] InterfaceT = new Class[0];
        InterfaceT = CurClass.getInterfaces();
        if (InterfaceT.length>0) {
            if (clorit == "interface")
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
/*
*Получение полей класса
* CurClass - класс у которого ищутся поля
 */
    private void getFieldsClass(Class CurClass)
    {
        Field[] FieldsClass;
        FieldsClass = CurClass.getDeclaredFields();
        for ( int i=0;FieldsClass.length>i;i++ ) {
            choise_modifier(FieldsClass[i].getModifiers());
            isertBuf(FieldsClass[i].getType().getSimpleName());
            isertBuf(FieldsClass[i].getName());
            isertBuf(";\n");
        }
    }

/*
 * Поиск методов в классе
 * CurClass - класс у которого ищутся методы
 */
    private void getMeth(Class CurClass)
    {
        Method[] MethodsT;
        MethodsT = CurClass.getDeclaredMethods();
        for(int i = 0; i< MethodsT.length; i++) {
            getAnnot(MethodsT[i]);
            choise_modifier(MethodsT[i].getModifiers());
            isertBuf(MethodsT[i].getReturnType().getSimpleName());
            isertBuf(MethodsT[i].getName());
            getParamType(MethodsT[i]);
        }
    }
/*
 *Поиск параметров метода
 * Meth -  метод
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
/*
 *Поиск аннтотаций
 * Meth - метод у которого ищется аннотация
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

/*
* Функция выводит в консоль класс, все его поля и методы, а так же интерфейс и классы которые он наследует
* BaseClass - класс который необходимо исследовать
* clorit - если передается класс то следует указать class, если интерфейс то interface
 */
    private void get_classInfo(Class BaseClass, String clorit)
    {
        Class Super = null;
        Class[] InterfaceT = new Class[0];
        //Ищем модификатор класса
        choise_modifier(BaseClass.getModifiers());
        //Выводим название класса
        isertBuf(clorit);
        isertBuf(BaseClass.getName());
        //Ищем предка если исследуем класс
        if (clorit != "interface") {
            Super = getSuper(BaseClass);
        }
        //Ищем интерфейс
        InterfaceT = getInterf(BaseClass,clorit);
        isertBuf("{\n");
        //Ищем поля класса
        getFieldsClass(BaseClass);
        //Ищем методы класса
        getMeth(BaseClass);
        isertBuf("}\n");
        //Если есть родитель то ищем все его поля, методы и т.д.
        if (Super != null) {
            if (Super.getName() != "java.lang.Object") {
                get_classInfo(Super, "class");
            }
         }
        //Если у интерфейса есть предок то ищем все его поля
        if (InterfaceT.length != 0)
        {
            for(int i = 0; i<InterfaceT.length; i++)
                get_classInfo(InterfaceT[i],"interface");
        }
    }


    public void startReflect(Class BaseClass, String clinter)
    {
        Logger logger = getLogger(MyReflection.class);
        logger.info("Start Reflection");
        get_classInfo(BaseClass, clinter);
        logger.info("\n" + this.bufReflect.toString());
        logger.info("Stop Reflection");
    }

}
