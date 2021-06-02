package com.wanmeizhensuo.streams.parser;

import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;

import static com.wanmeizhensuo.streams.parser.Parsers.*;
import static com.wanmeizhensuo.streams.parser.Parsers.nameT;
import static com.wanmeizhensuo.streams.parser.common.Collector.nName;
import static jaskell.parsec.common.Combinator.*;

public class Save2PGParser implements Parsec<Token, String> {
    final Parsec<Token, Token> parser = nameT().over(closeSquareParser());
    @Override
    public String parse(State<Token> s) throws Throwable {
        var from = new Token("saveTo(PG)", TokenType.NAME);
        var n = nName(from);
        manyTill(n,nameT("saveTo(PG)")).parse(s);

        return parser.parse(s).content.toString();
    }
}
