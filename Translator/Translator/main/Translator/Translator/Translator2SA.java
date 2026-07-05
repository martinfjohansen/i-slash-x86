package Translator.Translator;

import DataStructures.Array.Structures.Array;
import references.references.StringReference;

import java.util.Stack;

import static DataStructures.Array.Arrays.Arrays.*;
import static Translator.Translator.Translator2.IsValidNumber;
import static Translator.Translator.Translator2.StringIsInArray;
import static arrays.arrays.arrays.StringsEqual;
import static strings.strings.strings.*;

public class Translator2SA {
    public static boolean StaticAnalysis(Ast ast, StringReference message) {
        boolean success;
        double i, j, k;
        Function fnc;
        Struct st;

        success = true;

        // Checks

        // Check that all structures have unique names.
        if(success) {
            success = CheckUniqueStructureNames(ast.st, message);
        }

        // Check that all functions have unique names.
        if(success){
            success = CheckUniqueFunctionNames(ast.fnc, message);
        }

        // Check that all structure variables have unique names within that structure.
        if(success){
            for(i = 0; i < ast.st.length && success; i = i + 1d) {
                st = ast.st[(int) i];
                success = CheckUnqiueVariableNames(st, message);
            }
        }

        // Check that a structure exists for each function (the function structure).
        if(success){
            for(i = 0; i < ast.fnc.length && success; i = i + 1d) {
                fnc = ast.fnc[(int) i];
                success = CheckExistenceOfFunctionStructure(fnc, ast.st, message);
            }
        }

        // Check that all variables are declared, or that structures or functions exists. Assign
        if(success){
            Array special = CreateArray();

            ArrayAddString(special, "Acr".toCharArray());
            ArrayAddString(special, "Acw".toCharArray());
            ArrayAddString(special, "New".toCharArray());
            ArrayAddString(special, "Call".toCharArray());

            for(i = 0; i < ast.fnc.length && success; i = i + 1d) {
                fnc = ast.fnc[(int) i];

                for(j = 0; j < fnc.ins.length && success; j = j + 1d) {
                    Instruction ins = fnc.ins[(int) j];

                    if(!StringIsInArray(ins.name, special)) {
                        for (k = 0; k < ins.params.length && success; k = k + 1d) {
                            Param param = ins.params[(int) k];

                            if (StringsEqual(param.type, "var".toCharArray())) {
                                success = CheckThatVariableIsDeclared(param, fnc.functionStructure, message);
                            }
                        }
                    }else if(StringsEqual(ins.name, "Acw".toCharArray())) {
                        success = CheckAcw(ast, ins, fnc, message);
                    }else if(StringsEqual(ins.name, "Acr".toCharArray())) {
                        success = CheckAcr(ast, ins, fnc, message);
                    }else if(StringsEqual(ins.name, "Call".toCharArray())) {
                        success = CheckCall(ast, ins, fnc, message);
                    }else if(StringsEqual(ins.name, "New".toCharArray())) {
                        success = CheckNew(ast, ins, fnc, message);
                    }else{
                        success = false;
                        message.string = ("Missing check for: " + new String(ins.name)).toCharArray();
                    }
                }
            }
        }

        // Check parameter count
        if(success){
            for(i = 0; i < ast.fnc.length && success; i = i + 1d) {
                fnc = ast.fnc[(int) i];

                for(j = 0; j < fnc.ins.length && success; j = j + 1d) {
                    Instruction ins = fnc.ins[(int) j];

                    success = CheckParameterCount(ins, message);
                }
            }
        }

        // Check parameter types
        if(success){
            for(i = 0; i < ast.fnc.length && success; i = i + 1d) {
                fnc = ast.fnc[(int) i];

                for(j = 0; j < fnc.ins.length && success; j = j + 1d) {
                    Instruction ins = fnc.ins[(int) j];

                    success = CheckParameterTypes(ins, message);
                }
            }
        }

        // Check control flow and compute labels
        if(success){
            for(i = 0; i < ast.fnc.length && success; i = i + 1d) {
                fnc = ast.fnc[(int) i];

                success = CheckControlFlowAndComputeLabels(fnc, message);
            }
        }

        return success;
    }

    private static boolean CheckNew(Ast ast, Instruction ins, Function fnc, StringReference message) {
        boolean success;

        success = true;

        // Assigned to variable
        Param param = ins.params[0];

        if (StringsEqual(param.type, "var".toCharArray())) {
            success = CheckThatVariableIsDeclared(param, fnc.functionStructure, message);
        }

        // Structure
        // TODO: Check that the structure exists.
        param = ins.params[1];

        return success;
    }

    private static boolean CheckCall(Ast ast, Instruction ins, Function fnc, StringReference message) {
        boolean success, found;
        double i;

        // Parameter variable
        Param param = ins.params[1];

        if (StringsEqual(param.type, "var".toCharArray())) {
            success = CheckThatVariableIsDeclared(param, fnc.functionStructure, message);
        }

        // Check that function exists
        param = ins.params[0];
        found = false;
        for(i = 0; i < ast.fnc.length; i = i + 1d){
            Function af = ast.fnc[(int)i];

            if(StringsEqual(af.name, param.varname)){
                found = true;
                param.var = new Var();
                param.var.isFunction = true;
                param.var.function = af;
            }
        }

        if(found){
            success = true;
        }else{
            success = false;
            message.string = ("Function in call not declared: " + new String(param.varname)).toCharArray();
        }

        return success;
    }

    private static boolean CheckAcr(Ast ast, Instruction ins, Function fnc, StringReference message) {
        boolean success = true, found;
        double k;
        Struct st;

        // Assigned to variable
        Param param = ins.params[0];

        if (StringsEqual(param.type, "var".toCharArray())) {
            success = CheckThatVariableIsDeclared(param, fnc.functionStructure, message);
        }

        // Read-from struct
        if(success) {
            param = ins.params[1];

            if (StringsEqual(param.type, "var".toCharArray())) {
                success = CheckThatVariableIsDeclared(param, fnc.functionStructure, message);
            }

            if(success){
                found = false;
                for (k = 0; k < ast.st.length && !found; k = k + 1d) {
                    if (StringsEqual(ast.st[(int) k].name, ins.params[1].var.type)) {
                        found = true;
                        param.var.st = ast.st[(int) k];
                    }
                }

                if(found){

                }else{
                    success = false;
                    message.string = ("Structure used in Acr does not exist: " + new String(param.var.type)).toCharArray();
                }
            }

        }

        if(success){
            st = param.var.st;

            // Structure element.
            param = ins.params[2];
            found = false;
            for (k = 0; k < st.vars.length && !found; k = k + 1d) {
                if (StringsEqual(st.vars[(int) k].name, param.varname)) {
                    found = true;
                    param.var = st.vars[(int) k];
                }
            }

            if (found) {
                success = true;
            } else {
                success = false;
                message.string = ("Element access is not in struct: " + new String(ins.params[0].varname)).toCharArray();
            }

        }

        return success;
    }

    private static boolean CheckAcw(Ast ast, Instruction ins, Function fnc, StringReference message) {
        double k;
        Struct st;
        boolean success;
        // Assigned to struct
        Param param = ins.params[0];

        if (StringsEqual(param.type, "var".toCharArray())) {
            success = CheckThatVariableIsDeclared(param, fnc.functionStructure, message);
        }

        // Structure element.
        param = ins.params[1];
        //System.out.println(ins.params[0].var.isStruct);
        //System.out.println(ins.params[0].var.type);

        // Find structure
        boolean found = false;
        for(k = 0; k < ast.st.length && !found; k = k + 1d){
            if(StringsEqual(ast.st[(int)k].name, ins.params[0].var.type)){
                found = true;
                ins.params[0].var.st = ast.st[(int)k];
            }
        }

        if(found) {
            success = true;

            st = ins.params[0].var.st;
            found = false;
            for(k = 0; k < st.vars.length && !found; k = k + 1d){
                if(StringsEqual(st.vars[(int)k].name, param.varname)){
                    found = true;
                    param.var = st.vars[(int)k];
                }
            }

            if(found){
                success = true;
            }else{
                success = false;
                message.string = ("Element access is not in struct: " + new String(ins.params[0].varname)).toCharArray();
            }
        }else{
            success = false;
            message.string = ("Structure used in Acw does not exist: " + new String(ins.params[0].var.type)).toCharArray();
        }

        // Check that element exists


        // Assigned from variable
        if(success){
            param = ins.params[2];

            if (StringsEqual(param.type, "var".toCharArray())) {
                success = CheckThatVariableIsDeclared(param, fnc.functionStructure, message);
            }
        }
        return success;
    }

