package com.wanmeizhensuo.streams.parser;

import com.wanmeizhensuo.streams.parser.Token;
import com.wanmeizhensuo.streams.parser.TokenType;
import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;
import org.apache.commons.lang3.StringUtils;

public class OpenSquare implements Parsec<Token, Token> {
    @Override
    public Token parse(State<Token> s) throws Throwable {
        var token = s.next();
        if (token.type == TokenType.OPEN_SQUARE_BRACKET && StringUtils.equals("[", token.content.toString())) {
            return token;
        } else {
            var message = String.format("expect a open square bracket [ but get %s", token);
            throw s.trap(message);
        }
    }
}