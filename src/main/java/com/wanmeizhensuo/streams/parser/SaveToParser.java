package com.wanmeizhensuo.streams.parser;

import jaskell.parsec.common.Parsec;
import jaskell.parsec.common.State;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.wanmeizhensuo.streams.parser.Parsers.nameT;
import static jaskell.parsec.common.Combinator.*;

public class SaveToParser implements Parsec<Token, String> {
    final Parsec<Token, Token> findDB = choice(attempt(find(nameT("saveTo(PG)"))),attempt(find(nameT("saveToEs"))));
    final Parsec<Token, String> pgParser = new Save2PGParser();
    final Parsec<Token, LinkedHashMap<String, String>> esParser = new Save2EsParser();
    String pgRes = "";
    LinkedHashMap<String,String> esRes = new LinkedHashMap<>();


    @Override
    public String parse(State<Token> state) throws Throwable {
        Integer tran = state.begin();
        var db = findDB.parse(state);
        state.rollback(tran);
        switch (db.content.toString()) {
            case "saveTo(PG)" : this.pgRes = pgParser.parse(state);
            case "saveToEs"   : this.esRes = esParser.parse(state);

        }
        String a = "";
        return a;
    }




}
