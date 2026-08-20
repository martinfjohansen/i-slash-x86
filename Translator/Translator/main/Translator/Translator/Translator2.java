package Translator.Translator;

import DataStructures.Array.Structures.Array;
import DataStructures.Array.Structures.DataReference;
import lists.LinkedListCharacters.Structures.LinkedListCharacters;
import references.references.NumberReference;
import references.references.StringReference;

import java.math.BigInteger;

import static DataStructures.Array.Arrays.Arrays.*;
import static DataStructures.Array.Structures.Structures.CreateArrayData;
import static arrays.arrays.arrays.StringsEqual;
import static charCharacters.Characters.Characters.charIsLetter;
import static charCharacters.Characters.Characters.charIsNumber;
import static lists.LinkedListCharacters.LinkedListCharactersFunctions.LinkedListCharactersFunctions.*;
import static numbers.StringToNumber.StringToNumber.CreateNumberFromDecimalStringWithCheck;
import static references.references.references.CreateNumberReference;
import static strings.strings.strings.StartsWith;
import static strings.strings.strings.Substring;

public class Translator2 {
    public static boolean Tokenize(char[] input, DataReference tokensRef, StringReference message) {
        Array tokens;
        double i, state, part;
        char c;
        LinkedListCharacters ll;
        char [] str;
        boolean success, escape;

        success = true;
        message.string = "".toCharArray();

        ll = CreateLinkedListCharacter();

        tokens = CreateArray();

        state = 0d;
        part = 0d;
        escape = false;
        for(i = 0; i < input.length + 1; i = i + 1d){
            if(i < input.length) {
                c = input[(int) i];
            }else{
                c = '\n';
            }

            if(state == 0d) {
                if (c == ' ' || c == '\t') {
                    // skip
                }else if(c == '\n'){
                    ArrayAddString(tokens, "<newline>".toCharArray());
                    //System.out.println("<newline>");
                }else if(charIsLetter(c)){
                    state = 1d;
                    ll = CreateLinkedListCharacter();
                }else if(charIsNumber(c) || c == '-'){
                    state = 2d;
                    ll = CreateLinkedListCharacter();
                }else if(c == ';'){
                    state = 3d;
                }else if(c == '\''){
                    state = 4d;
                    part = 0d;
                    ll = CreateLinkedListCharacter();
                }else if(c == ','){
                    ArrayAddString(tokens, "<comma>".toCharArray());
                    //System.out.println("<comma>");
                }else{
                    success = false;
                    message.string = ("Character not allowed in token (1): " + c).toCharArray();
                }
            }

            // Identifier
            if(state == 1d){
                if(charIsLetter(c)){
                    LinkedListAddCharacter(ll, c);
                }else if(charIsNumber(c)){
                    LinkedListAddCharacter(ll, c);
                }else if(c == '_'){
                    LinkedListAddCharacter(ll, c);
                }else if(c == '\n' || c == ' ' || c == '\t' || c == ','){
                    // Done
                    str = LinkedListCharactersToArray(ll);
                    ArrayAddString(tokens, str);
                    //System.out.println(new String(str));
                    state = 0d;

                    i = i - 1d;
                }else{
                    message.string = ("Character not allowed in token (2): " + c).toCharArray();
                    success = false;
                }
            }

            // Literal
            if(state == 2d){
                if(charIsNumber(c)){
                    LinkedListAddCharacter(ll, c);
                }else if(c == '.'){
                    LinkedListAddCharacter(ll, c);
                }else if(c == 'e'){
                    LinkedListAddCharacter(ll, c);
                }else if(c == '-'){
                    LinkedListAddCharacter(ll, c);
                }else if(c == 'x'){
                    LinkedListAddCharacter(ll, c);
                }else if(c == 'b'){
                    LinkedListAddCharacter(ll, c);
                }else if(c == 'a' || c == 'A' || c == 'b' || c == 'B' || c == 'c' || c == 'C' || c == 'd' || c == 'D' || c == 'e' || c == 'E' || c == 'f' || c == 'F'){
                    LinkedListAddCharacter(ll, c);
                }else if(c == '\n' || c == ' ' || c == '\t' || c == ','){
                    // Done
                    str = LinkedListCharactersToArray(ll);
                    ArrayAddString(tokens, str);
                    //System.out.println(new String(str));
                    state = 0d;

                    i = i - 1d;
                }else{
                    message.string = ("Character not allowed in token (3): " + c).toCharArray();
                    success = false;
                }
            }

            // Comment
            if(state == 3d){
                if(c == '\n'){
                    state = 0d;
                }else{
                    // skip
                }
            }

            // Character literal
            if(state == 4d){
                if(part == 0d){
                    if(c == '\''){
                        LinkedListAddCharacter(ll, c);
                        part = part + 1d;
                    }
                }else if(part == 1d){
                    LinkedListAddCharacter(ll, c);
                    part = part + 1d;
                    if(c == '\\'){
                        escape = true;
                    }
                }else if(part == 2d && escape){
                    LinkedListAddCharacter(ll, c);
                    part = part + 1d;
                    escape = false;
                }else if(part == 2d || part == 3d){
                    if(c == '\''){
                        LinkedListAddCharacter(ll, c);
                        part = part + 1d;
                        state = 0d;

                        str = LinkedListCharactersToArray(ll);
                        ArrayAddString(tokens, str);
                    }else{
                        message.string = ("Character literal must end with ': " + c).toCharArray();
                        success = false;
                    }
                }
            }
        }

        tokensRef.data = CreateArrayData(tokens);
        return success;
    }