    private static boolean CheckUnqiueVariableNames(Struct st, StringReference message) {
        double i;
        boolean valid;
        Array names;

        names = CreateArray();
        valid = true;

        for(i = 0d; i < st.vars.length; i = i + 1d){
            Var var = st.vars[(int) i];

            if(!StringIsInArray(var.name, names)){
                ArrayAddString(names, var.name);
            }else{
                valid = false;
                message.string = ("Two variables in a structure have the name name: " + new String(var.name)).toCharArray();
            }
        }

        return valid;
    }

    private static boolean CheckControlFlowAndComputeLabels(Function fnc, StringReference message) {
        double i, ind;
        boolean success, loopPreState;
        Stack<String> labels;
        int next;
        String label;

        next = 1;

        labels = new Stack<>();

        success = true;

        ind = 1d;
        loopPreState = false;

        for(i = 0d; i < fnc.ins.length; i = i + 1d){
            Instruction ins = fnc.ins[(int) i];

            ins.indentation = ind;

            if(StringsEqual(ins.name, "If".toCharArray())){
                if(!loopPreState) {
                    ind = ind + 1d;
                }else{
                    ins.indentation = ins.indentation - 1d;
                }

                label = "L" + next;
                next = next + 1;
                ins.label1 = label.toCharArray();
                labels.push(label);

                loopPreState = false;
            }

            if(StringsEqual(ins.name, "Else".toCharArray())){
                ins.indentation = ins.indentation - 1d;

                if(!labels.empty()) {
                    ins.label1 = labels.pop().toCharArray();
                }

                label = "L" + next;
                next = next + 1;
                ins.label2 = label.toCharArray();
                labels.push(label);
            }

            if(StringsEqual(ins.name, "Loop".toCharArray())){
                ind = ind + 1d;
                loopPreState = true;

                label = "L" + next;
                next = next + 1;
                ins.label1 = label.toCharArray();
                labels.push(label);
            }

            if(StringsEqual(ins.name, "Endb".toCharArray())){
                ind = ind - 1d;
                ins.indentation = ins.indentation - 1d;

                if(!labels.empty()) {
                    ins.label1 = labels.pop().toCharArray();
                }
            }

            if(StringsEqual(ins.name, "EndLoop".toCharArray())){
                ind = ind - 1d;
                ins.indentation = ins.indentation - 1d;

                if(!labels.empty()) {
                    ins.label2 = labels.pop().toCharArray();
                }
                if(!labels.empty()) {
                    ins.label1 = labels.pop().toCharArray();
                }
            }
        }

        if(ind == 1){
            // OK, balanced
        }else{
            success = false;
            message.string = "Blocks were not balanced out.".toCharArray();
        }

        return success;
    }

