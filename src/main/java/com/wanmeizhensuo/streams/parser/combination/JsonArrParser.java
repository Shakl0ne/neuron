package com.wanmeizhensuo.streams.parser.combination;

import com.wanmeizhensuo.streams.parser.Token;
import com.wanmeizhensuo.streams.parser.common.OneToken;
import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;

import java.util.List;
import java.util.stream.Collectors;

import static com.wanmeizhensuo.streams.parser.Parsers.*;
import static jaskell.parsec.common.Atom.eof;
import static jaskell.parsec.common.Combinator.*;

public class JsonArrParser implements Parsec<Token, List<Object>> {
    final Parsec<Token, List<Token>> parser = openSquareParser().then(many(new OneToken()));
    final Parsec<Token, ?> end = attempt(closeSquareParser().then(eof()));
    final Parsec<Token, List<Token>> next = many(new OneToken());
    @Override
    public List<Object> parse(State<Token> s) throws Throwable {
        List<Token> res = parser.parse(s);
        while (true) {
            var o = option(attempt(openSquare())).parse(s);
            if (o.isPresent()) {
                res.add(o.get());
            }
            res.addAll(next.parse(s));
            if (end.exec(s).isErr()) {
                var c = option(attempt(closeSquare())).parse(s);
                if (c.isPresent()) {
                    res.add(c.get());
                }
            }
            else {
                break;
            }

        }

        return res.stream().map(token -> token.getContent()).collect(Collectors.toList());
    }


}
