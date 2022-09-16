package nishvand;

public class Cache {
    static protected Object[] cache = new Object[ 8192 ];

    public static int put(Object o) { // превращаем объект в индекс
        int nullptr = -1;

        for (int i = 0; i < cache.length; i++) { // если он уже есть то вернем его индекс
            if(cache[i] == null) { nullptr = i; continue;} // находим пустую ячейку, на тот случай если объекта нет в кэше
            if(cache[i].equals(o)) // если она есть то возвращаем его index
                return i;
        }

        cache[nullptr] = o; // иначе помещаем его в ближайшую пустую ячейку
        return nullptr; // и возвращаем ее index
    }

    // возвращаем объект из кэша
    public static Object get(int index){
        return cache[index];
    }
    // изменяем объект в кэше
    public static void set(Object o, int index){
        cache[index] = o;
    }
}