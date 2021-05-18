package com.wanmeizhensuo.sqlisp.ast;

import com.wanmeizhensuo.sqlisp.ParserException;

import java.util.List;

public class Select implements Lambda{
    @Override
    public Object apply(Env env, List<Object> args) throws ParserException {
        if (args.size() < 2) {
            throw new ParserException(String.format("select require at least 2 parameters but got [%s]", args));
        }

        var columnNames = ((Expression) args.get(0)).getElements();

        return columnNames;
    }
}
