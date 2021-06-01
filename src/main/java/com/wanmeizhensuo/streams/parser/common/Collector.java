package com.wanmeizhensuo.streams.parser.common;


import com.wanmeizhensuo.streams.parser.Token;

public class Collector {
    public static NName nName(Token name) { return new NName(name); }

    public static OneToken oneToken() { return new OneToken(); }

    public static OneName oneName() { return new OneName(); }

    public static OneString oneString() { return new OneString(); }

}
