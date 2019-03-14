package indi.dependency.packet.map;

import java.util.List;

/**
 * Created by 小龙 on 2017/6/2.
 */
interface IKeyValue<K, V> {

    boolean put(K key, V value);

    boolean put(int index, K key, V value);

    boolean delete(K key);

    boolean delete(int index);

    V getValue(K key);

    V getValue(int index);

    K getKey(int index);

    List<K> getKeys();

    int size();

    /**
     * 交换map下标
     * @param index1
     * @param index2
     */
    boolean exchangeIndex(int index1, int index2);

    /**
     * 设置下标
     * @param start
     * @param end
     */
    boolean setIndex(int start, int end);

    int indexOf(K key);

    boolean containsKey(K key);

    boolean contains(K key, V value);

    boolean setValue(K key, V value);
}
