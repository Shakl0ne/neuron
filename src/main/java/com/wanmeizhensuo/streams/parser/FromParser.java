package com.wanmeizhensuo.streams.parser;

import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;

import static com.wanmeizhensuo.streams.parser.Combinator.*;
import static jaskell.parsec.common.Combinator.*;

/**
 * TODO
 *
 * @author mars
 * @version 1.0.0
 * @since 2021/05/18 17:43
 */
public class FromParser implements Parsec<Token, String> {
    final Parsec<Token, Token> parser = openSquareParser().then((nameT("from"))).then(nameT())
            .over(option(attempt(new FieldsParser())).then(closeSquareParser()));
    @Override
    public String parse(State<Token> s) throws Throwable {
        var result = parser.parse(s);
        return result.content.toString();
    }
}
