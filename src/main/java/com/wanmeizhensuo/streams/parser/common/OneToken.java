package com.wanmeizhensuo.streams.parser.common;

import com.wanmeizhensuo.streams.parser.Token;
import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;

import static com.wanmeizhensuo.streams.parser.Combinator.*;
import static jaskell.parsec.common.Combinator.*;

public class OneToken implements Parsec<Token, Token> {
    final Parsec<Token, Token> parser = choice(attempt(nameT()),attempt(integerT()),attempt(longT()),
            attempt(doubleT()),attempt(floatT()),attempt(booleanT()),attempt(nullT()));

    @Override
    public Token parse(State<Token> s) throws Throwable {
        return parser.parse(s);
    }

}
