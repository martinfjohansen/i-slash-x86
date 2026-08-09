package Translator.Translator;

import DataStructures.Array.Structures.DataReference;
import DataStructures.Array.Structures.Structure;
import lists.LinkedListCharacters.Structures.LinkedListCharacters;
import references.references.StringReference;

import java.util.HashMap;
import java.util.Map;

import static DataStructures.Array.Structures.Structures.*;
import static Translator.Translator.Translator.GetEntryTypes;
import static arrays.arrays.arrays.StringsEqual;
import static lists.LinkedListCharacters.LinkedListCharactersFunctions.LinkedListCharactersFunctions.*;
import static strings.strings.strings.AppendString;

public class Translator2T {
    public static boolean Translate(Ast ast, StringReference asmRef, StringReference message) {
        double i;
        boolean success;
        LinkedListCharacters ll;
        StringReference tmp;

        success = true;
        ll = CreateLinkedListCharacter();

        LinkedListCharactersAddString(ll, "%include \"x86.mac\"\n\n".toCharArray());

        for(i = 0d; i < ast.st.length && success; i = i + 1d){
            tmp = new StringReference();
            success = PrintStructure(ast.st[(int)i], tmp, message);
            if(success){
                LinkedListCharactersAddString(ll, tmp.string);
            }
        }

        for(i = 0d; i < ast.fnc.length && success; i = i + 1d){
            tmp = new StringReference();
            success = PrintFunction(ast.fnc[(int)i], tmp, message);
            if(success){
                LinkedListCharactersAddString(ll, tmp.string);
            }
        }

        if(success){
            asmRef.string = LinkedListCharactersToArray(ll);
            FreeLinkedListCharacter(ll);
        }

        return success;
    }

    public static boolean PrintStructure(Struct st, StringReference stRef, StringReference message) {
        double i;
        boolean success;
        LinkedListCharacters ll;
        StringReference tmp;

        ll = CreateLinkedListCharacter();
        success = true;
        tmp = new StringReference();

        LinkedListCharactersAddString(ll, ("struc " + new String(st.name) + "\n").toCharArray());

        for(i = 0d; i < st.vars.length && success; i = i + 1d){
            success = PrintVariable(st.vars[(int)i], tmp, message);
            if(success){
                LinkedListCharactersAddString(ll, tmp.string);
            }
        }

        LinkedListCharactersAddString(ll, "endstruc".toCharArray());
        LinkedListCharactersAddString(ll, "\n".toCharArray());
        LinkedListCharactersAddString(ll, "\n".toCharArray());

        stRef.string = LinkedListCharactersToArray(ll);

        FreeLinkedListCharacter(ll);

        return success;
    }

    public static boolean PrintVariable(Var var, StringReference varRef, StringReference message) {
        boolean success;
        DataReference entryTypesRef;
        LinkedListCharacters ll;

        ll = CreateLinkedListCharacter();
        success = true;

        entryTypesRef = new DataReference();

        success = GetEntryTypes(entryTypesRef, message);

        if(success) {
            Structure types = entryTypesRef.data.structure;
            if(StructHasKey(types, var.type)) {
                Structure data = GetStructFromStruct(types, var.type);
                if (StructHasKey(data, "nasmType".toCharArray())) {
                    char[] nasmType = GetStringFromStruct(data, "nasmType".toCharArray());

                    LinkedListCharactersAddString(ll, "  .".toCharArray());
                    LinkedListCharactersAddString(ll, var.name);
                    LinkedListCharactersAddString(ll, ": ".toCharArray());
                    LinkedListCharactersAddString(ll, nasmType);
                    LinkedListCharactersAddString(ll, " 1".toCharArray());
                    LinkedListCharactersAddString(ll, "\n".toCharArray());
                } else {
                    success = false;
                    message.string = ("Could not find NASM type for type (2): " + new String(var.type)).toCharArray();
                }
            }else if(var.isStruct) {
                LinkedListCharactersAddString(ll, "  .".toCharArray());
                LinkedListCharactersAddString(ll, var.name);
                LinkedListCharactersAddString(ll, ": resq 1\n".toCharArray());
            }else{
                success = false;
                message.string = ("Could not find NASM type for type (1): " + new String(var.type)).toCharArray();
            }
        }

        if(success) {
            varRef.string = LinkedListCharactersToArray(ll);
            FreeLinkedListCharacter(ll);
        }

        return success;
    }

