package com.wanmeizhensuo.streams.parser.combination;

import com.wanmeizhensuo.streams.parser.Token;
import com.wanmeizhensuo.streams.parser.common.OneToken;
import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;
import jaskell.util.Try;
import org.eclipse.yasson.internal.serializer.MapToEntriesArraySerializer;

import java.util.List;
import java.util.stream.Collectors;

import static com.wanmeizhensuo.streams.parser.Combinator.*;
import static jaskell.parsec.common.Atom.eof;
import static jaskell.parsec.common.Combinator.*;

public class JsonArrParser implements Parsec<Token, List<Object>> {
    final Parsec<Token, List<Token>> parser = openSquareParser().then(many1(new OneToken()));
    final Parsec<Token, ?> end = attempt(closeSquareParser().then(eof()));
    final Parsec<Token, List<Token>> next = choice(many(new OneToken()));
    @Override
    public List<Object> parse(State<Token> s) throws Throwable {
        Integer tran = 0;
        List<Token> res = parser.parse(s);
        while (end.exec(s).isErr()) {
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

        }

        return res.stream().map(token -> token.getContent()).collect(Collectors.toList());
    }


}
