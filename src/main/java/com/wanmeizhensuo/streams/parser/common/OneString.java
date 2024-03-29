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
Return a token of STRING type
*/

public class OneString implements Parsec<Token, Token> {
    final Parsec<Token, Token> parser = choice(attempt(nameT()),attempt(stringT()),attempt(integerT()),
            attempt(longT()), attempt(doubleT()),attempt(floatT()),attempt(booleanT()),attempt(nullT()),
            attempt(openCurly()),attempt(openSquare()),attempt(closeSquare()),attempt(closeCurly()));

    @Override
    public Token parse(State<Token> s) throws Throwable {
        var t = parser.parse(s);
        if (t.type == TokenType.STRING) {
            return t;
        }
        else {
            return null;
        }
    }
}
