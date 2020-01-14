public class GenericT<T>  {
        T ob; // объявление объекта типа T

        // Передать конструктору ссылку на объект типа T
        GenericT(T o) {
            ob = o;
        }

        // Вернуть ob
        T getob() {
            return ob;
        }

        // Показать тип T
        String showType() {
            return "Тип T: " + ob.getClass().getSimpleName();
        }
        public <G> void print(G item){
            System.out.println(item);
        }
}
