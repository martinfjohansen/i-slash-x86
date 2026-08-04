package FormulaTranslation.ArithmeticFormulaPratt;

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

import static FormulaTranslation.StructuralFormula.StructuralFormula.*;

import static FormulaTranslation.ArithmeticFormula.ArithmeticFormula.*;

import static FormulaTranslation.BitwiseFormula.BitwiseFormula.*;

import static FormulaTranslation.ArithmeticFormulaFunctionWriter.ArithmeticFormulaFunctionWriter.*;

import static FormulaTranslation.StructuralFormulaFunctionWriter.StructuralFormulaFunctionWriter.*;

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

public class ArithmeticFormulaPratt{
	public static boolean PrattParseArithmeticTokens(StringReference [] tokens, ASTNode ast, StringReference message){
		boolean success;
		double MIN_BINDING_POWER;
		NumberReference cur;
		ASTNodeRef astNodeRef;

		MIN_BINDING_POWER = -1d;

		cur = CreateNumberReference(0d);
		astNodeRef = new ASTNodeRef();
		success = PrattParseExpression(tokens, cur, MIN_BINDING_POWER, astNodeRef, message);

		if(success){
			AssignASTNode(ast, astNodeRef.node);

			if(TokenIs(tokens, cur, "<end>".toCharArray())){
			}else{
				message.string = "Expected the end of the formula.".toCharArray();
				success = false;
			}
		}

		return success;
	}

	public static boolean PrattParseExpression(StringReference [] tokens, NumberReference cur, double rightBindingPower, ASTNodeRef left, StringReference message){
		char [] tok;
		ASTNodeRef right;
		double innerBindingPower, leftBindingPower;
		boolean success;

		tok = Index(tokens, cur);
		AddToNumberReference(cur, 1d);

		success = PrattParseUnaryExpression(tokens, cur, tok, left, message);

		for(; rightBindingPower < GetBindingPower(Index(tokens, cur)) && success; ){
			tok = Index(tokens, cur);
			AddToNumberReference(cur, 1d);
			right = new ASTNodeRef();

			if(IsRightAssociative(tok)){
				innerBindingPower = GetBindingPower(tok) - 1d;
				success = PrattParseExpression(tokens, cur, innerBindingPower, right, message);
				if(success){
					left.node = CreateASTNode(left.node, right.node, tok);
				}
			}else{
				leftBindingPower = GetBindingPower(tok);
				success = PrattParseExpression(tokens, cur, leftBindingPower, right, message);
				if(success){
					left.node = CreateASTNode(left.node, right.node, tok);
				}
			}
		}

		return success;
	}

	public static boolean PrattParseUnaryExpression(StringReference [] tokens, NumberReference cur, char [] tok, ASTNodeRef rexp, StringReference message){
		boolean success;
		double MIN_BINDING_POWER;
		ASTNodeRef expr;

		MIN_BINDING_POWER = -1d;
		success = true;
		expr = new ASTNodeRef();

		if(IsKnownArithmeticFunction(tok)){
			AddToNumberReference(cur, 1d);

			success = PrattParseExpression(tokens, cur, MIN_BINDING_POWER, expr, message);

			if(StringsEqual(Index(tokens, cur), ")".toCharArray())){
				AddToNumberReference(cur, 1d);
				rexp.node = new ASTNode();
				rexp.node.value = tok;
				rexp.node.l = expr.node;
				rexp.node.leaf = false;
			}else{
				success = false;
				message.string = "Expected \')\'.".toCharArray();
			}
		}else if(StringsEqual(tok, "(".toCharArray())){
			success = PrattParseExpression(tokens, cur, MIN_BINDING_POWER, expr, message);

			if(StringsEqual(Index(tokens, cur), ")".toCharArray())){
				AddToNumberReference(cur, 1d);
				rexp.node = new ASTNode();
				rexp.node.value = "()".toCharArray();
				rexp.node.l = expr.node;
				rexp.node.leaf = false;
			}else{
				success = false;
				message.string = "Expected \')\'.".toCharArray();
			}
		}else if(StringsEqual(tok, "+".toCharArray()) || StringsEqual(tok, "-".toCharArray())){
			if(StringsEqual(tok, "-".toCharArray())){
				tok = "unary-".toCharArray();
			}else{
				tok = "unary+".toCharArray();
			}

			success = PrattParseExpression(tokens, cur, GetBindingPower(tok), expr, message);
			if(success){
				rexp.node = new ASTNode();
				rexp.node.value = tok;
				rexp.node.l = expr.node;
			}
		}else{
			rexp.node = new ASTNode();
			rexp.node.leaf = true;
			rexp.node.value = tok;
		}

		return success;
	}

	public static double GetBindingPower(char [] kind){
		double bindingPower, MIN_BINDING_POWER;

		MIN_BINDING_POWER = -1d;

		if(StringsEqual(kind, "+".toCharArray()) || StringsEqual(kind, "-".toCharArray())){
			bindingPower = 10d;
		}else if(StringsEqual(kind, "*".toCharArray()) || StringsEqual(kind, "/".toCharArray())){
			bindingPower = 20d;
		}else if(StringsEqual(kind, "unary-".toCharArray()) || StringsEqual(kind, "unary+".toCharArray())){
			bindingPower = 25d;
		}else if(StringsEqual(kind, "^".toCharArray())){
			bindingPower = 30d;
		}else{
			bindingPower = MIN_BINDING_POWER;
		}

		return bindingPower;
	}

	public static boolean IsRightAssociative(char [] kind){
		boolean isRightAssoc;

		if(StringsEqual(kind, "^".toCharArray())){
			isRightAssoc = true;
		}else{
			isRightAssoc = false;
		}

		return isRightAssoc;
	}

  public static void delete(Object object){
    // Java has garbage collection.
  }
}
