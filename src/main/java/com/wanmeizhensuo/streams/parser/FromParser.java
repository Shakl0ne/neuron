package com.wanmeizhensuo.streams.parser;

import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;
import static com.wanmeizhensuo.streams.parser.Parsers.*;

/**
 * TODO
 *
 * @author mars
 * @version 1.0.0
 * @since 2021/05/18 17:43
 */
public class FromParser implements Parsec<Token, String> {
    final Parsec<Token, Token> parser = openSquare().then(Parsers.nameT("from")).then(Parsers.nameT()).over(closeSquare());
    @Override
    public String parse(State<Token> s) throws Throwable {
        var result = parser.parse(s);
        return result.content.toString();
    }
}
