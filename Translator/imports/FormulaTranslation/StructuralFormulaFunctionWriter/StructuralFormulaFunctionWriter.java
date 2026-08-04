package FormulaTranslation.StructuralFormulaFunctionWriter;

import static java.lang.Math.*;

import references.references.*;
import static references.references.references.*;

import static numbers.NumberToString.NumberToString.*;

import static numbers.NumberComputations.NumberComputations.*;

import static numbers.StringToNumber.StringToNumber.*;

import static charCharacters.Characters.Characters.*;

import static arrays.arrays.arrays.*;

import static strings.stream.stream.*;

import static strings.strings.strings.*;

import static lists.NumberList.NumberList.*;

import static lists.StringList.StringList.*;

import lists.DynamicArrayCharacters.Structures.*;

import static lists.DynamicArrayCharacters.DynamicArrayCharactersFunctions.DynamicArrayCharactersFunctions.*;

import static lists.BooleanList.BooleanList.*;

import lists.LinkedListStrings.Structures.*;

import static lists.LinkedListStrings.LinkedListStringsFunctions.LinkedListStringsFunctions.*;

import lists.LinkedListNumbers.Structures.*;

import static lists.LinkedListNumbers.LinkedListNumbersFunctions.LinkedListNumbersFunctions.*;

import static lists.LinkedListCharacters.LinkedListCharactersFunctions.LinkedListCharactersFunctions.*;

import lists.LinkedListCharacters.Structures.*;

import lists.DynamicArrayNumbers.Structures.*;

import static lists.DynamicArrayNumbers.DynamicArrayNumbersFunctions.DynamicArrayNumbersFunctions.*;

import static lists.CharacterList.CharacterList.*;

import static math.math.math.*;

import static math.Decimal15E2.Decimal15E2.*;


import static FormulaTranslation.BooleanFormula.BooleanFormula.*;

import static FormulaTranslation.ArithmeticFormulaPratt.ArithmeticFormulaPratt.*;

import static FormulaTranslation.StructuralFormula.StructuralFormula.*;

import static FormulaTranslation.ArithmeticFormula.ArithmeticFormula.*;

import static FormulaTranslation.BitwiseFormula.BitwiseFormula.*;

import static FormulaTranslation.ArithmeticFormulaFunctionWriter.ArithmeticFormulaFunctionWriter.*;

import static FormulaTranslation.TS.TS.*;

import static FormulaTranslation.ArithmeticFormulaEvaluator.ArithmeticFormulaEvaluator.*;

import static FormulaTranslation.StructuralFormulaSymbolicWriter.StructuralFormulaSymbolicWriter.*;

import static FormulaTranslation.BooleanFormulaSymbolicWriter.BooleanFormulaSymbolicWriter.*;

import static FormulaTranslation.BitwiseFormulaFunctionWriter.BitwiseFormulaFunctionWriter.*;

import FormulaTranslation.ASTNodes.*;
import static FormulaTranslation.ASTNodes.ASTNodes.*;

import static FormulaTranslation.ArithmeticFormulaSymbolicWriter.ArithmeticFormulaSymbolicWriter.*;

import static FormulaTranslation.BitwiseFormulaSymbolicWriter.BitwiseFormulaSymbolicWriter.*;

import static FormulaTranslation.BooleanFormulaFunctionWriter.BooleanFormulaFunctionWriter.*;

public class StructuralFormulaFunctionWriter{
	public static void StructuralASTToTFormFunctions(ASTNode ast, StringReference tf, char [] prefix, char [] postfix, char [] tprefix, boolean parenthesis, boolean semicolon, boolean wrappedNumber, char [] target){
		BooleanArrayReference t;
		NumberReference assignedT;

		t = CreateBooleanArrayReferenceLengthValue(0d, false);
		assignedT = CreateNumberReference(0d);
		tf.string = new char [0];

		StructuralASTToTFormFunctionsInner(ast, tf, t, assignedT, prefix, postfix, tprefix, parenthesis, semicolon, wrappedNumber, target, 0d);
	}

