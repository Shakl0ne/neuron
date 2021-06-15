package com.wanmeizhensuo.streams.parser;

import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;

import java.util.LinkedHashMap;
import java.util.List;

import static com.wanmeizhensuo.streams.parser.Parsers.*;
import static jaskell.parsec.common.Combinator.*;
import static com.wanmeizhensuo.streams.parser.common.Collector.*;

public class SelectParser implements  Parsec<Token, LinkedHashMap<String, String>>{
    final Parsec<Token, LinkedHashMap<Token,Token>> parser = openCurlyParser().then(dictionary(many1(nameT()))).over(closeCurlyParser());

    @Override
    public LinkedHashMap<String, String> parse(State<Token> s) throws Throwable {
        var select = new Token("select",TokenType.NAME);
        var n = nName(select);
        manyTill(n,nameT("select")).parse(s);

        var res = parser.parse(s);

        LinkedHashMap<String, String> result = new LinkedHashMap<>();
        res.forEach((t1, t2) -> {
            result.put(
                    t1.content.toString(),
                    t2.content.toString()
            );
        });
        return result;
    }


}
