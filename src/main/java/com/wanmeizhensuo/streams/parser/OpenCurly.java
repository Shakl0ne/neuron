package com.wanmeizhensuo.streams.parser;

import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;
import org.apache.commons.lang3.StringUtils;

public class OpenCurly implements Parsec<Token,Token> {
    @Override
    public Token parse(State<Token> s) throws Throwable {
        var token = s.next();
        if (token.type == TokenType.OPEN_CURLY_BRACKET && StringUtils.equals("{", token.content.toString())) {
            return token;
        } else {
            var message = String.format("expect a open curly bracket { but get %s", token);
            throw s.trap(message);
        }
    }
}