    public static boolean CheckParameterCount(Instruction ins, StringReference message) {
        boolean valid, found;
        Array p0, p1, p2, p3, p4, p5;
        Array [] ps;
        double i, j;

        valid = false;

        p0 = CreateArray();

        ArrayAddString(p0, "Loop".toCharArray());
        ArrayAddString(p0, "EndLoop".toCharArray());
        ArrayAddString(p0, "Endb".toCharArray());
        ArrayAddString(p0, "Else".toCharArray());
        ArrayAddString(p0, "Nop".toCharArray());

        p1 = CreateArray();

        ArrayAddString(p1, "Inc".toCharArray());
        ArrayAddString(p1, "Dec".toCharArray());
        ArrayAddString(p1, "Del".toCharArray());
        ArrayAddString(p1, "Rdrand".toCharArray());
        ArrayAddString(p1, "Rdtsc".toCharArray());
        ArrayAddString(p1, "If".toCharArray());

        p2 = CreateArray();

        ArrayAddString(p2, "Round".toCharArray());
        ArrayAddString(p2, "Floor".toCharArray());
        ArrayAddString(p2, "Ceil".toCharArray());
        ArrayAddString(p2, "Truncate".toCharArray());
        ArrayAddString(p2, "Abs".toCharArray());
        ArrayAddString(p2, "Mov".toCharArray());
        ArrayAddString(p2, "Broadcast".toCharArray());
        ArrayAddString(p2, "Len".toCharArray());
        ArrayAddString(p2, "Not".toCharArray());
        ArrayAddString(p2, "Call".toCharArray());
        ArrayAddString(p2, "New".toCharArray());
        ArrayAddString(p2, "Set".toCharArray());
        ArrayAddString(p2, "Movzx".toCharArray());
        ArrayAddString(p2, "u8tou16".toCharArray());
        ArrayAddString(p2, "u8tou32".toCharArray());
        ArrayAddString(p2, "u16tou32".toCharArray());
        ArrayAddString(p2, "u32tou64".toCharArray());
        ArrayAddString(p2, "u8tos16".toCharArray());
        ArrayAddString(p2, "u16tos32".toCharArray());
        ArrayAddString(p2, "u32tos64".toCharArray());
        ArrayAddString(p2, "s8tos16".toCharArray());
        ArrayAddString(p2, "s16tos32".toCharArray());
        ArrayAddString(p2, "s32tos64".toCharArray());
        ArrayAddString(p2, "u16tof32".toCharArray());
        ArrayAddString(p2, "s16tof32".toCharArray());
        ArrayAddString(p2, "u32tof64".toCharArray());
        ArrayAddString(p2, "s32tof64".toCharArray());
        ArrayAddString(p2, "f32tof64".toCharArray());
        ArrayAddString(p2, "u16tou8".toCharArray());
        ArrayAddString(p2, "u32tou16".toCharArray());
        ArrayAddString(p2, "u64tou32".toCharArray());
        ArrayAddString(p2, "s16tou8".toCharArray());
        ArrayAddString(p2, "s32tou16".toCharArray());
        ArrayAddString(p2, "s64tou32".toCharArray());
        ArrayAddString(p2, "s16tos8".toCharArray());
        ArrayAddString(p2, "s32tos16".toCharArray());
        ArrayAddString(p2, "s64tos32".toCharArray());
        ArrayAddString(p2, "f32tou16".toCharArray());
        ArrayAddString(p2, "f32tos16".toCharArray());
        ArrayAddString(p2, "f64tou32".toCharArray());
        ArrayAddString(p2, "f64tos32".toCharArray());
        ArrayAddString(p2, "f64tof32".toCharArray());
        ArrayAddString(p2, "u8x8tou16x8".toCharArray());
        ArrayAddString(p2, "u8x4tou32x4".toCharArray());
        ArrayAddString(p2, "u8x2tou64x2".toCharArray());
        ArrayAddString(p2, "u16x4tou32x4".toCharArray());
        ArrayAddString(p2, "u32x2tou64x2".toCharArray());
        ArrayAddString(p2, "s8x8tos16x8".toCharArray());
        ArrayAddString(p2, "s8x4tos32x4".toCharArray());
        ArrayAddString(p2, "s64x2tos32x2".toCharArray());
        ArrayAddString(p2, "f64x2tof32x2".toCharArray());
        ArrayAddString(p2, "f32x4tof16x4".toCharArray());
        ArrayAddString(p2, "f64x4tof32x4".toCharArray());
        ArrayAddString(p2, "f32x8tof16x8".toCharArray());
        ArrayAddString(p2, "f32x4tof64x4".toCharArray());
        ArrayAddString(p2, "Xu8".toCharArray());
        ArrayAddString(p2, "Xu16".toCharArray());
        ArrayAddString(p2, "Xu32".toCharArray());
        ArrayAddString(p2, "Xu64".toCharArray());
        ArrayAddString(p2, "Xu8a".toCharArray());
        ArrayAddString(p2, "Xu16a".toCharArray());
        ArrayAddString(p2, "Xu64a".toCharArray());
        ArrayAddString(p2, "Xb8".toCharArray());
        ArrayAddString(p2, "Xb16".toCharArray());
        ArrayAddString(p2, "Xu16".toCharArray());
        ArrayAddString(p2, "Xb32".toCharArray());
        ArrayAddString(p2, "Xb64".toCharArray());
        ArrayAddString(p2, "Xb128".toCharArray());
        ArrayAddString(p2, "Xb256".toCharArray());
        ArrayAddString(p2, "Xu32x4".toCharArray());
        ArrayAddString(p2, "Xu16x8a".toCharArray());
        ArrayAddString(p2, "Xu8x16a".toCharArray());
        ArrayAddString(p2, "Xu8x16".toCharArray());
        ArrayAddString(p2, "Xu16x8a".toCharArray());
        ArrayAddString(p2, "Xu16x16a".toCharArray());
        ArrayAddString(p2, "Xu16x16a".toCharArray());
        ArrayAddString(p2, "Xu16x32a".toCharArray());
        ArrayAddString(p2, "Xb8x16".toCharArray());
        ArrayAddString(p2, "u32tou16s".toCharArray());
        ArrayAddString(p2, "s32tos16s".toCharArray());
        ArrayAddString(p2, "Xchg".toCharArray());
        ArrayAddString(p2, "Xadd".toCharArray());
        ArrayAddString(p2, "Bswap".toCharArray());
        ArrayAddString(p2, "Bsf".toCharArray());
        ArrayAddString(p2, "Bsr".toCharArray());
        ArrayAddString(p2, "Crc32".toCharArray());
        ArrayAddString(p2, "Popcnt".toCharArray());
        ArrayAddString(p2, "Lzcnt".toCharArray());
        ArrayAddString(p2, "Tzcnt".toCharArray());
        ArrayAddString(p2, "Rdpmc".toCharArray());
        ArrayAddString(p2, "Blsi".toCharArray());
        ArrayAddString(p2, "Blsmsk".toCharArray());
        ArrayAddString(p2, "Blsr".toCharArray());
        ArrayAddString(p2, "ExtractMask".toCharArray());
        ArrayAddString(p2, "Sqrt".toCharArray());
        ArrayAddString(p2, "ApproxReciprocal".toCharArray());
        ArrayAddString(p2, "ApproxReciprocalSqrt".toCharArray());
        ArrayAddString(p2, "MovAndDuplicate".toCharArray());
        ArrayAddString(p2, "MovAndDuplicateOdd".toCharArray());
        ArrayAddString(p2, "Rdrand".toCharArray());

        p3 = CreateArray();
        ArrayAddString(p3, "Add".toCharArray());
        ArrayAddString(p3, "Sub".toCharArray());
        ArrayAddString(p3, "Mul".toCharArray());
        ArrayAddString(p3, "MulFull".toCharArray());
        ArrayAddString(p3, "Div".toCharArray());
        ArrayAddString(p3, "DivFull".toCharArray());
        ArrayAddString(p3, "Mod".toCharArray());
        ArrayAddString(p3, "AddSaturated".toCharArray());
        ArrayAddString(p3, "SubSaturated".toCharArray());
        ArrayAddString(p3, "Lt".toCharArray());
        ArrayAddString(p3, "LessThan".toCharArray());
        ArrayAddString(p3, "Lte".toCharArray());
        ArrayAddString(p3, "Gt".toCharArray());
        ArrayAddString(p3, "Gte".toCharArray());
        ArrayAddString(p3, "And".toCharArray());
        ArrayAddString(p3, "Andnot".toCharArray());
        ArrayAddString(p3, "Idr".toCharArray());
        ArrayAddString(p3, "Eq".toCharArray());
        ArrayAddString(p3, "Neq".toCharArray());
        ArrayAddString(p3, "Unequal".toCharArray());
        ArrayAddString(p3, "Equal".toCharArray());
        ArrayAddString(p3, "MoreThan".toCharArray());
        ArrayAddString(p3, "MoreThanOrEqual".toCharArray());
        ArrayAddString(p3, "LessThanOrEqual".toCharArray());
        ArrayAddString(p3, "Acw".toCharArray());
        ArrayAddString(p3, "Acr".toCharArray());
        ArrayAddString(p3, "Shl".toCharArray());
        ArrayAddString(p3, "Shr".toCharArray());
        ArrayAddString(p3, "Sar".toCharArray());
        ArrayAddString(p3, "Or".toCharArray());
        ArrayAddString(p3, "Xor".toCharArray());
        ArrayAddString(p3, "Idw".toCharArray());
        ArrayAddString(p3, "Ror".toCharArray());
        ArrayAddString(p3, "Cmov".toCharArray());
        ArrayAddString(p3, "Rol".toCharArray());
        ArrayAddString(p3, "Bt".toCharArray());
        ArrayAddString(p3, "Movs".toCharArray());
        ArrayAddString(p3, "Bzhi".toCharArray());
        ArrayAddString(p3, "Pdep".toCharArray());
        ArrayAddString(p3, "Pext".toCharArray());
        ArrayAddString(p3, "Shuffle".toCharArray());
        ArrayAddString(p3, "MulStoreHigh".toCharArray());
        ArrayAddString(p3, "Avg".toCharArray());
        ArrayAddString(p3, "Min".toCharArray());
        ArrayAddString(p3, "Max".toCharArray());
        ArrayAddString(p3, "InterleaveLow".toCharArray());
        ArrayAddString(p3, "InterleaveHigh".toCharArray());
        ArrayAddString(p3, "SumAbsoluteDifference".toCharArray());
        ArrayAddString(p3, "PairwiseAdd".toCharArray());
        ArrayAddString(p3, "PairwiseSub".toCharArray());
        ArrayAddString(p3, "PairwiseMulAdd".toCharArray());
        ArrayAddString(p3, "AlternatingSubAdd".toCharArray());
        ArrayAddString(p3, "ConditionalNegate".toCharArray());
        ArrayAddString(p3, "DotProduct".toCharArray());
        ArrayAddString(p3, "ShiftLeft".toCharArray());
        ArrayAddString(p3, "ShiftRight".toCharArray());
        ArrayAddString(p3, "ShiftArithmeticRight".toCharArray());

        p4 = CreateArray();
        ArrayAddString(p4, "DivMod".toCharArray());
        ArrayAddString(p4, "MulDiv".toCharArray());
        ArrayAddString(p4, "Idro".toCharArray());
        ArrayAddString(p4, "Cmoveq".toCharArray());
        ArrayAddString(p4, "Cmps".toCharArray());
        ArrayAddString(p4, "Scas".toCharArray());
        ArrayAddString(p4, "Bextr".toCharArray());
        ArrayAddString(p4, "SelectiveCopy".toCharArray());
        ArrayAddString(p4, "MultiplyAndAdd".toCharArray());
        ArrayAddString(p4, "CombineExtract".toCharArray());
        ArrayAddString(p4, "Gather".toCharArray());

        p5 = CreateArray();
        ArrayAddString(p5, "StringSubset".toCharArray());
        ArrayAddString(p5, "StringRangeCheck".toCharArray());
        ArrayAddString(p5, "MatchString".toCharArray());
        ArrayAddString(p5, "FindSubstring".toCharArray());

        ps = new Array[6];
        ps[0] = p0;
        ps[1] = p1;
        ps[2] = p2;
        ps[3] = p3;
        ps[4] = p4;
        ps[5] = p5;

        found = false;
        for(i = 0d; i < ps.length; i = i + 1d){
            Array p = ps[(int) i];
            for(j = 0d; j < ArrayLength(p); j = j + 1d){
                if(StringsEqual(ins.name, ArrayIndexString(p, j))){
                    if(ins.params.length == i) {
                        valid = true;
                        found = true;
                    }else{
                        valid = false;
                        message.string = ("Wrong parameter count for instruction: " + new String(ins.name)).toCharArray();
                    }
                }
            }
        }

        if(found){
        }else{
            message.string = ("Parameter count of instruction unknown: " + new String(ins.name)).toCharArray();
        }

        return valid;
    }