    public static Array GetTypeInstructionTokens() {
        Array ins;

        ins = CreateArray();

        // Naming rules: numbers: u, s, f. bitfield: b. Followed by size. Array: a-ending. Multitypes has x followed by multiple.

        // Types
        ArrayAddString(ins, "u8".toCharArray());
        ArrayAddString(ins, "u16".toCharArray());
        ArrayAddString(ins, "u32".toCharArray());
        ArrayAddString(ins, "u64".toCharArray());
        ArrayAddString(ins, "u128".toCharArray());

        ArrayAddString(ins, "s8".toCharArray());
        ArrayAddString(ins, "s16".toCharArray());
        ArrayAddString(ins, "s32".toCharArray());
        ArrayAddString(ins, "s64".toCharArray());
        ArrayAddString(ins, "s128".toCharArray());

        ArrayAddString(ins, "b1".toCharArray());
        ArrayAddString(ins, "b8".toCharArray());
        ArrayAddString(ins, "b16".toCharArray());
        ArrayAddString(ins, "b32".toCharArray());
        ArrayAddString(ins, "b64".toCharArray());
        ArrayAddString(ins, "b128".toCharArray());
        ArrayAddString(ins, "b256".toCharArray());
        ArrayAddString(ins, "b512".toCharArray());

        ArrayAddString(ins, "m8".toCharArray());
        ArrayAddString(ins, "m16".toCharArray());
        ArrayAddString(ins, "m32".toCharArray());
        ArrayAddString(ins, "m64".toCharArray());

        ArrayAddString(ins, "f16".toCharArray());
        ArrayAddString(ins, "f32".toCharArray());
        ArrayAddString(ins, "f64".toCharArray());
        ArrayAddString(ins, "f80".toCharArray());

        ArrayAddString(ins, "u8a".toCharArray());
        ArrayAddString(ins, "u16a".toCharArray());
        ArrayAddString(ins, "u32a".toCharArray());
        ArrayAddString(ins, "u64a".toCharArray());
        ArrayAddString(ins, "u128a".toCharArray());

        ArrayAddString(ins, "s8a".toCharArray());
        ArrayAddString(ins, "s16a".toCharArray());
        ArrayAddString(ins, "s32a".toCharArray());
        ArrayAddString(ins, "s64a".toCharArray());
        ArrayAddString(ins, "s128a".toCharArray());

        ArrayAddString(ins, "b1a".toCharArray());
        ArrayAddString(ins, "b8a".toCharArray());
        ArrayAddString(ins, "b16a".toCharArray());
        ArrayAddString(ins, "b32a".toCharArray());
        ArrayAddString(ins, "b64a".toCharArray());
        ArrayAddString(ins, "b128a".toCharArray());
        ArrayAddString(ins, "b256a".toCharArray());
        ArrayAddString(ins, "b512a".toCharArray());

        ArrayAddString(ins, "f16a".toCharArray());
        ArrayAddString(ins, "f32a".toCharArray());
        ArrayAddString(ins, "f64a".toCharArray());
        ArrayAddString(ins, "f80a".toCharArray());

        ArrayAddString(ins, "u8x8".toCharArray());
        ArrayAddString(ins, "u8x16".toCharArray());
        ArrayAddString(ins, "u8x32".toCharArray());
        ArrayAddString(ins, "u8x64".toCharArray());
        ArrayAddString(ins, "u8x8a".toCharArray());
        ArrayAddString(ins, "u8x16a".toCharArray());
        ArrayAddString(ins, "u8x32a".toCharArray());
        ArrayAddString(ins, "u8x64a".toCharArray());

        ArrayAddString(ins, "u16x4".toCharArray());
        ArrayAddString(ins, "u16x8".toCharArray());
        ArrayAddString(ins, "u16x16".toCharArray());
        ArrayAddString(ins, "u16x32".toCharArray());
        ArrayAddString(ins, "u16x4a".toCharArray());
        ArrayAddString(ins, "u16x8a".toCharArray());
        ArrayAddString(ins, "u16x16a".toCharArray());
        ArrayAddString(ins, "u16x32a".toCharArray());

        ArrayAddString(ins, "u32x2".toCharArray());
        ArrayAddString(ins, "u32x4".toCharArray());
        ArrayAddString(ins, "u32x8".toCharArray());
        ArrayAddString(ins, "u32x16".toCharArray());
        ArrayAddString(ins, "u32x2a".toCharArray());
        ArrayAddString(ins, "u32x4a".toCharArray());
        ArrayAddString(ins, "u32x8a".toCharArray());
        ArrayAddString(ins, "u32x16a".toCharArray());

        ArrayAddString(ins, "u64x2".toCharArray());
        ArrayAddString(ins, "u64x4".toCharArray());
        ArrayAddString(ins, "u64x8".toCharArray());
        ArrayAddString(ins, "u64x2a".toCharArray());
        ArrayAddString(ins, "u64x4a".toCharArray());
        ArrayAddString(ins, "u64x8a".toCharArray());

        ArrayAddString(ins, "s8x8".toCharArray());
        ArrayAddString(ins, "s8x16".toCharArray());
        ArrayAddString(ins, "s8x32".toCharArray());
        ArrayAddString(ins, "s8x64".toCharArray());
        ArrayAddString(ins, "s8x8a".toCharArray());
        ArrayAddString(ins, "s8x16a".toCharArray());
        ArrayAddString(ins, "s8x32a".toCharArray());
        ArrayAddString(ins, "s8x64a".toCharArray());

        ArrayAddString(ins, "s16x4".toCharArray());
        ArrayAddString(ins, "s16x8".toCharArray());
        ArrayAddString(ins, "s16x16".toCharArray());
        ArrayAddString(ins, "s16x32".toCharArray());
        ArrayAddString(ins, "s16x4a".toCharArray());
        ArrayAddString(ins, "s16x8a".toCharArray());
        ArrayAddString(ins, "s16x16a".toCharArray());
        ArrayAddString(ins, "s16x32a".toCharArray());

        ArrayAddString(ins, "s32x2".toCharArray());
        ArrayAddString(ins, "s32x4".toCharArray());
        ArrayAddString(ins, "s32x8".toCharArray());
        ArrayAddString(ins, "s32x16".toCharArray());
        ArrayAddString(ins, "s32x2a".toCharArray());
        ArrayAddString(ins, "s32x4a".toCharArray());
        ArrayAddString(ins, "s32x8a".toCharArray());
        ArrayAddString(ins, "s32x16a".toCharArray());

        ArrayAddString(ins, "s64x2".toCharArray());
        ArrayAddString(ins, "s64x4".toCharArray());
        ArrayAddString(ins, "s64x8".toCharArray());
        ArrayAddString(ins, "s64x2a".toCharArray());
        ArrayAddString(ins, "s64x4a".toCharArray());
        ArrayAddString(ins, "s64x8a".toCharArray());

        ArrayAddString(ins, "b8x8".toCharArray());
        ArrayAddString(ins, "b8x16".toCharArray());
        ArrayAddString(ins, "b8x32".toCharArray());
        ArrayAddString(ins, "b8x64".toCharArray());
        ArrayAddString(ins, "b8x8a".toCharArray());
        ArrayAddString(ins, "b8x16a".toCharArray());
        ArrayAddString(ins, "b8x32a".toCharArray());
        ArrayAddString(ins, "b8x64a".toCharArray());

        ArrayAddString(ins, "b16x4".toCharArray());
        ArrayAddString(ins, "b16x8".toCharArray());
        ArrayAddString(ins, "b16x16".toCharArray());
        ArrayAddString(ins, "b16x32".toCharArray());
        ArrayAddString(ins, "b16x4a".toCharArray());
        ArrayAddString(ins, "b16x8a".toCharArray());
        ArrayAddString(ins, "b16x16a".toCharArray());
        ArrayAddString(ins, "b16x32a".toCharArray());

        ArrayAddString(ins, "b32x2".toCharArray());
        ArrayAddString(ins, "b32x4".toCharArray());
        ArrayAddString(ins, "b32x8".toCharArray());
        ArrayAddString(ins, "b32x16".toCharArray());
        ArrayAddString(ins, "b32x2a".toCharArray());
        ArrayAddString(ins, "b32x4a".toCharArray());
        ArrayAddString(ins, "b32x8a".toCharArray());
        ArrayAddString(ins, "b32x16a".toCharArray());

        ArrayAddString(ins, "b64x2".toCharArray());
        ArrayAddString(ins, "b64x4".toCharArray());
        ArrayAddString(ins, "b64x8".toCharArray());
        ArrayAddString(ins, "b64x2a".toCharArray());
        ArrayAddString(ins, "b64x4a".toCharArray());
        ArrayAddString(ins, "b64x8a".toCharArray());

        ArrayAddString(ins, "f16x8".toCharArray());
        ArrayAddString(ins, "f16x16".toCharArray());
        ArrayAddString(ins, "f16x32".toCharArray());

        ArrayAddString(ins, "f32x4".toCharArray());
        ArrayAddString(ins, "f32x8".toCharArray());
        ArrayAddString(ins, "f32x16".toCharArray());
        ArrayAddString(ins, "f32x4a".toCharArray());
        ArrayAddString(ins, "f32x8a".toCharArray());
        ArrayAddString(ins, "f32x16a".toCharArray());

        ArrayAddString(ins, "f64x2".toCharArray());
        ArrayAddString(ins, "f64x4".toCharArray());
        ArrayAddString(ins, "f64x8".toCharArray());
        ArrayAddString(ins, "f64x2a".toCharArray());
        ArrayAddString(ins, "f64x4a".toCharArray());
        ArrayAddString(ins, "f64x8a".toCharArray());

        return ins;
    }

