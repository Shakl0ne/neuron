package com.wanmeizhensuo.sqlisp.ast;

import com.wanmeizhensuo.sqlisp.ParserException;
import com.wanmeizhensuo.sqlisp.ast.Lambda;

import java.util.List;

public class From implements Lambda{
    @Override
    public Object apply(Env env, List<Object> args ) throws ParserException {
    if (args.size() < 2 ) {
        throw new ParserException(String.format("from require at least 2 parameters but got [%s]", args));
    }
    var tableNames = ((Expression) args.get(0)).getElements();

    return tableNames;
    }
}
