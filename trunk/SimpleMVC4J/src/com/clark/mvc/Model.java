package com.clark.mvc;

import java.util.HashMap;

public class Model {

    private static class Holder {
        static Model model = new Model();
    }

    private Model() {
    }

    static Model getInstance() {
        return Holder.model;
    }

    private HashMap<String, Proxy> hashMap = new HashMap<String, Proxy>();

    /**
     * 注册一个 {@link Proxy} 实例到 Model 的注册表中。
     * 
     * @param proxy
     *            一个需要注册的 {@link Proxy} 实例。不能为 null。
     */
    public synchronized void register(Proxy proxy) {
        if (proxy == null) {
            throw new NullPointerException("proxy can't be null");
        }

        if (proxy.getProxyName() == null) {
            throw new NullPointerException(
                    "Proxy#getProxyName() can't return null");
        }

        String name = proxy.getProxyName();
        if (!hashMap.containsKey(name)) {
            hashMap.put(name, proxy);
            System.out.println("register proxy: [" + proxy + "]");
        } else {
            System.err.println("Repeat registered proxy: [" + proxy + "]");
        }
    }

    /**
     * 从 Model 的注册表中移除 {@link Proxy} 实例。
     * 
     * @param proxy
     *            一个已注册的 {@link Proxy} 实例。不能为 null。
     */
    public synchronized void remove(Proxy proxy) {
        if (proxy == null) {
            throw new NullPointerException("proxy can't be null");
        }

        String proxyName = proxy.getProxyName();
        if (proxyName == null) {
            throw new NullPointerException(
                    "Proxy#getProxyName() can't return null");
        }

        if (hashMap.containsKey(proxyName)) {
            hashMap.remove(proxyName);
            System.out.println("remove proxy: [" + proxy + "]");
        } else {
            System.err.println("There is no proxy name: [" + proxyName
                    + "] already registered");
        }
    }

    /**
     * 从 Model 注册表中取出 proxyName 映射的 {@link Proxy} 实例。
     * 
     * @param proxyName
     *            {@link Proxy} 实例对应的键值。
     * @return 返回映射的 {@link Proxy} 实例，可能为 null。
     */
    public synchronized Proxy get(String proxyName) {
        return hashMap.get(proxyName);
    }
}