    public static Array GetInstructionTokens() {
        Array ins;

        ins = CreateArray();

        // Basics
        // ArrayAddString(ins, "Bgs".toCharArray());
        // ArrayAddString(ins, "Ens".toCharArray());
        // ArrayAddString(ins, "Fnc".toCharArray());
        // ArrayAddString(ins, "Ret".toCharArray());

        // Types
        // Computations
        ArrayAddString(ins, "Mov".toCharArray());
        ArrayAddString(ins, "Movx8".toCharArray());
        ArrayAddString(ins, "Broadcast".toCharArray());
        ArrayAddString(ins, "InterleaveLow".toCharArray());
        ArrayAddString(ins, "InterleaveHigh".toCharArray());

        ArrayAddString(ins, "Add".toCharArray());
        ArrayAddString(ins, "Sub".toCharArray());
        ArrayAddString(ins, "Mul".toCharArray());
        ArrayAddString(ins, "Div".toCharArray());
        ArrayAddString(ins, "Mod".toCharArray());
        ArrayAddString(ins, "DivMod".toCharArray());
        ArrayAddString(ins, "MulDiv".toCharArray());
        ArrayAddString(ins, "MulFull".toCharArray());
        ArrayAddString(ins, "Inc".toCharArray());
        ArrayAddString(ins, "Dec".toCharArray());
        ArrayAddString(ins, "Round".toCharArray());
        ArrayAddString(ins, "Floor".toCharArray());
        ArrayAddString(ins, "Ceil".toCharArray());
        ArrayAddString(ins, "Truncate".toCharArray());
        ArrayAddString(ins, "Abs".toCharArray());
        ArrayAddString(ins, "Not".toCharArray());
        ArrayAddString(ins, "Shl".toCharArray());
        ArrayAddString(ins, "Shr".toCharArray());
        ArrayAddString(ins, "Sar".toCharArray());
        ArrayAddString(ins, "ShiftLeft".toCharArray());
        ArrayAddString(ins, "ShiftRight".toCharArray());
        ArrayAddString(ins, "ShiftArithmeticRight".toCharArray());
        ArrayAddString(ins, "And".toCharArray());
        ArrayAddString(ins, "Andnot".toCharArray());
        ArrayAddString(ins, "Or".toCharArray());
        ArrayAddString(ins, "Xor".toCharArray());
        ArrayAddString(ins, "Ror".toCharArray());
        ArrayAddString(ins, "Rol".toCharArray());
        ArrayAddString(ins, "Bswap".toCharArray());
        ArrayAddString(ins, "Bsf".toCharArray());
        ArrayAddString(ins, "Bsr".toCharArray());
        ArrayAddString(ins, "Bt".toCharArray());
        ArrayAddString(ins, "IsZero".toCharArray());
        ArrayAddString(ins, "IsNonZero".toCharArray());

        ArrayAddString(ins, "Lt".toCharArray());
        ArrayAddString(ins, "Lte".toCharArray());
        ArrayAddString(ins, "LessThanOrEqual".toCharArray());
        ArrayAddString(ins, "LessThan".toCharArray());
        ArrayAddString(ins, "MoreThanOrEqual".toCharArray());
        ArrayAddString(ins, "MoreThan".toCharArray());
        ArrayAddString(ins, "Gt".toCharArray());
        ArrayAddString(ins, "Gte".toCharArray());
        ArrayAddString(ins, "MoreThanOrEqual".toCharArray());
        ArrayAddString(ins, "Eq".toCharArray());
        ArrayAddString(ins, "Equal".toCharArray());
        ArrayAddString(ins, "Neq".toCharArray());
        ArrayAddString(ins, "Unequal".toCharArray());

        ArrayAddString(ins, "Rdrand".toCharArray());

        ArrayAddString(ins, "New".toCharArray());
        ArrayAddString(ins, "Del".toCharArray());

        ArrayAddString(ins, "s8tos16".toCharArray());
        ArrayAddString(ins, "s16tof32".toCharArray());
        ArrayAddString(ins, "f32tof64".toCharArray());
        ArrayAddString(ins, "s16tos8".toCharArray());
        ArrayAddString(ins, "f32tos16".toCharArray());
        ArrayAddString(ins, "f64tof32".toCharArray());
        ArrayAddString(ins, "u8tou16".toCharArray());
        ArrayAddString(ins, "u8tou32".toCharArray());
        ArrayAddString(ins, "u8x8tou16x8".toCharArray());
        ArrayAddString(ins, "u8x4tou32x4".toCharArray());
        ArrayAddString(ins, "u16tou32".toCharArray());
        ArrayAddString(ins, "u32tou64".toCharArray());
        ArrayAddString(ins, "f64x2tof32x2".toCharArray());
        ArrayAddString(ins, "f64x4tof32x4".toCharArray());
        ArrayAddString(ins, "f32x4tof16x4".toCharArray());
        ArrayAddString(ins, "f32x8tof16x8".toCharArray());
        ArrayAddString(ins, "f32x4tof64x4".toCharArray());
        ArrayAddString(ins, "u32tou16s".toCharArray());
        ArrayAddString(ins, "s8tos16".toCharArray());
        ArrayAddString(ins, "s8tos32".toCharArray());
        ArrayAddString(ins, "s8tos64".toCharArray());
        ArrayAddString(ins, "s16tos32".toCharArray());
        ArrayAddString(ins, "s16tos64".toCharArray());
        ArrayAddString(ins, "s32tos64".toCharArray());
        ArrayAddString(ins, "u8tou16".toCharArray());
        ArrayAddString(ins, "u8tou32".toCharArray());
        ArrayAddString(ins, "u8tou64".toCharArray());
        ArrayAddString(ins, "u16tou32".toCharArray());
        ArrayAddString(ins, "u16tou64".toCharArray());
        ArrayAddString(ins, "u32tou64".toCharArray());
        ArrayAddString(ins, "s16tos8".toCharArray());
        ArrayAddString(ins, "s32tos16".toCharArray());
        ArrayAddString(ins, "u16tou8".toCharArray());
        ArrayAddString(ins, "u32tou16".toCharArray());
        ArrayAddString(ins, "u64tou32".toCharArray());
        ArrayAddString(ins, "s64tos32".toCharArray());
        ArrayAddString(ins, "s8tou8".toCharArray());
        ArrayAddString(ins, "s16tou16".toCharArray());
        ArrayAddString(ins, "s32tou32".toCharArray());
        ArrayAddString(ins, "s64tou64".toCharArray());
        ArrayAddString(ins, "u8tos8".toCharArray());
        ArrayAddString(ins, "u16tos16".toCharArray());
        ArrayAddString(ins, "u32tos32".toCharArray());
        ArrayAddString(ins, "u64tos64".toCharArray());
        ArrayAddString(ins, "u8tos16".toCharArray());
        ArrayAddString(ins, "u8tos32".toCharArray());
        ArrayAddString(ins, "u16tos32".toCharArray());
        ArrayAddString(ins, "u32tos64".toCharArray());
        ArrayAddString(ins, "f32tof64".toCharArray());
        ArrayAddString(ins, "f64tof32".toCharArray());
        ArrayAddString(ins, "f32tou32".toCharArray());
        ArrayAddString(ins, "f32tos32".toCharArray());
        ArrayAddString(ins, "f32tou64".toCharArray());
        ArrayAddString(ins, "f32tos64".toCharArray());
        ArrayAddString(ins, "f64tou64".toCharArray());
        ArrayAddString(ins, "f64tos64".toCharArray());
        ArrayAddString(ins, "s32tof32".toCharArray());
        ArrayAddString(ins, "s32tof64".toCharArray());
        ArrayAddString(ins, "s64tof32".toCharArray());
        ArrayAddString(ins, "s64tof64".toCharArray());


        ArrayAddString(ins, "Xu32x4".toCharArray());
        ArrayAddString(ins, "Xb8".toCharArray());
        ArrayAddString(ins, "Xb16".toCharArray());
        ArrayAddString(ins, "Xb32".toCharArray());
        ArrayAddString(ins, "Xb64".toCharArray());
        ArrayAddString(ins, "Xu8".toCharArray());
        ArrayAddString(ins, "Xu16".toCharArray());
        ArrayAddString(ins, "Xu32".toCharArray());
        ArrayAddString(ins, "Xu64".toCharArray());
        ArrayAddString(ins, "Xb128".toCharArray());
        ArrayAddString(ins, "Xb256".toCharArray());
        ArrayAddString(ins, "Xu16x8a".toCharArray());
        ArrayAddString(ins, "Xu8x16a".toCharArray());
        ArrayAddString(ins, "Xb8x16a".toCharArray());
        ArrayAddString(ins, "Xb8x32a".toCharArray());
        ArrayAddString(ins, "Xu8x16".toCharArray());
        ArrayAddString(ins, "Xb8x16".toCharArray());
        ArrayAddString(ins, "Xb32x8".toCharArray());
        ArrayAddString(ins, "Xb16x8".toCharArray());
        ArrayAddString(ins, "Xu16x16a".toCharArray());
        ArrayAddString(ins, "Xu16x32a".toCharArray());
        ArrayAddString(ins, "Xb8x32".toCharArray());
        ArrayAddString(ins, "Xb8x64".toCharArray());

        ArrayAddString(ins, "Cmov".toCharArray());
        ArrayAddString(ins, "Set".toCharArray());
        ArrayAddString(ins, "Xchg".toCharArray());
        ArrayAddString(ins, "Xadd".toCharArray());
        ArrayAddString(ins, "CmpXchg".toCharArray());
        ArrayAddString(ins, "Crc32".toCharArray());

        ArrayAddString(ins, "Movs".toCharArray());
        ArrayAddString(ins, "Cmps".toCharArray());
        ArrayAddString(ins, "Scas".toCharArray());
        ArrayAddString(ins, "Popcnt".toCharArray());
        ArrayAddString(ins, "Lzcnt".toCharArray());
        ArrayAddString(ins, "Tzcnt".toCharArray());
        ArrayAddString(ins, "Rdtsc".toCharArray());
        ArrayAddString(ins, "Nop".toCharArray());
        ArrayAddString(ins, "Rdpmc".toCharArray());

        ArrayAddString(ins, "Bextr".toCharArray());
        ArrayAddString(ins, "Blsi".toCharArray());
        ArrayAddString(ins, "Blsmsk".toCharArray());
        ArrayAddString(ins, "Blsr".toCharArray());
        ArrayAddString(ins, "Bzhi".toCharArray());
        ArrayAddString(ins, "Pdep".toCharArray());
        ArrayAddString(ins, "Pext".toCharArray());
        ArrayAddString(ins, "ExtractMask".toCharArray());
        ArrayAddString(ins, "Shuffle".toCharArray());
        ArrayAddString(ins, "MulStoreHigh".toCharArray());
        ArrayAddString(ins, "AddSaturated".toCharArray());
        ArrayAddString(ins, "SubSaturated".toCharArray());
        ArrayAddString(ins, "MultiplyAndAdd".toCharArray());
        ArrayAddString(ins, "Avg".toCharArray());
        ArrayAddString(ins, "Max".toCharArray());
        ArrayAddString(ins, "Min".toCharArray());
        ArrayAddString(ins, "Sqrt".toCharArray());
        ArrayAddString(ins, "ApproxReciprocal".toCharArray());
        ArrayAddString(ins, "ApproxReciprocalSqrt".toCharArray());
        ArrayAddString(ins, "InterleaveLow".toCharArray());
        ArrayAddString(ins, "InterleaveHigh".toCharArray());
        ArrayAddString(ins, "SelectiveCopy".toCharArray());
        ArrayAddString(ins, "CombineExtract".toCharArray());
        ArrayAddString(ins, "SumAbsoluteDifference".toCharArray());
        ArrayAddString(ins, "PairwiseAdd".toCharArray());
        ArrayAddString(ins, "PairwiseSub".toCharArray());
        ArrayAddString(ins, "PairwiseMulAdd".toCharArray());
        ArrayAddString(ins, "AlternatingSubAdd".toCharArray());
        ArrayAddString(ins, "ConditionalNegate".toCharArray());
        ArrayAddString(ins, "DotProduct".toCharArray());
        ArrayAddString(ins, "Gather".toCharArray());
        ArrayAddString(ins, "MovAndDuplicate".toCharArray());
        ArrayAddString(ins, "MovAndDuplicateOdd".toCharArray());

        ArrayAddString(ins, "StringSubset".toCharArray());
        ArrayAddString(ins, "StringRangeCheck".toCharArray());
        ArrayAddString(ins, "MatchString".toCharArray());
        ArrayAddString(ins, "FindSubstring".toCharArray());

        // Control
        ArrayAddString(ins, "If".toCharArray());
        ArrayAddString(ins, "Loop".toCharArray());
        ArrayAddString(ins, "EndLoop".toCharArray());
        ArrayAddString(ins, "Else".toCharArray());
        ArrayAddString(ins, "Endb".toCharArray());
        ArrayAddString(ins, "Call".toCharArray());

        // Structural
        ArrayAddString(ins, "Idr".toCharArray());
        ArrayAddString(ins, "Idw".toCharArray());
        ArrayAddString(ins, "Idro".toCharArray());
        ArrayAddString(ins, "Len".toCharArray());
        ArrayAddString(ins, "Acw".toCharArray());
        ArrayAddString(ins, "Acr".toCharArray());

        return ins;
    }