    public static boolean PrintFunction(Function fnc, StringReference fncRef, StringReference message) {
        double i;
        char [] functionStructureName;
        boolean success;
        LinkedListCharacters ll;
        StringReference tmp;

        ll = CreateLinkedListCharacter();

        success = true;

        LinkedListCharactersAddString(ll, ("global " + new String(fnc.name)).toCharArray());
        LinkedListCharactersAddString(ll, "\n".toCharArray());

        functionStructureName = (new String(fnc.name) + "S").toCharArray();

        LinkedListCharactersAddString(ll, (new String(fnc.name) + ":").toCharArray());
        LinkedListCharactersAddString(ll, "\n".toCharArray());

        for(i = 0d; i < fnc.ins.length && success; i = i + 1d){
            tmp = new StringReference();
            success = PrintInstruction(fnc.ins[(int)i], tmp, functionStructureName);
            if(success){
                LinkedListCharactersAddString(ll, tmp.string);
            }
        }

        LinkedListCharactersAddString(ll, "ret".toCharArray());
        LinkedListCharactersAddString(ll, "\n".toCharArray());

        if(success) {
            fncRef.string = LinkedListCharactersToArray(ll);
            FreeLinkedListCharacter(ll);
        }

        return success;
    }

    public static boolean PrintInstruction(Instruction ins, StringReference insRef, char [] fncStName) {
        double i, j, nr;
        boolean success;
        LinkedListCharacters ll;
        Structure aliases;
        char [] name;
        Param param;
        Map<String, Double> immVarCount;

        ll = CreateLinkedListCharacter();
        success = true;

        // What instructions should keep immediates.
        if(!StringsEqual(ins.name, "Mov".toCharArray()) && !StringsEqual(ins.name, "Movx8".toCharArray())) {
            // Preload immediates
            // The reason is as follows: Not preloading immediates causees a combinatorial explosion of instruction macros.
            immVarCount = new HashMap<>();

            for (i = 0d; i < ins.params.length; i = i + 1d) {

                // What parameters should remain immediates.
                if(!(StringsEqual(ins.name, "Idro".toCharArray()) && i == 3) && !(StringsEqual(ins.name, "Idw".toCharArray()) && i == 1) && !(StringsEqual(ins.name, "Idr".toCharArray()) && i == 2)) {
                    param = ins.params[(int) i];
                    param.useImmediateVar = true;

                    if (StringsEqual(param.type, "literal".toCharArray())) {
                        for (j = 0; j < ins.indentation; j = j + 1d) {
                            LinkedListCharactersAddString(ll, "  ".toCharArray());
                        }

                        if (!immVarCount.containsKey(new String(param.immediateType))) {
                            immVarCount.put(new String(param.immediateType), 0d);
                        }
                        nr = immVarCount.get(new String(param.immediateType));

                        param.immediateVarName = ("i" + new String(param.immediateType) + (int) nr).toCharArray();
                        immVarCount.put(new String(param.immediateType), nr + 1d);

                        LinkedListCharactersAddString(ll, "Mov.i".toCharArray());
                        LinkedListCharactersAddString(ll, param.immediateType);
                        LinkedListCharactersAddString(ll, " ".toCharArray());
                        LinkedListCharactersAddString(ll, (new String(fncStName) + ".").toCharArray());
                        LinkedListCharactersAddString(ll, param.immediateVarName);
                        LinkedListCharactersAddString(ll, ", ".toCharArray());
                        LinkedListCharactersAddString(ll, param.literal);
                        LinkedListCharactersAddString(ll, "\n".toCharArray());

                    }
                }
            }
        }

        // -- immediates

        for(i = 0; i < ins.indentation; i = i + 1d){
            LinkedListCharactersAddString(ll, "  ".toCharArray());
        }

        aliases = GetAliases();

        if(StructHasKey(aliases, ins.name)){
            name = GetStringFromStruct(aliases, ins.name);
        }else{
            name = ins.name;
        }
        LinkedListCharactersAddString(ll, name);

        // Instructions with suffix:
        if((ins.hasTypePostfix || ins.params.length > 0) && !StringsEqual(ins.name, "Call".toCharArray())) {
            LinkedListCharactersAddString(ll, ".".toCharArray());
        }

        if(ins.memoryPostfix != null){
            LinkedListCharactersAddString(ll, ins.memoryPostfix);
        }else{
            LinkedListCharactersAddString(ll, "_".toCharArray());
        }

        if(ins.hasTypePostfix) {
            if (ins.typePostfix != null) {
                LinkedListCharactersAddString(ll, ins.typePostfix);
            } else {
                LinkedListCharactersAddString(ll, "_".toCharArray());
            }
        }

        LinkedListCharactersAddString(ll, " ".toCharArray());

        for(i = 0d; i < ins.params.length; i = i + 1d){
            param = ins.params[(int) i];
            if(StringsEqual(param.type, "var".toCharArray())){
                if(StringsEqual(ins.name, "Acr".toCharArray()) && i == 2d){
                    LinkedListCharactersAddString(ll, (new String(ins.params[1].var.st.name) + ".").toCharArray());
                }else if(StringsEqual(ins.name, "Acw".toCharArray()) && i == 1d){
                    LinkedListCharactersAddString(ll, (new String(ins.params[0].var.st.name) + ".").toCharArray());
                }else if(StringsEqual(ins.name, "Call".toCharArray()) && i == 0d){
                }else{
                    LinkedListCharactersAddString(ll, (new String(fncStName) + ".").toCharArray());
                }
                LinkedListCharactersAddString(ll, param.varname);
            }else if(StringsEqual(param.type, "literal".toCharArray())){
                if(param.useImmediateVar) {
                    LinkedListCharactersAddString(ll, (new String(fncStName) + ".").toCharArray());
                    LinkedListCharactersAddString(ll, param.immediateVarName);
                }else{
                    LinkedListCharactersAddString(ll, param.literal);
                }
            }

            if(i + 1d < ins.params.length) {
                LinkedListCharactersAddString(ll, ", ".toCharArray());
            }
        }

        if(ins.label1 != null){
            if(ins.params.length > 0){
                LinkedListCharactersAddString(ll, ", ".toCharArray());
            }

            LinkedListCharactersAddString(ll, ins.label1);

            if(ins.label2 != null){
                LinkedListCharactersAddString(ll, (", " + new String(ins.label2)).toCharArray());
            }
        }

        LinkedListCharactersAddString(ll, "\n".toCharArray());

        if(success) {
            insRef.string = LinkedListCharactersToArray(ll);
            FreeLinkedListCharacter(ll);
        }

        return success;
    }

