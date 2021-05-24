package com.wanmeizhensuo.streams.parser.common;

import com.wanmeizhensuo.streams.parser.Token;
import jaskell.expression.Env;

public interface Field {
    Object eval(Env env) throws FieldException;
    Field makeAst();
}