    public static boolean Parse(Array tokens, Ast ast, StringReference message) {
        Array ins;
        boolean success;
        double functions, structures, i, st, fnc;
        char [] token;
        NumberReference tokenRef;
        Struct struct;
        Function function;

        success = true;

        ins = GetInstructionTokens();

        // Count structures and functions
        functions = 0d;
        structures = 0d;
        for(i = 0d; i < ArrayLength(tokens); i = i + 1d){
            token = ArrayIndexString(tokens, i);
            if(StringsEqual(token, "Bgs".toCharArray())){
                structures = structures + 1d;
            }
            if(StringsEqual(token, "Fnc".toCharArray())){
                functions = functions + 1d;
            }
        }

        ast.fnc = new Function[(int)functions];
        ast.st = new Struct[(int)structures];

        // Parse functions and structures
        st = 0d;
        fnc = 0d;
        tokenRef = CreateNumberReference(0d);
        for(; tokenRef.numberValue < ArrayLength(tokens) && success; ){
            token = GetNextToken(tokens, tokenRef);
            if(StringsEqual(token, "Bgs".toCharArray())){
                struct = new Struct();
                ast.st[(int)st] = struct;

                success = ParseStructure(struct, tokens, tokenRef, message);

                st = st + 1d;
            }else if(StringsEqual(token, "Fnc".toCharArray())){
                function = new Function();
                ast.fnc[(int)fnc] = function;

                success = ParseFunction(function, tokens, tokenRef, message);

                fnc = fnc + 1d;
            }else if(StringsEqual(token, "<newline>".toCharArray())){
                // OK
            }else{
                success = false;
                message.string = ("Unexpected token outside functions and structures: " + new String(token)).toCharArray();
            }
        }

        return success;
    }