    public static boolean CheckThatVariableIsDeclared(Param param, Struct fncSt, StringReference message) {
        double i;
        boolean found;

        found = false;

        for(i = 0d; i < fncSt.vars.length; i = i + 1d){
            Var var = fncSt.vars[(int) i];

            if(StringsEqual(param.varname, var.name)){
                found = true;
                param.var = var;
            }
        }

        if(found){

        }else{
            message.string = ("Variable not declared: " + new String(param.varname)).toCharArray();
        }

        return found;
    }

    public static boolean CheckExistenceOfFunctionStructure(Function fnc, Struct [] sts, StringReference message) {
        double i;
        boolean found;
        char [] expectedName;

        found = false;
        expectedName = (new String(fnc.name) + "S").toCharArray();

        for(i = 0d; i < sts.length; i = i + 1d){
            Struct st = sts[(int)i];

            if(StringsEqual(st.name, expectedName)){
                found = true;
                fnc.functionStructure = st;
            }
        }

        if(found){

        }else{
            message.string = ("Function structure not found: " + new String(expectedName)).toCharArray();
        }

        return found;
    }

    public static boolean CheckUniqueFunctionNames(Function[] fncs, StringReference message) {
        double i;
        boolean valid;
        Array names;

        names = CreateArray();
        valid = true;

        for(i = 0d; i < fncs.length; i = i + 1d){
            Function fnc = fncs[(int) i];

            if(!StringIsInArray(fnc.name, names)){
                ArrayAddString(names, fnc.name);
            }else{
                valid = false;
                message.string = ("Two functions have the name name: " + new String(fnc.name)).toCharArray();
            }
        }

        return valid;
    }

    public static boolean CheckUniqueStructureNames(Struct[] sts, StringReference message) {
        double i;
        boolean valid;
        Array names;

        names = CreateArray();
        valid = true;

        for(i = 0d; i < sts.length; i = i + 1d){
            Struct struct = sts[(int) i];

            if(!StringIsInArray(struct.name, names)){
                ArrayAddString(names, struct.name);
            }else{
                valid = false;
                message.string = ("Two structures have the name name: " + new String(struct.name)).toCharArray();
            }
        }

        return valid;
    }

