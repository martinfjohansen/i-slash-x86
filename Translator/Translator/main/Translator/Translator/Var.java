package Translator.Translator;

public class Var {
    public char [] name;
    public char [] type;
    public boolean isStruct;
    public boolean isStructArray;
    public boolean isFunction;

    // Computed
    public Struct st;
    public Function function;
    //public boolean isNumber;
    //public boolean isArray;
    //public boolean isBitfield;
    //public boolean isSIMD;
    //public boolean isInteger;
    //public boolean isFloat;
}
