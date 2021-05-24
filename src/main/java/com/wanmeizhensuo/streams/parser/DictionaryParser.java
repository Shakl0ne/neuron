package com.wanmeizhensuo.streams.parser;

import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;

import java.util.LinkedHashMap;

import static com.wanmeizhensuo.streams.parser.Parsers.*;
import static jaskell.parsec.common.Combinator.*;
import static com.wanmeizhensuo.streams.parser.common.Dictionary.dictionary;

/*
public class DictionaryParser implements Parsec<Token, LinkedHashMap<String,Object>> {
    final Parsec<Token,LinkedHashMap<Token,Token>> parser = between(openCurly(),closeCurly(),
            many1(
                    attempt(dictionary(nameT("source"),nameT())), option(attempt(dictionary(nameT("as"),nameT()))),
                    attempt(dictionary(nameT("table"),nameT())),attempt(dictionary(nameT("on"),nameT()))
            );

    @Override
    public LinkedHashMap<String,Object> parse(State<Token> s) throws Throwable {
        var result = parser.parse(s);
        LinkedHashMap<String,Object> res = new LinkedHashMap<>(5, (float) 0.8,false);
        var en = result.get("on");
        System.out.println(en.content.toString());
        //res.put(result.get(result.size() - 1).getContent().toString(),result.entrySet().stream().
        return null;
    }
}
*/
