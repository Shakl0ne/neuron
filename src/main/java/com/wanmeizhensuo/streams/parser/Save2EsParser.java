package com.wanmeizhensuo.streams.parser;

import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;

import java.util.LinkedHashMap;
import java.util.Set;

import static com.wanmeizhensuo.streams.parser.Parsers.*;
import static com.wanmeizhensuo.streams.parser.common.Collector.*;
import static jaskell.parsec.common.Combinator.many1;
import static jaskell.parsec.common.Combinator.manyTill;

public class Save2EsParser implements Parsec<Token, LinkedHashMap<String,String>> {
    final Parsec<Token, LinkedHashMap<Token,Token>> parser = openCurlyParser().then(dictionary(many1(nameT()))).over(closeCurlyParser());

    @Override
    public LinkedHashMap<String, String> parse(State<Token> state) throws Throwable {
        var es = new Token("saveToEs",TokenType.NAME);
        var n = nName(es);
        manyTill(n,nameT("saveToEs")).parse(state);

        var res = parser.parse(state);

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
