package com.wanmeizhensuo.streams.parser;

import com.wanmeizhensuo.streams.parser.common.OneToken;
import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.wanmeizhensuo.streams.parser.Combinator.*;
import static jaskell.parsec.common.Atom.eof;
import static jaskell.parsec.common.Combinator.*;

public class FieldsParser implements Parsec<Token, List<String>> {
    final Parsec<Token, ?> end = attempt(closeSquareParser().then(eof()));
    final Parsec<Token, List<Token>> next = many(new OneToken());
    @Override
    public List<String> parse(State<Token> s) throws Throwable {
        List<Token> res = new ArrayList<>();
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

        return res.stream().map(token -> token.content.toString()).collect(Collectors.toList());
    }


}