    public static boolean CheckParameterTypes(Instruction ins, StringReference message) {
        boolean valid;
        char [] targetType;
        Array sameAsAssigneeNumber, numbersToBits;
        double i;

        valid = true;

        // All parameters are the same, all are numbers. The type of the assigned var is the type of the instruction.
        sameAsAssigneeNumber = CreateArray();
        ArrayAddString(sameAsAssigneeNumber, "Add".toCharArray());
        ArrayAddString(sameAsAssigneeNumber, "Sub".toCharArray());
        ArrayAddString(sameAsAssigneeNumber, "Mul".toCharArray());
        ArrayAddString(sameAsAssigneeNumber, "Div".toCharArray());
        ArrayAddString(sameAsAssigneeNumber, "DivMod".toCharArray());
        ArrayAddString(sameAsAssigneeNumber, "Mod".toCharArray());
        ArrayAddString(sameAsAssigneeNumber, "MulDiv".toCharArray());
        ArrayAddString(sameAsAssigneeNumber, "AddSaturated".toCharArray());
        ArrayAddString(sameAsAssigneeNumber, "SubSaturated".toCharArray());
        ArrayAddString(sameAsAssigneeNumber, "Round".toCharArray());
        ArrayAddString(sameAsAssigneeNumber, "Floor".toCharArray());
        ArrayAddString(sameAsAssigneeNumber, "Ceil".toCharArray());
        ArrayAddString(sameAsAssigneeNumber, "Truncate".toCharArray());
        ArrayAddString(sameAsAssigneeNumber, "Abs".toCharArray());
        ArrayAddString(sameAsAssigneeNumber, "Inc".toCharArray());
        ArrayAddString(sameAsAssigneeNumber, "Dec".toCharArray());
        ArrayAddString(sameAsAssigneeNumber, "MulStoreHigh".toCharArray());
        ArrayAddString(sameAsAssigneeNumber, "MultiplyAndAdd".toCharArray());
        ArrayAddString(sameAsAssigneeNumber, "Avg".toCharArray());
        ArrayAddString(sameAsAssigneeNumber, "Min".toCharArray());
        ArrayAddString(sameAsAssigneeNumber, "Max".toCharArray());
        ArrayAddString(sameAsAssigneeNumber, "Sqrt".toCharArray());
        ArrayAddString(sameAsAssigneeNumber, "ApproxReciprocal".toCharArray());
        ArrayAddString(sameAsAssigneeNumber, "ApproxReciprocalSqrt".toCharArray());
        ArrayAddString(sameAsAssigneeNumber, "SumAbsoluteDifference".toCharArray());
        ArrayAddString(sameAsAssigneeNumber, "PairwiseAdd".toCharArray());
        ArrayAddString(sameAsAssigneeNumber, "PairwiseSub".toCharArray());
        ArrayAddString(sameAsAssigneeNumber, "PairwiseMulAdd".toCharArray());
        ArrayAddString(sameAsAssigneeNumber, "AlternatingSubAdd".toCharArray());
        ArrayAddString(sameAsAssigneeNumber, "ConditionalNegate".toCharArray());
        ArrayAddString(sameAsAssigneeNumber, "MovAndDuplicate".toCharArray());
        ArrayAddString(sameAsAssigneeNumber, "MovAndDuplicateOdd".toCharArray());
        ArrayAddString(sameAsAssigneeNumber, "DotProduct".toCharArray());

        // These return a boolean. The type is the type of one of the: b <- num or bw <- num. If both are immediates, use f64
        numbersToBits = CreateArray();
        ArrayAddString(numbersToBits, "Lt".toCharArray());
        ArrayAddString(numbersToBits, "Lte".toCharArray());
        ArrayAddString(numbersToBits, "Gt".toCharArray());
        ArrayAddString(numbersToBits, "Gte".toCharArray());
        ArrayAddString(numbersToBits, "LessThan".toCharArray());
        ArrayAddString(numbersToBits, "MoreThan".toCharArray());

        // These work on other types as well
        Array dataToBits = CreateArray();
        ArrayAddString(dataToBits, "Eq".toCharArray());
        ArrayAddString(dataToBits, "Neq".toCharArray());
        ArrayAddString(dataToBits, "Unequal".toCharArray());
        ArrayAddString(dataToBits, "Equal".toCharArray());

        // These take a bitfield and return a number.
        Array bitfieldToNumber = CreateArray();
        ArrayAddString(bitfieldToNumber, "Popcnt".toCharArray());
        ArrayAddString(bitfieldToNumber, "Lzcnt".toCharArray());
        ArrayAddString(bitfieldToNumber, "Tzcnt".toCharArray());
        ArrayAddString(bitfieldToNumber, "Blsi".toCharArray());

        // These are bitfields for all parameters
        Array sameAsAssigneeBitfields = CreateArray();
        ArrayAddString(sameAsAssigneeBitfields, "Not".toCharArray());
        ArrayAddString(sameAsAssigneeBitfields, "And".toCharArray());
        ArrayAddString(sameAsAssigneeBitfields, "Andnot".toCharArray());
        ArrayAddString(sameAsAssigneeBitfields, "Or".toCharArray());
        ArrayAddString(sameAsAssigneeBitfields, "Xor".toCharArray());
        ArrayAddString(sameAsAssigneeBitfields, "Pdep".toCharArray());
        ArrayAddString(sameAsAssigneeBitfields, "Pext".toCharArray());

        // bw <- bw, number
        Array bitwiseAndNumberToBitwise = CreateArray();
        ArrayAddString(bitwiseAndNumberToBitwise, "Shl".toCharArray());
        ArrayAddString(bitwiseAndNumberToBitwise, "Shr".toCharArray());
        ArrayAddString(bitwiseAndNumberToBitwise, "Sar".toCharArray());
        ArrayAddString(bitwiseAndNumberToBitwise, "Ror".toCharArray());
        ArrayAddString(bitwiseAndNumberToBitwise, "Rol".toCharArray());
        ArrayAddString(bitwiseAndNumberToBitwise, "ShiftLeft".toCharArray());
        ArrayAddString(bitwiseAndNumberToBitwise, "ShiftRight".toCharArray());
        ArrayAddString(bitwiseAndNumberToBitwise, "ShiftArithmeticLeft".toCharArray());

        // Conversion
        Array conversion = CreateArray();
        ArrayAddString(conversion, "s8tos16".toCharArray());
        ArrayAddString(conversion, "s16tof32".toCharArray());
        ArrayAddString(conversion, "f32tof64".toCharArray());
        ArrayAddString(conversion, "s16tos8".toCharArray());
        ArrayAddString(conversion, "f32tos16".toCharArray());
        ArrayAddString(conversion, "f64tof32".toCharArray());
        ArrayAddString(conversion, "u8tou16".toCharArray());
        ArrayAddString(conversion, "u8tou32".toCharArray());
        ArrayAddString(conversion, "u8x8tou16x8".toCharArray());
        ArrayAddString(conversion, "u8x4tou32x4".toCharArray());
        ArrayAddString(conversion, "u16tou32".toCharArray());
        ArrayAddString(conversion, "u32tou64".toCharArray());
        ArrayAddString(conversion, "f64x2tof32x2".toCharArray());
        ArrayAddString(conversion, "f64x4tof32x4".toCharArray());
        ArrayAddString(conversion, "f32x4tof16x4".toCharArray());
        ArrayAddString(conversion, "f32x8tof16x8".toCharArray());
        ArrayAddString(conversion, "f32x4tof64x4".toCharArray());
        ArrayAddString(conversion, "u32tou16s".toCharArray());

        // Reinterpretation
        Array reint = CreateArray();
        ArrayAddString(reint, "Xu16x8a".toCharArray());
        ArrayAddString(reint, "Xu32x4".toCharArray());
        ArrayAddString(reint, "Xu8x16a".toCharArray());
        ArrayAddString(reint, "Xu8x16".toCharArray());
        ArrayAddString(reint, "Xb8x16".toCharArray());
        ArrayAddString(reint, "Xb8".toCharArray());
        ArrayAddString(reint, "Xb16".toCharArray());
        ArrayAddString(reint, "Xb32".toCharArray());
        ArrayAddString(reint, "Xb64".toCharArray());
        ArrayAddString(reint, "Xu8".toCharArray());
        ArrayAddString(reint, "Xu16".toCharArray());
        ArrayAddString(reint, "Xu32".toCharArray());
        ArrayAddString(reint, "Xu64".toCharArray());
        ArrayAddString(reint, "Xb128".toCharArray());
        ArrayAddString(reint, "Xb256".toCharArray());
        ArrayAddString(reint, "Xu16".toCharArray());


        // No parameters
        Array noParameters = CreateArray();
        ArrayAddString(noParameters, "Loop".toCharArray());
        ArrayAddString(noParameters, "Endb".toCharArray());
        ArrayAddString(noParameters, "EndLoop".toCharArray());
        ArrayAddString(noParameters, "Else".toCharArray());

        /*

        Other:
        Acw
        Acr
        MulFull
        DivFull
        Call
        New
        Del
        Rdrand
        Set
        Movzx
        StringSubset
        StringSubset
        StringRangeCheck
        MatchString

        */

        if(StringIsInArray(ins.name, sameAsAssigneeNumber)) {
            valid = CheckSameTypeAsAssigneeWithNumbers(ins, message);
        }else if(StringIsInArray(ins.name, sameAsAssigneeBitfields)){
            valid = CheckSameTypeAsAssigneeWithBitfields(ins, message);
        }else if(StringIsInArray(ins.name, reint)){
            valid = CheckReinterpretations(ins, message);
        }else if(StringIsInArray(ins.name, conversion)){
            valid = CheckConversions(ins, message);
        }else if(StringIsInArray(ins.name, numbersToBits)){
            valid = CheckNumbersToBits(ins, message);
        }else if(StringIsInArray(ins.name, dataToBits)){
            valid = CheckDataToBits(ins, message);
        }else if(StringIsInArray(ins.name, bitfieldToNumber)){
            valid = CheckBitfieldToNumber(ins, message);
        }else if(StringIsInArray(ins.name, bitwiseAndNumberToBitwise)){
            valid = CheckBitwiseAndNumberToBitwise(ins, message);
        }else if(StringsEqual(ins.name, "Broadcast".toCharArray())){
            valid = CheckBroadcast(ins, message);
        }else if(StringsEqual(ins.name, "If".toCharArray())){
            valid = CheckIf(ins, message);
        }else if(StringsEqual(ins.name, "Idr".toCharArray())){
            valid = CheckIdr(ins, message);
        }else if(StringsEqual(ins.name, "Idro".toCharArray())){
            valid = CheckIdro(ins, message);
        }else if(StringsEqual(ins.name, "Idw".toCharArray())){
            valid = CheckIdw(ins, message);
        }else if(StringsEqual(ins.name, "Mov".toCharArray())){
            valid = CheckMov(ins, message);
        }else if(StringsEqual(ins.name, "FindSubstring".toCharArray())){
            valid = CheckFindSubstring(ins, message);
        }else if(StringIsInArray(ins.name, noParameters)){
            valid = true;
        }else if(StringsEqual(ins.name, "ExtractMask".toCharArray())){
            valid = CheckExtractMask(ins, message);
        }else if(StringsEqual(ins.name, "Shuffle".toCharArray())){
            valid = CheckShuffle(ins, message);
        }else if(StringsEqual(ins.name, "Acw".toCharArray())){
            valid = CheckAcw(ins, message);
        }else if(StringsEqual(ins.name, "Acr".toCharArray())){
            valid = CheckAcr(ins, message);
        }else if(StringsEqual(ins.name, "Len".toCharArray())){
            valid = CheckLen(ins, message);
        }else if(StringsEqual(ins.name, "Call".toCharArray())){
            valid = CheckCall(ins, message);
        }else{
            valid = false;
            message.string = ("Unknown typing rules for instruction: " + new String(ins.name)).toCharArray();
        }

        // Compute memory postfix.
        if(valid){
            ComputeMemoryPostfix(ins);
        }

        return valid;
    }

    private static boolean CheckBitwiseAndNumberToBitwise(Instruction ins, StringReference message) {
        boolean valid;
        char [] ntype;

        valid = true;

        // Parameters: bX <- bX, nX

        if(ParamIsVariable(ins.params[2])){
            ntype = ins.params[2].var.type;
        }else{
            ntype = "u64".toCharArray();
        }

        char[] btype = ins.params[0].var.type;

        char[] type = ConcatenateString(ins.params[0].var.type, ntype);
        ins.hasTypePostfix = true;
        ins.typePostfix = type;

        if(TypeIsBitfieldType(btype) && !TypeIsArrayType(btype)){
            if(StringsEqual(btype, ins.params[1].var.type)){
                if(TypeIsNumberType(ntype) && !TypeIsArrayType(ntype) && !TypeIsMultipleType(type)){
                    // OK
                }else{
                    valid = false;
                    message.string = ("Thirs parameter to operation must be number type: was " + new String(ins.params[1].var.type)).toCharArray();
                }
            }else{
                valid = false;
                message.string = ("Second parameter to operation must have the same type as the assigned type: was " + new String(ins.params[1].var.type)).toCharArray();
            }
        }else{
            valid = false;
            message.string = ("Operation must assign to non-array multiple bitfield type: was " + new String(type)).toCharArray();
        }

        return valid;
    }

