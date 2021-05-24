package com.wanmeizhensuo.streams.parser.common;

import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;

import java.util.LinkedHashMap;

public class Dictionary<E, T> implements Parsec<E, LinkedHashMap<T,T>> {
    private final Parsec<E, T> p1;
    private final Parsec<E, T> p2;
    @Override
    public LinkedHashMap<T,T> parse(State<E> s) throws Throwable {
        LinkedHashMap<T,T> dict = new LinkedHashMap<T, T>(5, (float) 0.8,false);
        dict.put(this.p1.parse(s),this.p2.parse(s));
        return dict;
    }

    public  Dictionary(Parsec<E,T> p1, Parsec<E,T> p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public static <E,T> Dictionary<E,T> dictionary(Parsec<E,T> p1, Parsec<E,T> p2) {return new Dictionary<>(p1,p2); }
}