    private static Structure GetAliases() {
        Structure aliases;

        aliases = CreateStructure();
        AddStringToStruct(aliases, "LessThan".toCharArray(), "Lt".toCharArray());
        AddStringToStruct(aliases, "Equal".toCharArray(), "Eq".toCharArray());
        AddStringToStruct(aliases, "Unequal".toCharArray(), "Neq".toCharArray());
        AddStringToStruct(aliases, "MoreThanOrEqual".toCharArray(), "Gte".toCharArray());
        AddStringToStruct(aliases, "MoreThan".toCharArray(), "Gt".toCharArray());
        AddStringToStruct(aliases, "LessThanOrEqual".toCharArray(), "Lte".toCharArray());

        AddStringToStruct(aliases, "ShiftLeft".toCharArray(), "Shl".toCharArray());
        AddStringToStruct(aliases, "ShiftRight".toCharArray(), "Shr".toCharArray());
        AddStringToStruct(aliases, "ShiftArithmeticRight".toCharArray(), "Sal".toCharArray());

        return aliases;
    }

    public static boolean WriteCHeader(Ast ast, StringReference cInclude, StringReference message) {
        double i;
        boolean success;
        LinkedListCharacters ll;
        StringReference tmp;

        success = true;
        ll = CreateLinkedListCharacter();

        LinkedListCharactersAddString(ll, ("#include <stdint.h>" + "\n").toCharArray());
        LinkedListCharactersAddString(ll, ("#include <stdbool.h>" + "\n").toCharArray());
        LinkedListCharactersAddString(ll, ("#include <uchar.h>" + "\n").toCharArray());
        LinkedListCharactersAddString(ll, ("#include <immintrin.h>" + "\n").toCharArray());
        LinkedListCharactersAddString(ll, ("\n").toCharArray());

        for(i = 0d; i < ast.st.length && success; i = i + 1d){
            tmp = new StringReference();
            success = PrintCStructure(ast.st[(int)i], tmp, message);
            if(success){
                LinkedListCharactersAddString(ll, tmp.string);
            }
        }

        for(i = 0d; i < ast.fnc.length && success; i = i + 1d){
            tmp = new StringReference();
            success = PrintCFunctionPrototype(ast.fnc[(int)i], tmp, message);
            if(success){
                LinkedListCharactersAddString(ll, tmp.string);
            }
        }

        if(success) {
            cInclude.string = LinkedListCharactersToArray(ll);
            FreeLinkedListCharacter(ll);
        }

        return success;
    }

