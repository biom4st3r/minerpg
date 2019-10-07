package com.biom4st3r.minerpg.interfaces;

@FunctionalInterface
public interface DestructiveRequirement<T> {

    public void destory(T obj);

}
