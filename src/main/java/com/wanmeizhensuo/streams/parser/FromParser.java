package com.wanmeizhensuo.streams.parser;

import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;

import static com.wanmeizhensuo.streams.parser.Parsers.*;
import static com.wanmeizhensuo.streams.parser.common.Collector.*;
import static jaskell.parsec.common.Combinator.*;



public class FromParser implements Parsec<Token, String> {
    final Parsec<Token, Token> parser = nameT().over(choice(attempt(openSquareParser()),attempt(closeSquareParser())));
    @Override
    public String parse(State<Token> s) throws Throwable {
        var from = new Token("from", TokenType.NAME);
        var n = nName(from);
        manyTill(n,nameT("from")).parse(s);

        return parser.parse(s).content.toString();
    }
}
