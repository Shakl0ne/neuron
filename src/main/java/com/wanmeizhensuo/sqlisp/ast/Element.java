package com.wanmeizhensuo.sqlisp.ast;

import com.wanmeizhensuo.sqlisp.ParserException;

public interface Element {
    Object eval(Env env) throws ParserException;
}
