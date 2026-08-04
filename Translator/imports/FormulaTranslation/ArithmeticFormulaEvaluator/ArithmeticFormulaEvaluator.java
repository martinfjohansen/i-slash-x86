package FormulaTranslation.ArithmeticFormulaEvaluator;

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

import static FormulaTranslation.StructuralFormulaFunctionWriter.StructuralFormulaFunctionWriter.*;

import static FormulaTranslation.TS.TS.*;

import static FormulaTranslation.StructuralFormulaSymbolicWriter.StructuralFormulaSymbolicWriter.*;

import static FormulaTranslation.BooleanFormulaSymbolicWriter.BooleanFormulaSymbolicWriter.*;

import static FormulaTranslation.BitwiseFormulaFunctionWriter.BitwiseFormulaFunctionWriter.*;

import FormulaTranslation.ASTNodes.*;
import static FormulaTranslation.ASTNodes.ASTNodes.*;

import static FormulaTranslation.ArithmeticFormulaSymbolicWriter.ArithmeticFormulaSymbolicWriter.*;

import static FormulaTranslation.BitwiseFormulaSymbolicWriter.BitwiseFormulaSymbolicWriter.*;

import static FormulaTranslation.BooleanFormulaFunctionWriter.BooleanFormulaFunctionWriter.*;

public class ArithmeticFormulaEvaluator{
	public static boolean Evaluate(char [] formula, NumberReference result, StringReference message){
		boolean success;
		StringArrayReference tokens;
		ASTNode ast;

		tokens = new StringArrayReference();
		success = TokenizeArithmeticFormula(formula, tokens, message);

		if(success){
			/* Parse*/
			ast = new ASTNode();
			success = ParseArithmeticTokens(tokens.stringArray, ast, message);

			if(success){
				success = EvaluateAST(ast, result, message);
			}
		}

		return success;
	}

	public static boolean EvaluateAST(ASTNode ast, NumberReference result, StringReference message){
		boolean success, isNumber, binary, unary;
		NumberReference left, right;

		left = new NumberReference();
		right = new NumberReference();

		success = true;

		if(ast.leaf){
			isNumber = CreateNumberFromStringWithCheck(ast.value, 10d, result, message);
			if(isNumber){
				/* Done*/
				success = true;
			}else{

				if(StringsEqual(ast.value, "pi".toCharArray())){
					result.numberValue = PI;
				}else if(StringsEqual(ast.value, "e".toCharArray())){
					result.numberValue = E;
				}else if(StringsEqual(ast.value, "g".toCharArray())){
					result.numberValue = 0.57721566490153286060651209008240243;
				}else if(StringsEqual(ast.value, "o".toCharArray())){
					result.numberValue = 0.56714329040978387299996866221035554;
				}else{
					/* Variable, TODO*/
					success = false;
				}
			}
		}else{
			binary = false;
			unary = false;

			if(StringsEqual(ast.value, "+".toCharArray())){
				binary = true;
			}else if(StringsEqual(ast.value, "-".toCharArray())){
				binary = true;
			}else if(StringsEqual(ast.value, "*".toCharArray())){
				binary = true;
			}else if(StringsEqual(ast.value, "/".toCharArray())){
				binary = true;
			}else if(StringsEqual(ast.value, "^".toCharArray())){
				binary = true;
			}else if(StringsEqual(ast.value, "()".toCharArray())){
				unary = true;
			}else if(StringsEqual(ast.value, "sin".toCharArray())){
				unary = true;
			}else if(StringsEqual(ast.value, "cos".toCharArray())){
				unary = true;
			}else if(StringsEqual(ast.value, "tan".toCharArray())){
				unary = true;
			}else if(StringsEqual(ast.value, "asin".toCharArray())){
				unary = true;
			}else if(StringsEqual(ast.value, "acos".toCharArray())){
				unary = true;
			}else if(StringsEqual(ast.value, "atan".toCharArray())){
				unary = true;
			}else if(StringsEqual(ast.value, "log".toCharArray())){
				unary = true;
			}else if(StringsEqual(ast.value, "ln".toCharArray())){
				unary = true;
			}else if(StringsEqual(ast.value, "exp".toCharArray())){
				unary = true;
			}else if(StringsEqual(ast.value, "sqrt".toCharArray())){
				unary = true;
			}else if(StringsEqual(ast.value, "cbrt".toCharArray())){
				unary = true;
			}else if(StringsEqual(ast.value, "gamma".toCharArray())){
				unary = true;
			}else if(StringsEqual(ast.value, "unary-".toCharArray())){
				unary = true;
			}else if(StringsEqual(ast.value, "unary+".toCharArray())){
				unary = true;
			}else{
				success = false;
			}

			if(success){
				if(unary){
					success = EvaluateAST(ast.l, left, message);
					if(success){
						if(StringsEqual(ast.value, "()".toCharArray())){
							result.numberValue = left.numberValue;
						}else if(StringsEqual(ast.value, "sin".toCharArray())){
							result.numberValue = sin(left.numberValue);
						}else if(StringsEqual(ast.value, "cos".toCharArray())){
							result.numberValue = cos(left.numberValue);
						}else if(StringsEqual(ast.value, "tan".toCharArray())){
							result.numberValue = tan(left.numberValue);
						}else if(StringsEqual(ast.value, "asin".toCharArray())){
							result.numberValue = asin(left.numberValue);
						}else if(StringsEqual(ast.value, "acos".toCharArray())){
							result.numberValue = acos(left.numberValue);
						}else if(StringsEqual(ast.value, "atan".toCharArray())){
							result.numberValue = atan(left.numberValue);
						}else if(StringsEqual(ast.value, "log".toCharArray())){
							result.numberValue = log10(left.numberValue);
						}else if(StringsEqual(ast.value, "ln".toCharArray())){
							result.numberValue = log(left.numberValue);
						}else if(StringsEqual(ast.value, "exp".toCharArray())){
							result.numberValue = exp(left.numberValue);
						}else if(StringsEqual(ast.value, "sqrt".toCharArray())){
							result.numberValue = sqrt(left.numberValue);
						}else if(StringsEqual(ast.value, "cbrt".toCharArray())){
							result.numberValue = pow(left.numberValue, 1d/3d);
						}else if(StringsEqual(ast.value, "gamma".toCharArray())){
							result.numberValue = Gamma(left.numberValue);
						}else if(StringsEqual(ast.value, "unary-".toCharArray())){
							result.numberValue = -left.numberValue;
						}else if(StringsEqual(ast.value, "unary+".toCharArray())){
							result.numberValue = +left.numberValue;
						}
					}
				}

				if(binary){

					success = EvaluateAST(ast.l, left, message);
					if(success){
						success = EvaluateAST(ast.r, right, message);
						if(success){
							if(StringsEqual(ast.value, "+".toCharArray())){
								result.numberValue = left.numberValue + right.numberValue;
							}else if(StringsEqual(ast.value, "-".toCharArray())){
								result.numberValue = left.numberValue - right.numberValue;
							}else if(StringsEqual(ast.value, "*".toCharArray())){
								result.numberValue = left.numberValue*right.numberValue;
							}else if(StringsEqual(ast.value, "/".toCharArray())){
								result.numberValue = left.numberValue/right.numberValue;
							}else if(StringsEqual(ast.value, "^".toCharArray())){
								result.numberValue = pow(left.numberValue, right.numberValue);
							}
						}
					}
				}
			}
		}

		return success;
	}

