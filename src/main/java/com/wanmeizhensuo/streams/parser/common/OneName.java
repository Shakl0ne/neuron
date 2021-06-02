package com.wanmeizhensuo.streams.parser.common;

import com.wanmeizhensuo.streams.parser.Token;
import com.wanmeizhensuo.streams.parser.TokenType;
import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;

import static com.wanmeizhensuo.streams.parser.Parsers.*;
import static com.wanmeizhensuo.streams.parser.Parsers.nullT;
import static jaskell.parsec.common.Combinator.attempt;
import static jaskell.parsec.common.Combinator.choice;

/*
Return one token of NAME type.
*/

public class OneName implements Parsec<Token, Token> {
    final Parsec<Token, Token> parser = choice(attempt(nameT()),attempt(stringT()),attempt(integerT()),
            attempt(longT()), attempt(doubleT()),attempt(floatT()),attempt(booleanT()),attempt(nullT()));

    @Override
    public Token parse(State<Token> s) throws Throwable {
        var t = parser.parse(s);
        if (t.type == TokenType.NAME) {
            return t;
        }
        else {
            return null;
        }
    }
}
