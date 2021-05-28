package com.wanmeizhensuo.streams.parser.combination;

public class NameExp implements Field {
    final Field field;

    public NameExp(Field field) {
        this.field = field;
    }

    @Override
    public Field makeAst() { return new NameExp(this.field.makeAst()); }



}
