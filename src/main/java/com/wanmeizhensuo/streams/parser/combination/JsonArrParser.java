package com.wanmeizhensuo.streams.parser.combination;

import com.wanmeizhensuo.streams.parser.Token;
import com.wanmeizhensuo.streams.parser.common.OneToken;
import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;

import java.util.List;
import java.util.stream.Collectors;

import static com.wanmeizhensuo.streams.parser.Combinator.*;
import static jaskell.parsec.common.Atom.eof;
import static jaskell.parsec.common.Combinator.*;

public class JsonArrParser implements Parsec<Token, List<Object>> {
    final Parsec<Token, ?> end = attempt(closeSquareParser().then(eof()));
    final Parsec<Token, List<Token>> next = (choice(many1(new OneToken())));
    final Parsec<Token, Token> close = closeSquare();

    @Override
    public List<Object> parse(State<Token> s) throws Throwable {
        Integer tran = 0;
        final Parsec<Token, List<Token>> parser = openSquareParser().then(many1(new OneToken()));
        List<Token> res = parser.parse(s);
        res.add(openSquare().parse(s));
        while (end.exec(s).isErr()) {
            tran = s.begin();
            res.addAll(next.parse(s));
            res.add(close.parse(s));
            if (s.next() == end.parse(s)) {
                s.rollback(tran);
                res.remove(res.size() - 1);
            }
        }

        return res.stream().map(token -> token.getContent()).collect(Collectors.toList());
    }


}
