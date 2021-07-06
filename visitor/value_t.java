package visitor;

import parser.*;

public class value_t {
    public value_t() {
        this.i = 0;
        this.f = 0;
        this.b = false;
        this.s = "";
    }

    public value_t(TYPE type, int lineNum) {
        this.i = 0;
        this.f = 0;
        this.b = false;
        this.s = "";
        this.type = type;
        this.lineNum = lineNum;
    }

    public TYPE type = null;
    public int lineNum = 0;
    public int i;
    public float f;
    public boolean b;
    public String s;
}