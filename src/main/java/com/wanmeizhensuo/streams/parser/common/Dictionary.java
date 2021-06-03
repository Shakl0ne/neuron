package com.wanmeizhensuo.streams.parser.common;

import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;

import java.util.LinkedHashMap;
import java.util.List;

public class Dictionary<E, T> implements Parsec<E, LinkedHashMap<T,T>> {
    private final Parsec<E, List<T>> parser;
    @Override
    public LinkedHashMap<T,T> parse(State<E> s) throws Throwable {
        LinkedHashMap<T,T> dict = new LinkedHashMap<T, T>(5, (float) 0.8,false);
        var p = parser.parse(s);
        if(p.size() % 2 != 0) {
            throw s.trap(String.format("expect even number of elements to be put in dictionary but got %d",
            p.size()));
        }

        for (Integer i = 0; i < p.size(); i+=2) {
            dict.put(p.get(i),p.get(i+1));
        }
        return dict;
    }

    public Dictionary(Parsec<E,List<T>> parser) {
        this.parser = parser;
    }


}
