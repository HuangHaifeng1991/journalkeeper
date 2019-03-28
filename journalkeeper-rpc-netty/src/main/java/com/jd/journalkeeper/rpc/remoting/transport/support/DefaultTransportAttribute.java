package com.jd.journalkeeper.rpc.remoting.transport.support;

import com.jd.journalkeeper.rpc.remoting.transport.TransportAttribute;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 默认通信属性
 * author: gaohaoxiang
 * email: gaohaoxiang@jd.com
 * date: 2018/8/14
 */
public class DefaultTransportAttribute implements TransportAttribute {

    private volatile AtomicReference<ConcurrentMap<Object, Object>> attributes = new AtomicReference();

    @Override
    public <T> T set(Object key, Object value) {
        return (T) getOrNewAttributes().put(key, value);
    }

    @Override
    public <T> T putIfAbsent(Object key, Object value) {
        return (T) getOrNewAttributes().putIfAbsent(key, value);
    }

    @Override
    public <T> T get(Object key) {
        ConcurrentMap<Object, Object> attributes = this.attributes.get();
        if (attributes == null) {
            return null;
        }
        return (T) attributes.get(key);
    }

    @Override
    public <T> T remove(Object key) {
        ConcurrentMap<Object, Object> attributes = this.attributes.get();
        if (attributes == null) {
            return null;
        }
        return (T) attributes.remove(key);
    }

    @Override
    public boolean contains(Object key) {
        ConcurrentMap<Object, Object> attributes = this.attributes.get();
        if (attributes == null) {
            return false;
        }
        return attributes.containsKey(key);
    }

    @Override
    public Set<Object> keys() {
        ConcurrentMap<Object, Object> attributes = this.attributes.get();
        if (attributes == null) {
            return Collections.emptySet();
        }
        return attributes.keySet();
    }

    protected ConcurrentMap<Object, Object> getOrNewAttributes() {
        ConcurrentMap<Object, Object> result = attributes.get();
        if (result == null) {
            attributes.compareAndSet(null, new ConcurrentHashMap<>());
            return attributes.get();
        }
        return result;
    }
}