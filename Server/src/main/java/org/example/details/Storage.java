package org.example.details;

import org.island.object.StudyGroup;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Инкапсулированный класс для хранения коллекции и работы с ней
 */
public class Storage {
    private final LocalDateTime date;
    private Map<Long, Integer> mapKey = new HashMap<>();
    private LinkedHashMap<Integer, StudyGroup> map = new LinkedHashMap<>();


    public Storage(){
        this.date = getTime();
    }

//    /**
//     * Метод инициализирует коллекцию элементов и коллекцию её ключей с доступом по id элемента
//     * @param map
//     */
//    public void mapInit(LinkedHashMap<Integer, StudyGroup> map){
//        List<StudyGroup> list = new ArrayList<>(map.values());
//        Collections.sort(list);
//        for(Integer key : map.keySet()){
//            this.mapKey.put(map.get(key).getId(), key);
//        }
//        for(StudyGroup el : list){
//            this.map.put(findKey(el.getId()), el);
//        }
//    }
    public void sort(){
        map = map.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new));
    }


    public Collection<StudyGroup> getValues(){
        return map.values();
    }

//    public LinkedHashMap<Integer, StudyGroup> getMap(){
//        return map;
//    }

    public Set<Integer> getKeys(){
        return map.keySet();
    }

    /*
    public int getKey(StudyGroup obj){
        int key = Math.abs(obj.hashCode() / 10000);
        while(mapKey.containsValue(key)){
            key += 5;
        }
        mapKey.put(obj.getId(), key);
        return key;
    }
     */

    /**
     * Метод возвращает ключ к элементу коллекции по id
     * @param id
     * @return key
     */
    public Integer findKey(long id){
        if(mapKey.containsKey(id)){
            return mapKey.get(id);
        }
        return null;
    }

    /**
     * Метод возвращает элемент коллекции по его id
     * @param id
     * @return StudyGroup element
     */
    public StudyGroup getObj(long id){
        for(StudyGroup el : map.values()){
            if(el.getId() == id){
                return el;
            }
        }
        return null;
    }
//    public void putMapKeys(int key, long id){
//        mapKey.put(id, key);
//        List<StudyGroup> list = new ArrayList<>(map.values());
//        Collections.sort(list);
//        for(Integer keyEl : map.keySet()){
//            this.mapKey.put(map.get(key).getId(), key);
//        }
//        for(StudyGroup el : list){
//            this.map.put(findKey(el.getId()), el);
//        }
//    }
//    /**
//     * Взаимодействие команды update_id с коллекцией
//     * @param id
//     * @param el
//     */
//    public String replaceElement(long id, StudyGroup el){
//        if(map.replace(mapKey.get(id), getObj(id), el)){
//            sort();
//            return ("замена прошла успешно, " + map.get(mapKey.get(id)));
//        }
//        else{
//            return ("Замена не удалась, что-то пошло не так");
//        }
//
//    }

    /**
     * Взаимодействие команды clear с коллекцией
     */
    public String clear(){
        if(map.isEmpty()){
            return "Коллекция и так пустует";
        }
        else{
            map.clear();
            return "Коллекция успешно очищена";
        }

    }
//    /**
//     * Взаимодействие команды remove_key с коллекцией
//     * @param key
//     */
//    public boolean removeElement(int key){
//        if(map.containsKey(key)){
//            map.remove(key);
//            return true;
//        }
//        else{
//            return false;
//        }
//
//
//    }

    /**
     * Метод для фиксирования времени создания коллекции
     * @return LocalDateTime
     */
    public LocalDateTime getTime(){
        return LocalDateTime.now();
    }
    public int getSize(){
        return map.size();
    }
    public Object getType(){
        return map.getClass();
    }
    public Collection<StudyGroup> getValue(){
        return map.values();
    }
    public void putWithKey(int key, StudyGroup group){
        map.put(key, group);
        mapKey.put(group.getId(), key);
        sort();
    }
    @Override
    public String toString(){
        return "Тип коллекции: " + this.getType() + ",\nДата инициализации: " + this.date + ",\nКоличество элементов: " + this.getSize();
    }
}