    private static boolean CheckCall(Instruction ins, StringReference message) {
        boolean success;
        char [] fsname;

        success = true;

        // Check that structure is an instance of the function structure.
        Param param = ins.params[1];

        if(param.var.isStruct && !param.var.isStructArray) {

            fsname = ConcatenateString(ins.params[0].var.function.name, "S".toCharArray());
            if (StringsEqual(fsname, param.var.type)) {
                // OK
            } else {
                success = false;
                message.string = "The second parameter to Call must be an instance of the function structure of the function being called.".toCharArray();
            }
        }else{
            success = false;
            message.string = "The second parameter to Call must be a structure.".toCharArray();
        }

        return success;
    }

    private static boolean CheckLen(Instruction ins, StringReference message) {
        boolean valid;

        valid = true;

        // Parameters: number <- array

        char[] type = ins.params[1].var.type;
        ins.hasTypePostfix = true;
        ins.typePostfix = type;

        if(TypeIsArrayType(type)){
            char[] targetType = ins.params[0].var.type;
            if(TypeIsNumberType(targetType) && !TypeIsArrayType(targetType)){

            }else{
                valid = false;
                message.string = ("Len must assign to a number variable: was " + new String(targetType)).toCharArray();
            }
        }else{
            valid = false;
            message.string = ("Len takes array as input: was " + new String(type)).toCharArray();
        }

        return valid;
    }

    private static boolean CheckShuffle(Instruction ins, StringReference message) {
        boolean valid;

        valid = true;

        // Parameters: bXxY <- bXxY, uXxY

        char[] type = ins.params[0].var.type;
        ins.hasTypePostfix = true;
        ins.typePostfix = type;

        if(TypeIsMultipleType(type) && TypeIsBitfieldType(type) && !TypeIsArrayType(type)){
            if(StringsEqual(type, ins.params[1].var.type)){

            }else{
                valid = false;
                message.string = ("Second parameter to shuffle must have the same type as the assigned type: was " + new String(ins.params[1].var.type)).toCharArray();
            }
        }else{
            valid = false;
            message.string = ("Shuffle must assign to non-array multiple bitfield type: was " + new String(type)).toCharArray();
        }

        return valid;
    }

    private static boolean CheckExtractMask(Instruction ins, StringReference message) {
        boolean valid;

        valid = true;

        // Parameters: bX <- b8xX

        char[] type = ins.params[1].var.type;
        ins.hasTypePostfix = true;
        ins.typePostfix = type;

        StringReference[] parts = SplitByCharacter(type, 'x');

        if(TypeIsMultipleType(type) && TypeIsBitfieldType(type)){
            if(StringsEqual(parts[0].string, "b8".toCharArray())){
                if(StringsEqual(ins.params[0].var.type, ("b" + new String(parts[1].string)).toCharArray())){

                }else{
                    valid = false;
                    message.string = ("If input to Extract mask is b8xX, output must be bX, was " + new String(ins.params[0].var.type)).toCharArray();
                }
            }else{
                valid = false;
                message.string = ("Input to ExtractMask must be multiple b8 type: was " + new String(type)).toCharArray();
            }
        }else{
            valid = false;
            message.string = ("Input to ExtractMask must be multiple b8 type: was " + new String(type)).toCharArray();
        }

        return valid;
    }

    private static boolean CheckFindSubstring(Instruction ins, StringReference message) {
        boolean valid;
        char [] type;

        valid = true;

        // Parameters: b16, u8x16, number, u8x16, number

        if(ParamIsVariable(ins.params[2])){
            type = ins.params[2].var.type;
        }else if(ParamIsVariable(ins.params[4])){
            type = ins.params[4].var.type;
        }else{
            type = "u64".toCharArray();
        }

        ins.typePostfix = type;
        ins.hasTypePostfix = true;


        if(valid) {
            if (StringsEqual(ins.params[0].var.type, "b16".toCharArray())) {

            } else {
                valid = false;
                message.string = ("Assign variable for FindSubstring must be b16.").toCharArray();
            }
        }

        if(valid) {
            if (StringsEqual(ins.params[1].var.type, "u8x16".toCharArray()) && StringsEqual(ins.params[3].var.type, "u8x16".toCharArray())) {

            } else {
                valid = false;
                message.string = ("First and third input for FindSubstring must be u8x16.").toCharArray();
            }
        }

        if(valid) {
            if(ParamIsVariable(ins.params[2])){
                if (TypeIsNumberType(ins.params[2].type) && !TypeIsArrayType(ins.params[2].type)) {

                } else {
                    valid = false;
                    message.string = ("Third parameter must be a non-array number type.").toCharArray();
                }
            }
        }

        if(valid) {
            if(ParamIsVariable(ins.params[4])){
                if (TypeIsNumberType(ins.params[4].type) && !TypeIsArrayType(ins.params[4].type)) {

                } else {
                    valid = false;
                    message.string = ("Fourth parameter must be a non-array number type.").toCharArray();
                }
            }
        }

        return valid;
    }

    private static boolean CheckAcr(Instruction ins, StringReference message) {
        boolean success;
        char [] type1;

        success = true;

        // 0: Must be type x
        // 1: Must be struct
        // 2: Must be type x

        // Determine the type
        type1 = ins.params[0].var.type;

        ins.hasTypePostfix = true;
        ins.typePostfix = type1;

        if(StringsEqual(ins.params[2].var.type, type1)){
            // OK
        }else{
            success = false;
            message.string = ("Variable to assign from is not the same as the variable being assigned to: " + new String(ins.params[2].var.type)).toCharArray();
        }

        if(success){
            if(ins.params[1].var.isStruct){
                // OK
            }else{
                success = false;
                message.string = ("Second parameter to Acr must be struct: was " + new String(ins.params[1].var.type)).toCharArray();
            }
        }


        return success;
    }

    private static boolean CheckAcw(Instruction ins, StringReference message) {
        boolean success;
        char [] type1;

        success = true;

        // 0: Must be struct
        // 1: Must be type x
        // 2: Must be type x

        // Determine the type
        type1 = ins.params[1].var.type;

        ins.hasTypePostfix = true;
        ins.typePostfix = type1;

        if(StringsEqual(ins.params[2].var.type, type1)){
            // OK
        }else{
            success = false;
            message.string = ("Variable to assign from is not the same as the variable being assigned to: " + new String(ins.params[2].var.type)).toCharArray();
        }

        if(success){
            if(ins.params[0].var.isStruct){
                // OK
            }else{
                success = false;
                message.string = ("First parameter to Acw must be struct: was " + new String(ins.params[0].var.type)).toCharArray();
            }
        }

        return success;
    }

    private static boolean CheckMov(Instruction ins, StringReference message) {
        char[] type1, type2;
        boolean success;

        success = true;

        // Determine the type
        type1 = ins.params[0].var.type;

        ins.hasTypePostfix = true;
        ins.typePostfix = type1;

        if(ParamIsVariable(ins.params[1])){
            type2 = ins.params[1].var.type;

            if(StringsEqual(type1, type2)){
                // OK
            }else{
                success = false;
                message.string = ("Input parameter to Mov must be the same as the assigned variable: was " + new String(type1) + " and " +  new String(type2)).toCharArray();
            }
        }

        return success;
    }