    private static boolean PrintCFunctionPrototype(Function fnc, StringReference cProRef, StringReference message) {
        boolean success;
        char [] functionStructureName;
        LinkedListCharacters ll;

        success = true;
        ll = CreateLinkedListCharacter();

        functionStructureName = (new String(fnc.name) + "S").toCharArray();

        LinkedListCharactersAddString(ll, ("void " + new String(fnc.name) + "(struct " + new String(functionStructureName) + " *args);").toCharArray());
        LinkedListCharactersAddString(ll, "\n".toCharArray());

        cProRef.string = LinkedListCharactersToArray(ll);
        FreeLinkedListCharacter(ll);

        return success;
    }

    private static boolean PrintCStructure(Struct st, StringReference cStRef, StringReference message) {
        double i;
        boolean success;
        LinkedListCharacters ll;
        StringReference tmp;

        success = true;
        ll = CreateLinkedListCharacter();

        LinkedListCharactersAddString(ll, ("#pragma pack(push, 1)" + "\n").toCharArray());
        LinkedListCharactersAddString(ll, ("struct " + new String(st.name) + "{" + "\n").toCharArray());

        for(i = 0d; i < st.vars.length && success; i = i + 1d){
            tmp = new StringReference();
            success = PrintCVariable(st.vars[(int)i], tmp, message);
            if(success){
                LinkedListCharactersAddString(ll, tmp.string);
            }
        }

        LinkedListCharactersAddString(ll, ("};" + "\n").toCharArray());
        LinkedListCharactersAddString(ll, ("#pragma pack(pop)" + "\n").toCharArray());
        LinkedListCharactersAddString(ll, "\n".toCharArray());

        cStRef.string = LinkedListCharactersToArray(ll);

        return success;
    }

    private static boolean PrintCVariable(Var var, StringReference cVarRef, StringReference message) {
        DataReference entryTypesRef;
        boolean success;
        LinkedListCharacters ll;

        entryTypesRef = new DataReference();
        ll = CreateLinkedListCharacter();

        success = GetEntryTypes(entryTypesRef, message);

        if(success){
            if(StructHasKey(entryTypesRef.data.structure, var.type)) {
                Structure data = GetStructFromStruct(entryTypesRef.data.structure, var.type);
                if (StructHasKey(data, "ctype".toCharArray())) {
                    char[] ctype = GetStringFromStruct(data, "ctype".toCharArray());

                    LinkedListCharactersAddString(ll, "  ".toCharArray());
                    LinkedListCharactersAddString(ll, ctype);
                    LinkedListCharactersAddString(ll, " ".toCharArray());
                    LinkedListCharactersAddString(ll, var.name);
                    LinkedListCharactersAddString(ll, ";".toCharArray());
                    LinkedListCharactersAddString(ll, "\n".toCharArray());
                } else {
                    success = false;
                    message.string = ("Could not find C type for type (2): " + new String(var.type)).toCharArray();
                }
            }else if(var.isStruct){
                LinkedListCharactersAddString(ll, "  struct ".toCharArray());
                LinkedListCharactersAddString(ll, var.type);
                if(!var.isStructArray) {
                    LinkedListCharactersAddString(ll, " * ".toCharArray());
                }else{
                    LinkedListCharactersAddString(ll, " ** ".toCharArray());
                }
                LinkedListCharactersAddString(ll, var.name);
                LinkedListCharactersAddString(ll, ";".toCharArray());
                LinkedListCharactersAddString(ll, "\n".toCharArray());
            }else{
                success = false;
                message.string = ("Could not find C type for type (1): " + new String(var.type)).toCharArray();
            }
        }

        if(success){
            cVarRef.string = LinkedListCharactersToArray(ll);
            FreeLinkedListCharacter(ll);
        }

        return success;
    }
}