    public static boolean ParseStructure(Struct struct, Array tokens, NumberReference tokenRef, StringReference message) {
        boolean success, done, found;
        char [] token, type, name;
        Array typeInstructions;
        double typeDecs, typeDec, startOfTypeDecs, i;

        typeInstructions = GetTypeInstructionTokens();

        token = GetNextToken(tokens, tokenRef);

        success = IsValidIdentifier(token, message);
        if(success){
            struct.name = token;

            //System.out.println("Bgs " + new String(struct.name));

            token = GetNextToken(tokens, tokenRef);

            success = IsNewline(token, message);

            if(success) {
                // Check that there is an Ens.
                found = false;
                for (i = tokenRef.numberValue; i < ArrayLength(tokens) && !found; i = i + 1d) {
                    token = ArrayIndexString(tokens, i);

                    if (StringsEqual(token, "Ens".toCharArray())) {
                        found = true;
                    }
                }

                if (found) {
                    // Count type declarations
                    done = false;
                    typeDecs = 0d;
                    startOfTypeDecs = tokenRef.numberValue;

                    for (; !done && success; ) {
                        token = GetNextToken(tokens, tokenRef);

                        if (StringsEqual(token, "Ens".toCharArray())) {
                            done = true;
                        } else if (IsNewline(token, message)) {
                            // Skip
                        } else {
                            if (StringIsInArray(token, typeInstructions)) {
                                token = GetNextToken(tokens, tokenRef);

                                success = IsValidIdentifier(token, message);

                                if (success) {
                                    token = GetNextToken(tokens, tokenRef);

                                    success = IsNewline(token, message);

                                    if (success) {
                                        typeDecs = typeDecs + 1d;
                                    }
                                }
                            }else if (StringsEqual(token, "str".toCharArray()) || StringsEqual(token, "sta".toCharArray())) {
                                token = GetNextToken(tokens, tokenRef);

                                success = IsValidIdentifier(token, message);

                                if (success) {
                                    token = GetNextToken(tokens, tokenRef);

                                    success = IsValidIdentifier(token, message);

                                    if (success) {
                                        token = GetNextToken(tokens, tokenRef);

                                        success = IsNewline(token, message);

                                        if (success) {
                                            typeDecs = typeDecs + 1d;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Create vars
                    struct.vars = new Var[(int) typeDecs];

                    done = false;
                    typeDec = 0d;
                    tokenRef.numberValue = startOfTypeDecs;

                    for (; !done && success; ) {
                        token = GetNextToken(tokens, tokenRef);

                        if (StringsEqual(token, "Ens".toCharArray())) {
                            done = true;

                            //System.out.println("Ens");

                            token = GetNextToken(tokens, tokenRef);
                        } else if (StringIsInArray(token, typeInstructions)) {
                            type = token;
                            token = GetNextToken(tokens, tokenRef);

                            success = IsValidIdentifier(token, message);

                            if (success) {
                                name = token;
                                token = GetNextToken(tokens, tokenRef);

                                success = IsNewline(token, message);

                                if (success) {
                                    struct.vars[(int) typeDec] = CreateTypeDec(type, name, false, false);

                                    //System.out.println("\t" + new String(type) + " " + new String(name));

                                    typeDec = typeDec + 1d;
                                }
                            }
                        } else if (StringsEqual(token, "str".toCharArray()) || StringsEqual(token, "sta".toCharArray())) {
                            boolean isStructArray = StringsEqual(token, "sta".toCharArray());

                            token = GetNextToken(tokens, tokenRef);

                            success = IsValidIdentifier(token, message);

                            if (success) {
                                type = token;
                                token = GetNextToken(tokens, tokenRef);

                                success = IsValidIdentifier(token, message);

                                if (success) {
                                    name = token;

                                    token = GetNextToken(tokens, tokenRef);

                                    success = IsNewline(token, message);

                                    if (success) {

                                        struct.vars[(int) typeDec] = CreateTypeDec(type, name, true, isStructArray);

                                        //System.out.println("\t" + new String(type) + " " + new String(name));

                                        typeDec = typeDec + 1d;
                                    }
                                }
                            }
                        } else {
                            success = false;
                            message.string = ("Token not a type instruction: " + new String(token)).toCharArray();
                        }
                    }

                } else {
                    success = false;
                    message.string = "Structure must end with Ens-instruction.".toCharArray();
                }
            }
        }

        return success;
    }

    private static Var CreateTypeDec(char[] type, char[] name, boolean isStruct, boolean isStructArray) {
        Var var = new Var();

        var.type = type;
        var.name = name;
        var.isStruct = isStruct;
        var.isStructArray = isStructArray;

        return var;
    }

    public static boolean StringIsInArray(char[] token, Array typeInstructions) {
        boolean isIn;
        double i;
        char [] cToken;

        isIn = false;

        for(i = 0d; i < ArrayLength(typeInstructions) && !isIn; i = i + 1d){
            cToken = ArrayIndexString(typeInstructions, i);

            if(StringsEqual(token, cToken)){
                isIn = true;
            }
        }

        return isIn;
    }

    private static boolean IsNewline(char[] token, StringReference message) {
        boolean valid;

        valid = StringsEqual(token, "<newline>".toCharArray());

        if(valid){

        }else{
            message.string = "Newline expected.".toCharArray();
        }

        return valid;
    }

    public static char[] GetNextToken(Array tokens, NumberReference tokenRef) {
        char [] token;

        token = ArrayIndexString(tokens, tokenRef.numberValue);

        Inc(tokenRef);

        return token;
    }

    public static void Inc(NumberReference tokenRef) {
        tokenRef.numberValue = tokenRef.numberValue + 1d;
    }

    public static boolean IsValidIdentifier(char[] token, StringReference message) {
        boolean valid;
        double i;
        char c;

        valid = token.length > 0;

        if(valid) {
            c = token[0];
            valid = charIsLetter(c);

            for (i = 0d; i < token.length && valid; i = i + 1d) {
                c = token[(int) i];
                if (charIsLetter(c) || charIsNumber(c) || c == '_') {
                    // OK
                }else{
                    valid = false;
                    message.string = ("Invalid identifier: " + new String(token)).toCharArray();
                }
            }
        }

        return valid;
    }

    public static boolean ParseFunction(Function function, Array tokens, NumberReference tokenRef, StringReference message) {
        boolean success, found, done;
        char [] token;
        double i, state, insCount;
        Array ins;

        ins = GetInstructionTokens();

        token = GetNextToken(tokens, tokenRef);

        success = IsValidIdentifier(token, message);
        if(success) {
            function.name = token;

            //System.out.println("Fnc " + new String(function.name));

            token = GetNextToken(tokens, tokenRef);

            success = IsNewline(token, message);

            if(success) {
                // Find Ret
                found = false;
                for (i = tokenRef.numberValue; i < ArrayLength(tokens) && !found; i = i + 1d) {
                    token = ArrayIndexString(tokens, i);

                    if (StringsEqual(token, "Ret".toCharArray())) {
                        found = true;
                    }
                }

                if (found) {

                    // Count instructions
                    state = 0d;
                    insCount = 0d;
                    done = false;
                    for(i = tokenRef.numberValue; i < ArrayLength(tokens) && !done; i = i + 1d){
                        token = ArrayIndexString(tokens, i);

                        if(state == 0d) {
                            if(StringsEqual(token, "Ret".toCharArray())){
                                done = true;
                            }else if (StringIsInArray(token, ins)) {
                                insCount = insCount + 1d;
                                state = 1d;
                            }
                        }else if(state == 1d){
                            if (StringsEqual(token, "<newline>".toCharArray())) {
                                state = 0d;
                            }else{
                                // Skip
                            }
                        }
                    }

                    // Create instructions
                    function.ins = new Instruction[(int)insCount];
                    insCount = 0d;
                    done = false;
                    for(; !done && success;  ){
                        token = GetNextToken(tokens, tokenRef);

                        if (StringIsInArray(token, ins)) {
                            Instruction instruction = new Instruction();
                            success = ParseInstruction(instruction, token, tokens, tokenRef, message);

                            if(success){
                                function.ins[(int)insCount] = instruction;
                                insCount = insCount + 1d;
                            }
                        }else if(StringsEqual(token, "Ret".toCharArray())) {
                            //System.out.println("Ret");

                            done = true;
                        }else if(IsNewline(token, message)) {
                            // Skip
                        }else{
                            success = false;
                            message.string = ("Instruction not one of the valid instructions: " + new String(token)).toCharArray();
                        }
                    }

                    if(success) {
                        if (insCount == function.ins.length) {

                        } else {
                            success = false;
                            message.string = ("Parsed instructions was not the expected count: for " + new String(function.name) + " excpected " + (int) function.ins.length + " found " + (int) insCount).toCharArray();
                        }
                    }

                } else {
                    success = false;
                    message.string = "Function must end with Ret.".toCharArray();
                }
            }
        }

        return success;
    }

    public static boolean ParseInstruction(Instruction instruction, char[] name, Array tokens, NumberReference tokenRef, StringReference message) {
        boolean success, done;
        char [] token;
        double i, param;
        Param p;

        success = true;

        instruction.name = name;

        //System.out.print("\t" + new String(name) + " ");

        // Count parameters
        done = false;
        param = 0d;
        for(i = tokenRef.numberValue; i < ArrayLength(tokens) && !done; i = i + 1d){
            token = ArrayIndexString(tokens, i);

            if(StringsEqual(token, "<comma>".toCharArray())){

            }else if(StringsEqual(token, "<newline>".toCharArray())){
                done = true;
            }else{
                param = param + 1d;
            }
        }

        // Create parameters
        instruction.params = new Param[(int)param];

        done = false;
        param = 0d;
        for(; !done && success; ){
            token = GetNextToken(tokens, tokenRef);

            if(StringsEqual(token, "<newline>".toCharArray())){
                done = true;
            }else{
                p = new Param();

                success = CreateParameter(p, token, message);

                instruction.params[(int)param] = p;

                param = param + 1d;

                token = GetNextToken(tokens, tokenRef);

                if(IsNewline(token, message)){
                    done = true;
                }else if(IsComma(token, message)){
                    //System.out.print(", ");
                }else{
                    success = false;
                    message.string = "Expected newline or comma.".toCharArray();
                }
            }
        }

        //System.out.print("\n");

        return success;
    }

    private static boolean IsComma(char[] token, StringReference message) {
        boolean valid;

        valid = StringsEqual(token, "<comma>".toCharArray());

        if(valid){

        }else{
            message.string = "Comma expected.".toCharArray();
        }

        return valid;
    }

    public static boolean CreateParameter(Param p, char[] token, StringReference message) {
        boolean success;

        success = true;

        if(IsValidLiteral(token, message)){
            p.type = "literal".toCharArray();
            p.literal = token;
            p.immediateType = "?".toCharArray();
        }else if(IsValidIdentifier(token, message)){
            p.type = "var".toCharArray();
            p.varname = token;
        }else{
            success = false;
            message.string = ("Parameter is neither variable nor literal: " + new String(token)).toCharArray();
        }

        //System.out.print(token);

        return success;
    }

    public static boolean IsValidLiteral(char[] token, StringReference message) {
        boolean valid;

        if(IsValidNumber(token)){
            valid = true;
        }else if(IsValidCharacter(token)){
            valid = true;
        }else if(IsValidBoolean(token)){
            valid = true;
        }else{
            valid = false;
        }

        return valid;
    }

    private static boolean IsValidBoolean(char[] token) {
        boolean valid;

        if(StringsEqual(token, "true".toCharArray())){
            valid = true;
        }else if(StringsEqual(token, "false".toCharArray())){
            valid = true;
        }else{
            valid = false;
        }

        return valid;
    }

    public static boolean IsValidCharacter(char[] token) {
        boolean valid;

        valid = true;

        // TODO: Expand this to more valid types.
        if(token.length == 3d){
            if(token[0] == '\'' && token[2] == '\''){

            }else{
                valid = false;
            }
        }else{
            valid = false;
        }


        return valid;
    }

    public static boolean IsValidNumber(char[] token) {
        NumberReference numRef = new NumberReference();
        boolean valid;
        StringReference message = new StringReference();

        // Decimal
        valid = CreateNumberFromDecimalStringWithCheck(token, numRef, message);

        if(valid){

        }else{
            // Hex
            if(StartsWith(token, "0x".toCharArray())) {
                try {
                    Long.parseUnsignedLong(new String(Substring(token, 2, token.length)), 16);
                    valid = true;
                }catch (NumberFormatException e){
                    valid = false;
                }
            }
        }

        return valid;
    }


    public static boolean PrettyPrint(Ast ast, StringReference message) {
        return true;
    }
}











































