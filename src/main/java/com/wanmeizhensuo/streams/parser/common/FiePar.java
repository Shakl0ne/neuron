/*
package com.wanmeizhensuo.streams.parser.common;

import com.wanmeizhensuo.streams.parser.Token;
import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;

import java.util.List;

import static jaskell.parsec.common.Combinator.*;

public class FiePar implements Parsec<Token, List<Token>> {


    @Override
    public List<Token> parse(State<Token> s) throws Throwable {
        Parsec<Token,Token> parser = choice(attempt(new JsonArr(),attempt(new Name())));
        return parser.parse(s);
    }






}
*/