	public static void StructuralASTToTFormFunctionsInner(ASTNode ast, StringReference tf, BooleanArrayReference ts, NumberReference assignedT, char [] prefix, char [] postfix, char [] tprefix, boolean parenthesis, boolean semicolon, boolean wrappedNumber, char [] target, double level){
		double tl, tr, t;
		char [] functionName, value, numberString;

		tl = 0d;
		tr = 0d;

		if(!ast.leaf){
			if(StringsEqual(ast.value, ".".toCharArray())){
				if(!ast.l.leaf){
					StructuralASTToTFormFunctionsInner(ast.l, tf, ts, assignedT, prefix, postfix, tprefix, parenthesis, semicolon, wrappedNumber, target, level + 1d);
					tl = assignedT.numberValue;
				}

				if(!ast.r.leaf){
					StructuralASTToTFormFunctionsInner(ast.r, tf, ts, assignedT, prefix, postfix, tprefix, parenthesis, semicolon, wrappedNumber, target, level + 1d);
					tr = assignedT.numberValue;
				}

				if(!ast.l.leaf){
					FreeTVariable(ts, tl);
				}

				if(!ast.r.leaf){
					FreeTVariable(ts, tr);
				}

				functionName = StructuralBinarySymbolToFunctionName(ast.value);

				tf.string = AppendString(tf.string, prefix);
				tf.string = AppendString(tf.string, functionName);
				tf.string = AppendString(tf.string, postfix);
				if(parenthesis){
					tf.string = AppendString(tf.string, "(".toCharArray());
				}else{
					tf.string = AppendString(tf.string, " ".toCharArray());
				}

				if(level > 0d || target.length == 0d){
					t = AllocateTVariable(ts);
					assignedT.numberValue = t;
					tf.string = AppendString(tf.string, "t".toCharArray());
					tf.string = AppendString(tf.string, tprefix);
					numberString = CreateStringDecimalFromNumber(t);
					tf.string = AppendString(tf.string, numberString);
				}else{
					tf.string = AppendString(tf.string, target);
				}

				tf.string = AppendString(tf.string, ", ".toCharArray());

				if(ast.l.leaf){
					if(charIsNumber(ast.l.value[0])){
						value = new char [0];
						if(wrappedNumber){
							value = AppendString(value, prefix);
							value = AppendString(value, "Number".toCharArray());
							value = AppendString(value, postfix);
							value = AppendString(value, "(".toCharArray());
						}
						value = AppendString(value, ast.l.value);
						if(wrappedNumber){
							value = AppendString(value, ")".toCharArray());
						}
					}else{
						value = ast.l.value;
					}
					tf.string = AppendString(tf.string, value);
				}else{
					tf.string = AppendString(tf.string, "t".toCharArray());
					tf.string = AppendString(tf.string, tprefix);
					numberString = CreateStringDecimalFromNumber(tl);
					tf.string = AppendString(tf.string, numberString);
				}
				tf.string = AppendString(tf.string, ", ".toCharArray());
				if(ast.r.leaf){
					if(charIsNumber(ast.r.value[0])){
						value = new char [0];
						if(wrappedNumber){
							value = AppendString(value, prefix);
							value = AppendString(value, "Number".toCharArray());
							value = AppendString(value, postfix);
							value = AppendString(value, "(".toCharArray());
						}
						value = AppendString(value, ast.r.value);
						if(wrappedNumber){
							value = AppendString(value, ")".toCharArray());
						}
					}else{
						value = ast.r.value;
					}
					tf.string = AppendString(tf.string, value);
				}else{
					tf.string = AppendString(tf.string, "t".toCharArray());
					tf.string = AppendString(tf.string, tprefix);
					numberString = CreateStringDecimalFromNumber(tr);
					tf.string = AppendString(tf.string, numberString);
				}
				if(parenthesis){
					tf.string = AppendString(tf.string, ")".toCharArray());
				}else{
					tf.string = AppendString(tf.string, " ".toCharArray());
				}
				if(semicolon){
					tf.string = AppendString(tf.string, ";".toCharArray());
				}
				tf.string = AppendString(tf.string, "\n".toCharArray());
			}else if(StringsEqual(ast.value, "()".toCharArray())){
				StructuralASTToTFormFunctionsInner(ast.l, tf, ts, assignedT, prefix, postfix, tprefix, parenthesis, semicolon, wrappedNumber, target, level + 1d);
			}else if(StringsEqual(ast.value, "[]".toCharArray())){
				if(!ast.l.leaf){
					StructuralASTToTFormFunctionsInner(ast.l, tf, ts, assignedT, prefix, postfix, tprefix, parenthesis, semicolon, wrappedNumber, target, level + 1d);
					tl = assignedT.numberValue;
				}

				if(!ast.r.leaf){
					StructuralASTToTFormFunctionsInner(ast.r, tf, ts, assignedT, prefix, postfix, tprefix, parenthesis, semicolon, wrappedNumber, target, level + 1d);
					tr = assignedT.numberValue;
				}

				if(!ast.l.leaf){
					FreeTVariable(ts, tl);
				}

				if(!ast.r.leaf){
					FreeTVariable(ts, tr);
				}

				tf.string = AppendString(tf.string, prefix);
				tf.string = AppendString(tf.string, "Idr".toCharArray());
				tf.string = AppendString(tf.string, postfix);
				if(parenthesis){
					tf.string = AppendString(tf.string, "(".toCharArray());
				}else{
					tf.string = AppendString(tf.string, " ".toCharArray());
				}

				if(level > 0d || target.length == 0d){
					t = AllocateTVariable(ts);
					assignedT.numberValue = t;
					tf.string = AppendString(tf.string, "t".toCharArray());
					tf.string = AppendString(tf.string, tprefix);
					numberString = CreateStringDecimalFromNumber(t);
					tf.string = AppendString(tf.string, numberString);
				}else{
					tf.string = AppendString(tf.string, target);
				}

				tf.string = AppendString(tf.string, ", ".toCharArray());

				if(ast.l.leaf){
					if(charIsNumber(ast.l.value[0])){
						value = new char [0];
						if(wrappedNumber){
							value = AppendString(value, prefix);
							value = AppendString(value, "Number".toCharArray());
							value = AppendString(value, postfix);
							value = AppendString(value, "(".toCharArray());
						}
						value = AppendString(value, ast.l.value);
						if(wrappedNumber){
							value = AppendString(value, ")".toCharArray());
						}
					}else{
						value = ast.l.value;
					}
					tf.string = AppendString(tf.string, value);
				}else{
					tf.string = AppendString(tf.string, "t".toCharArray());
					tf.string = AppendString(tf.string, tprefix);
					numberString = CreateStringDecimalFromNumber(tl);
					tf.string = AppendString(tf.string, numberString);
				}
				tf.string = AppendString(tf.string, ", ".toCharArray());
				if(ast.r.leaf){
					if(charIsNumber(ast.r.value[0])){
						value = new char [0];
						if(wrappedNumber){
							value = AppendString(value, prefix);
							value = AppendString(value, "Number".toCharArray());
							value = AppendString(value, postfix);
							value = AppendString(value, "(".toCharArray());
						}
						value = AppendString(value, ast.r.value);
						if(wrappedNumber){
							value = AppendString(value, ")".toCharArray());
						}
					}else{
						value = ast.r.value;
					}
					tf.string = AppendString(tf.string, value);
				}else{
					tf.string = AppendString(tf.string, "t".toCharArray());
					tf.string = AppendString(tf.string, tprefix);
					numberString = CreateStringDecimalFromNumber(tr);
					tf.string = AppendString(tf.string, numberString);
				}
				if(parenthesis){
					tf.string = AppendString(tf.string, ")".toCharArray());
				}else{
					tf.string = AppendString(tf.string, " ".toCharArray());
				}
				if(semicolon){
					tf.string = AppendString(tf.string, ";".toCharArray());
				}
				tf.string = AppendString(tf.string, "\n".toCharArray());
			}else if(IsKnownStructuralFunction(ast.value)){
				if(!ast.l.leaf){
					StructuralASTToTFormFunctionsInner(ast.l, tf, ts, assignedT, prefix, postfix, tprefix, parenthesis, semicolon, wrappedNumber, target, level + 1d);
					tl = assignedT.numberValue;
					FreeTVariable(ts, tl);
				}

				functionName = CopyString(ast.value);
				functionName[0] = charToUpperCase(functionName[0]);

				tf.string = AppendString(tf.string, prefix);
				tf.string = AppendString(tf.string, functionName);
				tf.string = AppendString(tf.string, postfix);
				if(parenthesis){
					tf.string = AppendString(tf.string, "(".toCharArray());
				}else{
					tf.string = AppendString(tf.string, " ".toCharArray());
				}

				if(level > 0d || target.length == 0d){
					t = AllocateTVariable(ts);
					assignedT.numberValue = t;
					tf.string = AppendString(tf.string, "t".toCharArray());
					tf.string = AppendString(tf.string, tprefix);
					numberString = CreateStringDecimalFromNumber(t);
					tf.string = AppendString(tf.string, numberString);
				}else{
					tf.string = AppendString(tf.string, target);
				}

				tf.string = AppendString(tf.string, ", ".toCharArray());

				if(ast.l.leaf){
					tf.string = AppendString(tf.string, ast.l.value);
				}else{
					tf.string = AppendString(tf.string, "t".toCharArray());
					tf.string = AppendString(tf.string, tprefix);
					numberString = CreateStringDecimalFromNumber(tl);
					tf.string = AppendString(tf.string, numberString);
				}
				if(parenthesis){
					tf.string = AppendString(tf.string, ")".toCharArray());
				}else{
					tf.string = AppendString(tf.string, " ".toCharArray());
				}
				if(semicolon){
					tf.string = AppendString(tf.string, ";".toCharArray());
				}
				tf.string = AppendString(tf.string, "\n".toCharArray());
			}else{
				tf.string = AppendString(tf.string, "<failed>".toCharArray());
			}
		}else{
			tf.string = AppendString(tf.string, "Mov".toCharArray());
			tf.string = AppendString(tf.string, " ".toCharArray());
			tf.string = AppendString(tf.string, target);
			tf.string = AppendString(tf.string, ",".toCharArray());
			tf.string = AppendString(tf.string, " ".toCharArray());
			tf.string = AppendString(tf.string, ast.value);
		}
	}

	public static char [] StructuralBinarySymbolToFunctionName(char [] value){
		char [] f;

		f = "Unknown".toCharArray();

		if(StringsEqual(value, ".".toCharArray())){
			f = "Acr".toCharArray();
		}
		if(StringsEqual(value, "[]".toCharArray())){
			f = "Idr".toCharArray();
		}

		return f;
	}

	public static boolean StructuralFormulaToTFormFunctions(char [] f, char [] prefix, char [] postfix, char [] tprefix, boolean parenthesis, boolean semicolon, boolean wrappedNumber, char [] target, StringReference result, StringReference message){
		StringArrayReference tokens;
		boolean success;
		ASTNode ast;
		NumberReference pos;

		tokens = new StringArrayReference();
		success = TokenizeStructuralFormula(f, tokens, message);

		if(success){
			/* Parse*/
			ast = new ASTNode();
			success = ParseStructuralTokens(tokens.stringArray, ast, message);

			if(success){
				StructuralASTToTFormFunctions(ast, result, prefix, postfix, tprefix, parenthesis, semicolon, wrappedNumber, target);
			}
		}

		return success;
	}

  public static void delete(Object object){
    // Java has garbage collection.
  }
}
