package org.hiperion.common.util.helpers;

import java.util.Map;

/**
 * User: iobestar
 * Date: 21.05.13.
 * Time: 19:31
 */
public class MapHelper {

    private MapHelper(){}

    public static <K,V> boolean isEquals(Map<K,V> mapA, Map<K,V> mapB){

        if(null == mapA && null == mapB){
            return true;
        }

        if(null != mapA && null == mapB){
            return false;
        }

        if(null == mapA && null != mapB){
            return false;
        }

        if(mapA.values().size() != mapB.values().size()){
            return false;
        }

        for(Map.Entry<K,V> mapEntry : mapA.entrySet()){
            V valueA = mapEntry.getValue();
            if (!mapB.containsKey(mapEntry.getKey())){
                return false;
            }
            V valueB = mapB.get(mapEntry.getKey());

            if(null == valueA && null == valueB){
                continue;
            }

            if(null == valueA && null != valueB || null != valueA && null == valueB){
                return false;
            }

            if(!valueA.equals(valueB)){
                return false;
            }
        }

        return true;
    }

    public static <K,V> int getHashCode(Map<K,V> map){
        int result = 0;
        for(V value : map.values()){
            result += 31 * result + ((value == null) ? 0 : value.hashCode());
        }
        return result;
    }
}
