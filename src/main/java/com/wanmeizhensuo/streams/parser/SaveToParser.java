package com.wanmeizhensuo.streams.parser;

import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;
import static com.wanmeizhensuo.streams.parser.Parsers.*;


public class SaveToParser implements Parsec<Token, String> {
    Parsec<Token, Token> parser = openSquare().then(Parsers.nameT("saveTo(PG)")).then(Parsers.nameT()).over(closeSquare());
    @Override
    public String parse(State<Token> s) throws Throwable {
        var result = parser.parse(s);
        return result.content.toString();
    }
}