	public static char [] IntegerToFormula(double n){
		StringArrayReference a, b;
		LinkedListCharacters ll;
		char [] str;
		double state, p, ps, orgn, pcut;

		orgn = n;

		ll = CreateLinkedListCharacter();

		pcut = 10d;

		a = CreateStringArrayReferenceLengthValue(22d, "".toCharArray());
		a.stringArray[0].string = "1".toCharArray();
		a.stringArray[1].string = "2".toCharArray();
		a.stringArray[2].string = "3".toCharArray();
		a.stringArray[3].string = "5".toCharArray();
		a.stringArray[4].string = "7".toCharArray();
		a.stringArray[5].string = "11".toCharArray();
		a.stringArray[6].string = "pi".toCharArray();
		a.stringArray[7].string = "g".toCharArray();
		a.stringArray[8].string = "o".toCharArray();
		a.stringArray[9].string = "e".toCharArray();
		a.stringArray[10].string = "-(".toCharArray();
		a.stringArray[11].string = "sin(".toCharArray();
		a.stringArray[12].string = "cos(".toCharArray();
		a.stringArray[13].string = "tan(".toCharArray();
		a.stringArray[14].string = "asin(".toCharArray();
		a.stringArray[15].string = "acos(".toCharArray();
		a.stringArray[16].string = "atan(".toCharArray();
		a.stringArray[17].string = "gamma(".toCharArray();
		a.stringArray[18].string = "ln(".toCharArray();
		a.stringArray[19].string = "sqrt(".toCharArray();
		a.stringArray[20].string = "cbrt(".toCharArray();
		a.stringArray[21].string = "(".toCharArray();

		b = CreateStringArrayReferenceLengthValue(6d, "".toCharArray());
		b.stringArray[0].string = ")".toCharArray();
		b.stringArray[1].string = "+".toCharArray();
		b.stringArray[2].string = "-".toCharArray();
		b.stringArray[3].string = "*".toCharArray();
		b.stringArray[4].string = "/".toCharArray();
		b.stringArray[5].string = "^".toCharArray();

		state = 0d;
		ps = 0d;

		for(; n > 0d; ){
			if(state == 0d){
				p = n%a.stringArray.length;
				n = n - p;
				n = n/a.stringArray.length;

				LinkedListCharactersAddString(ll, a.stringArray[(int)(p)].string);

				if(p < pcut){
					state = 1d;
				}else{
					ps = ps + 1d;
				}
			}else if(state == 1d){
				p = n%b.stringArray.length;
				n = n - p;
				n = n/b.stringArray.length;

				if(p == 0d && ps == 0d){
					p = 1d;
				}

				LinkedListCharactersAddString(ll, b.stringArray[(int)(p)].string);

				state = 0d;
			}

			/* Add final piece.*/
			if(state == 0d && n == 0d){
				p = orgn%pcut;
				LinkedListCharactersAddString(ll, a.stringArray[(int)(p)].string);
			}
		}

		/* End paren*/
		for(; ps > 0d; ps = ps - 1d){
			LinkedListCharactersAddString(ll, ")".toCharArray());
		}

		str = LinkedListCharactersToArray(ll);

		FreeLinkedListCharacter(ll);

		return str;
	}

  public static void delete(Object object){
    // Java has garbage collection.
  }
}
