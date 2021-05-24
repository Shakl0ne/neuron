package com.wanmeizhensuo.streams.parser.common;

import com.wanmeizhensuo.streams.parser.Token;
import jaskell.expression.Env;
import jaskell.expression.Expression;
import jaskell.expression.ExpressionException;

public class NameExp implements Field {
    final Field field;

    public NameExp(Field field) {
        this.field = field;
    }
    @Override
    public Object eval(Env env) throws FieldException {
        return field.eval(env);
    }

    @Override
    public Field makeAst() { return new NameExp(this.field.makeAst()); }



}
