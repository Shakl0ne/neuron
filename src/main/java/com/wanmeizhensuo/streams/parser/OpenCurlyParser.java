package com.wanmeizhensuo.streams.parser;

import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;
import org.apache.commons.lang3.StringUtils;

/**
 * TODO
 *
 * @author mars
 * @version 1.0.0
 * @since 2021/05/18 17:48
 */
public class OpenCurlyParser implements Parsec<Token, Void> {

    @Override
    public Void parse(State<Token> s) throws Throwable {
        var token = s.next();
        if (token.type == TokenType.OPEN_CURLY_BRACKET && StringUtils.equals("{", token.content.toString())) {
            return null;
        } else {
            var message = String.format("expect a open curly bracket { but get %s", token);
            throw s.trap(message);
        }
    }
}
