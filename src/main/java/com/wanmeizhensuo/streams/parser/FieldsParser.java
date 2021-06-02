package com.wanmeizhensuo.streams.parser;

import jaskell.parsec.ParsecException;
import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;

import java.io.EOFException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.wanmeizhensuo.streams.parser.Parsers.*;
import static com.wanmeizhensuo.streams.parser.common.Collector.*;
import static jaskell.parsec.common.Atom.eof;
import static jaskell.parsec.common.Combinator.*;

/*
Parse each token inside a json object or json array and return only NAME type tokens as a list of strings.
*/

public class FieldsParser implements Parsec<Token, List<String>> {
    final Parsec<Token, ?> end = attempt(closeSquareParser().then(eof()));
    final Parsec<Token, Optional<Token>> open = option(choice(attempt(openSquare()),attempt(openCurly())));
    final Parsec<Token, Optional<Token>> close = option(choice(attempt(closeSquare()),attempt(closeCurly())));
    @Override
    public List<String> parse(State<Token> s) throws Throwable {
        Integer osCount = 0, ocCount = 0, csCount = 0, ccCount = 0;
        List<Token> res = new ArrayList<>();
        while ((csCount <= osCount) && (ocCount <= ccCount)) {                      //Only extract the token inside
            var o = open.parse(s);                                  //either start with an open square bracket or open curly bracket
            if (o.isPresent()) {
                if (o.get().type == TokenType.OPEN_SQUARE_BRACKET) {
                    osCount += 1;
                }
                else {
                    ocCount += 1;
                }
                res.add(o.get());
            }

            res.addAll(many(oneToken()).parse(s));

            if (end.exec(s).isErr()) {
                var c = close.parse(s);                             //either end with an close square bracket or close curly bracket
                if (c.isPresent()) {
                    if (c.get().type == TokenType.CLOSE_SQUARE_BRACKET) {
                        csCount += 1;
                    }
                    else {
                        ccCount += 1;
                    }
                    res.add(c.get());
                }
            }
            else {
                break;                                                              //heading to end of the line
            }
        }
        if ((csCount - osCount == 1) || (ccCount - ocCount == 1)) {
            res.remove(res.size() - 1);
        }


        return res.stream().map(token -> token.content.toString()).collect(Collectors.toList());

    }


}
