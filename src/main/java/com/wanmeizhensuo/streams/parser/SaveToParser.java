package com.wanmeizhensuo.streams.parser;

import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

import static com.wanmeizhensuo.streams.parser.Parsers.*;
import static jaskell.parsec.common.Combinator.*;

public class SaveToParser implements Parsec<Token, String> {
    final Parsec<Token, List<Token,Token>> parser = between(openSquare(),closeSquare(),many1(choice(Parsers.nameT("saveTo"),Parsers.nameT())));
    @Override
    public String parse(State<Token> s) throws Throwable {
        var result = parser.parse(s);
        return result.content.toString();
    }
}
