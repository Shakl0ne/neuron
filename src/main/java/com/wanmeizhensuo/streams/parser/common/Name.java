/*
package com.wanmeizhensuo.streams.parser.common;

import com.wanmeizhensuo.streams.parser.Token;
import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;


import static com.wanmeizhensuo.streams.parser.Parsers.nameT;

public class Name implements Parsec<Token, Token> {
    final Parsec<Token, Token> nameT = nameT();

    @Override
    public Token parse(State<Token> s) throws Throwable {
        return new NameExp(nameT.parse(s));
    }
}
*/