    private static boolean CheckIdw(Instruction ins, StringReference message) {
        char[] type1, type2;
        boolean success;

        success = true;

        // Determine the type
        type1 = ins.params[0].var.type;

        if(ParamIsVariable(ins.params[1])){
            type2 = ins.params[1].var.type;
        }else{
            type2 = "s64".toCharArray();
        }

        ins.hasTypePostfix = true;
        ins.typePostfix = type1;

        if (TypeIsArrayType(type1)) {
            char[] elementType = Substring(type1, 0, type1.length - 1d);

            if(ParamIsVariable(ins.params[2])) {
                if (StringsEqual(ins.params[2].var.type, elementType)) {
                    if (TypeIsNumberType(type2) && !TypeIsArrayType(type2)) {
                        // OK
                    } else {
                        success = false;
                        message.string = "Index variable must be a number.".toCharArray();
                    }
                } else {
                    success = false;
                    message.string = "Input variable must be element type of array.".toCharArray();
                }
            }
        }else if (TypeIsMultipleType(type1)){
            StringReference[] parts = SplitByCharacter(type1, 'x');
            char[] elementType = parts[0].string;

            if(ParamIsVariable(ins.params[2])) {
                if (StringsEqual(ins.params[2].var.type, elementType)) {
                    if (TypeIsNumberType(type2) && !TypeIsArrayType(type2)) {
                        // OK
                    } else {
                        success = false;
                        message.string = "Index variable must be a number.".toCharArray();
                    }
                } else {
                    success = false;
                    message.string = "Input variable must be element type of multiple type.".toCharArray();
                }
            }
        }else{
            success = false;
            message.string = "First parameter to Idw must be an array.".toCharArray();
        }

        return success;
    }

    private static boolean CheckIdro(Instruction ins, StringReference message) {
        char[] type1, type2;
        boolean success;

        success = true;

        // Determine the type
        type1 = ins.params[1].var.type;

        if(ParamIsVariable(ins.params[2])){
            type2 = ins.params[2].var.type;
        }else{
            type2 = "s64".toCharArray();
        }

        if (TypeIsArrayType(type1)){
            ins.hasTypePostfix = true;
            ins.typePostfix = type1;

            char[] elementType = Substring(type1, 0, type1.length - 1d);

            if(StringsEqual(ins.params[0].var.type, elementType)){
                if(TypeIsNumberType(type2) && !TypeIsArrayType(type2)){
                    if(ParamIsLiteral(ins.params[3])){
                        if(IsValidNumber(ins.params[3].literal)){
                            // OK
                        }else{
                            success = false;
                            message.string = "Offset parameter must be a number.".toCharArray();
                        }
                    }else{
                        success = false;
                        message.string = "Offset parameter must be literal.".toCharArray();
                    }
                }else{
                    success = false;
                    message.string = "Index variable must be a number.".toCharArray();
                }
            }else{
                success = false;
                message.string = ("Assigned variable must be element type of array (1): was " + new String(elementType)).toCharArray();
            }
        }else{
            success = false;
            message.string = "Second parameter to Idro must be array.".toCharArray();
        }

        return success;
    }

    private static boolean CheckIdr(Instruction ins, StringReference message) {
        char[] type1, type2;
        boolean success;

        success = true;

        // Determine the type
        type1 = ins.params[1].var.type;

        if(ParamIsVariable(ins.params[2])){
            type2 = ins.params[2].var.type;
        }else{
            type2 = "s64".toCharArray();
        }

        ins.hasTypePostfix = true;
        if(ins.params[1].var.isStructArray){
            ins.typePostfix = "sta".toCharArray();
        }else{
            ins.typePostfix = type1;
        }


        if(TypeIsNumberType(type2) && !TypeIsArrayType(type2)){
            // OK
        }else{
            success = false;
            message.string = "Index variable must be a number.".toCharArray();
        }

        if(success) {

            if (TypeIsArrayType(type1)) {
                char[] elementType = Substring(type1, 0, type1.length - 1d);

                if (StringsEqual(ins.params[0].var.type, elementType)) {
                    // OK
                } else {
                    success = false;
                    message.string = ("Assigned variable must be element type of array or multi (2): was " + new String(elementType)).toCharArray();
                }
            } else if (TypeIsMultipleType(type1)) {
                StringReference[] parts = SplitByCharacter(type1, 'x');
                char[] elementType = parts[0].string;

                if (ParamIsVariable(ins.params[2])) {
                    if (StringsEqual(ins.params[2].var.type, elementType)) {
                        // OK
                    } else {
                        success = false;
                        message.string = "Input variable must be element type of multiple type.".toCharArray();
                    }
                }
            } else if (ins.params[1].var.isStructArray){
                char[] elementType = type1;

                if (StringsEqual(ins.params[0].var.type, elementType)) {
                    // OK
                } else {
                    success = false;
                    message.string = ("Assigned variable must be element type of array or multi (2): was " + new String(elementType)).toCharArray();
                }
            } else {
                success = false;
                message.string = "Second parameter to Idr must be array.".toCharArray();
            }
        }

        return success;
    }

    private static boolean CheckIf(Instruction ins, StringReference message) {
        char[] type1;
        boolean success;

        success = true;

        // Determine the type
        if (ParamIsVariable(ins.params[0])) {
            type1 = ins.params[0].var.type;
        }else{
            type1 = "b1".toCharArray();
        }

        if (TypeIsBitfieldType(type1) && !TypeIsArrayType(type1)){
            ins.hasTypePostfix = true;
            ins.typePostfix = type1;
        }else{
            success = false;
            message.string = "Variable for if must be bitfield type.".toCharArray();
        }

        return success;
    }

    private static boolean CheckBroadcast(Instruction ins, StringReference message) {
        char[] type1;
        boolean success;

        success = true;

        // Determine the type
        type1 = ins.params[0].var.type;

        if (TypeIsMultipleType(type1)){
            ins.hasTypePostfix = true;
            ins.typePostfix = type1;

            StringReference[] parts = SplitByCharacter(type1, 'x');

            if(ParamIsVariable(ins.params[1])){
                if(StringsEqual(ins.params[1].var.type, parts[0].string)){

                }else{
                    success = false;
                    message.string = "Input variable in broadcast must be same as element type of assigned variable.".toCharArray();
                }
            }
        }else{
            success = false;
            message.string = "Assigned variable must be a multi data type.".toCharArray();
        }

        return success;
    }

    private static boolean TypeIsMultipleType(char[] type) {
        return ContainsCharacter(type, 'x');
    }

    private static boolean CheckBitfieldToNumber(Instruction ins, StringReference message) {
        char[] type1, type2;
        boolean success;

        success = true;

        // Determine the type
        type1 = ins.params[0].var.type;

        //System.out.println(type1);

        if(ParamIsVariable(ins.params[1])){
            type2 = ins.params[1].var.type;
        }else{
            // If input types is a literal, use b64 as it is the native non-simd bitfield.
            type2 = "b64".toCharArray();
        }

        ins.typePostfix = (new String(type1) + new String(type2)).toCharArray();
        ins.hasTypePostfix = true;

        if (TypeIsNumberType(type1) && !TypeIsArrayType(type1)) {
            if (TypeIsBitfieldType(type2) && !TypeIsArrayType(type2)) {
                // OK
            } else {
                success = false;
                message.string = ("Instruction only works on non-array bitfield variables as input: " + new String(ins.name)).toCharArray();
            }
        } else {
            success = false;
            message.string = "Assigned variable must be non-array number type.".toCharArray();
        }

        return success;
    }

    private static boolean CheckNumbersToBits(Instruction ins, StringReference message) {
        char[] type1, type2;
        boolean success;

        success = true;

        // Determine the type
        type1 = ins.params[0].var.type;

        //System.out.println(type1);

        if(ParamIsVariable(ins.params[1])){
            type2 = ins.params[1].var.type;
        }else if(ParamIsVariable(ins.params[2])){
            type2 = ins.params[2].var.type;
        }else{
            // If both input types are literals, then we use the largest number type available.
            type2 = "f64".toCharArray();
        }

        ins.typePostfix = (new String(type1) + new String(type2)).toCharArray();
        ins.hasTypePostfix = true;

        if (TypeIsBitfieldType(type1)) {
            if (TypeIsNumberType(type2)) {
                if (!TypeIsArrayType(type2)) {

                } else {
                    success = false;
                    message.string = "Instruction does not work on arrays as input.".toCharArray();
                }
            } else {
                success = false;
                message.string = ("Instruction only works on number variables as input: " + new String(ins.name)).toCharArray();
            }
        } else {
            success = false;
            message.string = ("Assigned variable must be bitfield type: " + new String(ins.params[0].varname)).toCharArray();
        }

        // Check that all input parameters are the correct type.
        if(ParamIsVariable(ins.params[1])){
            if(StringsEqual(ins.params[1].var.type, type2)){
                // OK
            } else {
                success = false;
                message.string = "Input type not the correct type.".toCharArray();
            }
        }

        if(ParamIsVariable(ins.params[2])){
            if(StringsEqual(ins.params[2].var.type, type2)){
                // OK
            } else {
                success = false;
                message.string = "Input type not the correct type.".toCharArray();
            }
        }

        return success;
    }

