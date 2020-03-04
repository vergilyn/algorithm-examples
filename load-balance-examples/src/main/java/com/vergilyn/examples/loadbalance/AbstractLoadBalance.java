package com.vergilyn.examples.loadbalance;

import java.util.List;

/**
 * @author vergilyn
 * @date 2020-02-29
 */
public abstract class AbstractLoadBalance {

    public final <T> T select(List<T> invokers) {
        if (invokers == null || invokers.size() == 0) {
            return null;
        }
        if (invokers.size() == 1) {
            return invokers.get(0);
        }
        return doSelect(invokers);
    }

    /**
     * @param <T>      the type parameter
     * @param invokers the invokers
     * @return the t
     */
    protected abstract <T> T doSelect(List<T> invokers);
}
