package indi.dependency.packet.map;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 小龙 on 2017/6/2.
 */
public class KeyValue<K, V> implements IKeyValue<K, V> {

    private transient ArrayList<K> mKeyArrays;
    private transient ArrayList<V> mValueArrays;

    public KeyValue() {
        mKeyArrays = new ArrayList<K>();
        mValueArrays = new ArrayList<V>();
    }

    @Override
    public boolean put(K key, V value) {
        for (int i = 0; i < size(); i++) {
            if (mKeyArrays.contains(key)) {
                return false;
            }
        }
        mKeyArrays.add(key);
        mValueArrays.add(value);
        return true;
    }

    @Override
    public boolean put(int index, K key, V value) {
        if (index > -1 && index < size()) {
            for (int i = 0; i < size(); i++) {
                if (mKeyArrays.contains(key)) {
                    return false;
                }
            }
            mKeyArrays.add(index, key);
            mValueArrays.add(index, value);
            return true;
        } else {
            return put(key, value);
        }
    }

    @Override
    public boolean delete(K key) {
        int index = mKeyArrays.indexOf(key);
        if (index != -1) {
            mKeyArrays.remove(index);
            mValueArrays.remove(index);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean delete(int index) {
        if (size() > index && index >= 0) {
            mKeyArrays.remove(index);
            mValueArrays.remove(index);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public V getValue(K key) {
        int index = mKeyArrays.indexOf(key);
        if (index != -1) {
            return mValueArrays.get(index);
        } else {
            return null;
        }
    }

    @Override
    public V getValue(int index) {
        if (mKeyArrays.get(index)==null) {
            return null;
        }else {
            return mValueArrays.get(index);
        }
    }

    @Override
    public K getKey(int index) {
        return mKeyArrays.get(index);
    }

    @Override
    public List<K> getKeys() {
        ArrayList<K> keyArrays = this.mKeyArrays;
        return keyArrays;
    }

    @Override
    public int size() {
        return mKeyArrays.size();
    }

    @Override
    public boolean exchangeIndex(int index1, int index2) {
        if (index1 > -1 && index1 < size() && index2 > -1 && index2 < size()) {
            K tmpKey = mKeyArrays.get(index1);
            V tmpValue = mValueArrays.get(index1);
            mKeyArrays.set(index1, mKeyArrays.get(index2));
            mValueArrays.set(index1, mValueArrays.get(index2));
            mKeyArrays.set(index2, tmpKey);
            mValueArrays.set(index2, tmpValue);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean setIndex(int start, int end) {
        if (start > -1 && start < size()) {
            K key = mKeyArrays.get(start);
            V value = mValueArrays.get(start);
            delete(start);
            if (end < 0) end = 0;
            if (end > size() - 1) end = size() - 1;
            return put(end, key, value);
        } else {
            return false;
        }
    }

    @Override
    public int indexOf(K key) {
        return mKeyArrays.indexOf(key);
    }

    @Override
    public boolean containsKey(K key) {
        return mKeyArrays.contains(key);
    }

    @Override
    public boolean contains(K key, V value) {
        return mKeyArrays.contains(key) && mValueArrays.contains(value);
    }

    @Override
    public boolean setValue(K key, V value) {
        int index = mKeyArrays.indexOf(key);
        if (index != -1) {
            mValueArrays.set(index, value);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size(); i++) {
            sb.append(mKeyArrays.get(i).toString()).append("=").append(mValueArrays.get(i).toString()).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }

}