    private static boolean CheckDataToBits(Instruction ins, StringReference message) {
        char[] type1, type2;
        boolean success;

        success = true;

        // Determine the type
        type1 = ins.params[0].var.type;

        //System.out.println(type1);

        if(ParamIsVariable(ins.params[1])){
            type2 = ins.params[1].var.type;
        }else if(ParamIsVariable(ins.params[2])){
            type2 = ins.params[2].var.type;
        }else{
            // TODO: What to do her? Try to detect literal type?
            type2 = "f64".toCharArray();
        }

        ins.typePostfix = (new String(type1) + new String(type2)).toCharArray();
        ins.hasTypePostfix = true;

        if (TypeIsBitfieldType(type1)) {

        } else {
            success = false;
            message.string = ("Assigned variable must be bitfield type: " + new String(ins.params[0].varname)).toCharArray();
        }

        // Check that all input parameters are the correct type.
        if(ParamIsVariable(ins.params[1])){
            if(StringsEqual(ins.params[1].var.type, type2)){
                // OK
            } else {
                success = false;
                message.string = "Input type not the correct type.".toCharArray();
            }
        }

        if(ParamIsVariable(ins.params[2])){
            if(StringsEqual(ins.params[2].var.type, type2)){
                // OK
            } else {
                success = false;
                message.string = "Input type not the correct type.".toCharArray();
            }
        }

        return success;
    }

    private static boolean CheckConversions(Instruction ins, StringReference message) {
        boolean success;
        StringReference[] types;

        // Compute type
        types = SplitByString(ins.name, "to".toCharArray());

        // Check that the parameters are of the correct types.
        if(StringsEqual(ins.params[0].var.type, types[1].string)){
            if(StringsEqual(ins.params[1].var.type, types[0].string)){
                success = true;
            }else{
                success = false;
                message.string = ("Input variable is of wrong type: " + new String(ins.params[1].var.type) + " must be " + new String(types[0].string)).toCharArray();
            }
        }else{
            success = false;
            message.string = ("Variable assigned to is of wrong type: is " + new String(ins.params[0].var.type) + " must be " + new String(types[1].string)).toCharArray();
        }

        return success;
    }

    private static boolean CheckReinterpretations(Instruction ins, StringReference message) {
        char[] type;
        Param paramType;
        boolean success;

        // Compute type
        paramType = ins.params[1];
        type = paramType.var.type;
        ins.typePostfix = type;
        ins.hasTypePostfix = true;

        // Check that the first parameter is of the correct type.
        type = Substring(ins.name, 1, ins.name.length);
        if(StringsEqual(ins.params[0].var.type, type)){
            success = true;
        }else{
            success = false;
            message.string = ("Variable assigned to is of wrong type: " + new String(ins.params[0].var.type)).toCharArray();
        }

        return success;
    }

    private static boolean CheckSameTypeAsAssigneeWithBitfields(Instruction ins, StringReference message) {
        char[] targetType;
        Param assigneeType;
        boolean success;
        double i;

        success = true;

        // Compute type
        assigneeType = ins.params[0];
        targetType = assigneeType.var.type;
        ins.typePostfix = targetType;
        ins.hasTypePostfix = true;

        // Check that the type is a bitfield type
        if(TypeIsBitfieldType(targetType)){
            if(!TypeIsArrayType(targetType)){

            }else{
                success = false;
                message.string = "Instruction does not work on arrays.".toCharArray();
            }
        }else{
            success = false;
            message.string = ("Instruction only works on bitfield variables: " + new String(ins.name)).toCharArray();
        }

        // Check that all parameters are the correct type.
        if(success) {
            for (i = 0; i < ins.params.length && success; i = i + 1d) {
                Param p = ins.params[(int) i];
                if (ParamIsVariable(p)) {
                    if (StringsEqual(p.var.type, ins.typePostfix)) {
                        // OK
                    } else {
                        success = false;
                        message.string = ("Parameter is not the correct type: " + new String(p.varname)).toCharArray();
                    }
                }
            }
        }

        return success;
    }

    private static boolean CheckSameTypeAsAssigneeWithNumbers(Instruction ins, StringReference message) {
        double i;
        char[] targetType;
        Param assigneeType;
        boolean success;

        success = true;

        // Determine the type
        assigneeType = ins.params[0];
        targetType = assigneeType.var.type;
        ins.typePostfix = targetType;
        ins.hasTypePostfix = true;

        // Check type.
        if (TypeIsNumberType(targetType)) {
            if (!TypeIsArrayType(targetType)) {

            } else {
                success = false;
                message.string = "Instruction does not work on arrays.".toCharArray();
            }
        } else {
            success = false;
            message.string = ("Instruction only works on number variables: " + new String(ins.name) + " got " + new String(targetType)).toCharArray();
        }

        // Check that all parameters are the correct type.
        if(success) {
            for (i = 0; i < ins.params.length && success; i = i + 1d) {
                Param p = ins.params[(int) i];
                if (ParamIsVariable(p)) {
                    if (StringsEqual(p.var.type, ins.typePostfix)) {
                        // OK
                    } else {
                        success = false;
                        message.string = ("Parameter is not the correct type: " + new String(p.varname)).toCharArray();
                    }
                }
            }
        }

        return success;
    }


    private static boolean TypeIsBitfieldType(char[] type) {
        char first = type[0];

        return first == 'b';
    }

    private static boolean TypeIsNumberType(char [] type) {
        char first = type[0];

        return first == 's' || first == 'u' || first == 'f';
    }

    private static boolean TypeIsArrayType(char [] type) {
        char last = type[type.length - 1];

        return last == 'a';
    }

    private static void ComputeMemoryPostfix(Instruction ins) {
        double i;
        char[] memoryPostfix;
        memoryPostfix = new char[0];

        if (ins.params.length >= 1) {
            memoryPostfix = new char[ins.params.length - 1];

            for (i = 0; i < ins.params.length - 1d; i = i + 1d) {
                if (StringsEqual(ins.params[(int) (i + 1)].type, "var".toCharArray())) {
                    memoryPostfix[(int) i] = 'm';
                } else {
                    memoryPostfix[(int) i] = 'i';
                }
            }
        }

        if(StringsEqual(ins.name, "If".toCharArray()) || StringsEqual(ins.name, "Inc".toCharArray()) || StringsEqual(ins.name, "Dec".toCharArray())){
            memoryPostfix = new char[ins.params.length];

            for (i = 0; i < ins.params.length; i = i + 1d) {
                if (StringsEqual(ins.params[(int)i].type, "var".toCharArray())) {
                    memoryPostfix[(int) i] = 'm';
                } else {
                    memoryPostfix[(int) i] = 'i';
                }
            }
        }

        if(StringsEqual(ins.name, "Call".toCharArray())){
            memoryPostfix = "".toCharArray();
        }

        ins.memoryPostfix = memoryPostfix;
    }

    public static boolean ParamIsVariable(Param param) {
        boolean isVar;

        isVar = StringsEqual(param.type, "var".toCharArray());

        return isVar;
    }

    public static boolean ParamIsLiteral(Param param) {
        boolean isLit;

        isLit = StringsEqual(param.type, "literal".toCharArray());

        return isLit;
    }
}
