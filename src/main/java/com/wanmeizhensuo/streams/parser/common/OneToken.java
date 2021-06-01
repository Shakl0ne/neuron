package com.wanmeizhensuo.streams.parser.common;

import com.wanmeizhensuo.streams.parser.Token;
import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;

import static com.wanmeizhensuo.streams.parser.Parsers.*;
import static jaskell.parsec.common.Combinator.*;

/*
Return one token that can be any type of content
*/

public class OneToken implements Parsec<Token, Token> {
    final Parsec<Token, Token> parser = choice(attempt(nameT()),attempt(stringT()),attempt(integerT()),
            attempt(longT()), attempt(doubleT()),attempt(floatT()),attempt(booleanT()),attempt(nullT()));

    @Override
    public Token parse(State<Token> s) throws Throwable {
        return parser.parse(s);
    }

}
