package com.clark.func;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MapArray<K, V> {

    public MapArray() {
        internalMap = new LinkedHashMap<K, V>();
    }

    public MapArray(int initialCapacity) {
        internalMap = new LinkedHashMap<K, V>(initialCapacity);
    }

    public MapArray(int initialCapacity, float loadFactor) {
        internalMap = new LinkedHashMap<K, V>(initialCapacity, loadFactor);
    }

    public MapArray(int initialCapacity, float loadFactor, boolean accessOrder) {
        internalMap = new LinkedHashMap<K, V>(initialCapacity, loadFactor,
                accessOrder);
    }

    public V append(K key, V value) {
        w.lock();
        try {
            return internalMap.put(key, value);
        } finally {
            w.unlock();
        }
    }

    public void appendAll(Map<? extends K, ? extends V> map) {
        w.lock();
        try {
            internalMap.putAll(map);
        } finally {
            w.unlock();
        }
    }

    public void clear() {
        w.lock();
        try {
            internalMap.clear();
        } finally {
            w.unlock();
        }
    }

    public boolean containsIndex(int index) {
        r.lock();
        try {
            return index >= 0 && index < internalMap.size();
        } finally {
            r.unlock();
        }
    }

    public boolean containsKey(K key) {
        r.lock();
        try {
            return internalMap.containsKey(key);
        } finally {
            r.unlock();
        }
    }

    public boolean containsValue(V value) {
        r.lock();
        try {
            return internalMap.containsValue(value);
        } finally {
            r.unlock();
        }
    }

    public int indexOfKey(K key) {
        r.lock();
        try {
            if (containsKey(key)) {
                int index = -1;
                for (Map.Entry<K, V> entry : internalMap.entrySet()) {
                    index++;
                    if (entry.getKey().equals(key)) {
                        return index;
                    }
                }
            }
            return -1;
        } finally {
            r.unlock();
        }
    }

    public int indexOfValue(V value) {
        r.lock();
        try {
            if (containsValue(value)) {
                int index = -1;
                for (Map.Entry<K, V> entry : internalMap.entrySet()) {
                    index++;
                    if (entry.getValue().equals(value)) {
                        return index;
                    }
                }
            }
            return -1;
        } finally {
            r.unlock();
        }
    }

    public boolean isEmpty() {
        r.lock();
        try {
            return internalMap.size() == 0;
        } finally {
            r.unlock();
        }
    }

    public K keyOfIndex(int index) {
        r.lock();
        try {
            if (containsIndex(index)) {
                Iterator<K> iterator = internalMap.keySet().iterator();
                int i = 0;
                while (i != index) {
                    i++;
                    iterator.next();
                }

                return iterator.next();
            }
            throw new IndexOutOfBoundsException("size:" + size()
                    + " ,but request index is " + index);
        } finally {
            r.unlock();
        }
    }

    public V remove(int index) {
        w.lock();
        try {
            if (containsIndex(index)) {
                Iterator<K> iterator = internalMap.keySet().iterator();
                int i = 0;
                while (i != index) {
                    i++;
                    iterator.next();
                }

                return internalMap.remove(iterator.next());
            }
            throw new IndexOutOfBoundsException("size:" + size()
                    + " ,but request index is " + index);
        } finally {
            w.unlock();
        }
    }

    public V remove(K key) {
        w.lock();
        try {
            return internalMap.remove(key);
        } finally {
            w.unlock();
        }
    }

    public K set(int index, V value) {
        w.lock();
        try {
            if (containsIndex(index)) {
                Iterator<K> iterator = internalMap.keySet().iterator();
                int i = 0;
                while (i != index) {
                    i++;
                    iterator.next();
                }

                K key = iterator.next();
                internalMap.put(key, value);
                return key;
            }
            throw new IndexOutOfBoundsException("size:" + size()
                    + " ,but request index is " + index);
        } finally {
            w.unlock();
        }
    }

    public int size() {
        r.lock();
        try {
            return internalMap.size();
        } finally {
            r.unlock();
        }
    }

    public V valueOfIndex(int index) {
        r.lock();
        try {
            if (containsIndex(index)) {
                Iterator<K> iterator = internalMap.keySet().iterator();
                int i = 0;
                while (i != index) {
                    i++;
                    iterator.next();
                }

                return internalMap.get(iterator.next());
            }
            throw new IndexOutOfBoundsException("size:" + size()
                    + " ,but request index is " + index);
        } finally {
            r.unlock();
        }
    }

    public V valueOfKey(K key) {
        r.lock();
        try {
            return internalMap.get(key);
        } finally {
            r.unlock();
        }
    }

    private LinkedHashMap<K, V> internalMap;
    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock r = readWriteLock.readLock();
    private final Lock w = readWriteLock.writeLock();
}
