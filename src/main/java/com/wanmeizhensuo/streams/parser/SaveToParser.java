package com.wanmeizhensuo.streams.parser;

import jaskell.parsec.ParsecException;
import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.wanmeizhensuo.streams.parser.Parsers.*;
import static jaskell.parsec.common.Combinator.*;
import static com.wanmeizhensuo.streams.parser.common.Pair1.pair1;

public class SaveToParser implements Parsec<Token, ImmutablePair<String, String>> {
    final Parsec<Token, ImmutablePair<Token, Token>> parser = between(openSquare(), closeSquare(), pair1(nameT(), nameT()));

    @Override
    public ImmutablePair<String, String> parse(State<Token> s) throws Throwable {

        var result = parser.parse(s);

        Pattern p = Pattern.compile("saveTo\\(?(.+?)\\)?");
        Matcher m = p.matcher(result.left.content.toString());

        if (m.find()) {
            var left = result.left.content.toString().replaceAll("saveTo", "");
            if (left.startsWith("(") && left.endsWith(")")) {
                left = left.substring(1, left.length() - 1);
            }
            if (left.isBlank()) {
                var message = String.format("need database name");
                throw s.trap(message);
            }
            ImmutablePair res = new ImmutablePair(left, result.right.content.toString());
            return res;
        }
        else {
            var message = String.format("expect saveTo(database pool), but get %s", result.left.content.toString());
            throw s.trap(message);
        }
    }
}
