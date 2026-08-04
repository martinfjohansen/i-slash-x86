package FormulaTranslation.BooleanFormula;

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


import static FormulaTranslation.ArithmeticFormulaPratt.ArithmeticFormulaPratt.*;

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

public class BooleanFormula{
	public static boolean TokenizeBooleanFormula(char [] f, StringArrayReference tokens, StringReference errorMessage){
		boolean success;
		double i;
		LinkedListStrings ll;
		LinkedListCharacters llc;
		char [] str;
		boolean found;
		char c1, c2, c3;

		success = true;
		ll = CreateLinkedListString();
		llc = CreateLinkedListCharacter();

		for(i = 0d; i < f.length && success; ){
			found = false;

			c1 = f[(int)(i)];

			if(i + 2d < f.length){
				c2 = f[(int)(i + 1d)];
				c3 = f[(int)(i + 2d)];

				if(c1 == '<' && c2 == '-' && c3 == '>'){
					LinkedListAddString(ll, "<->".toCharArray());
					i = i + 3d;
					found = true;
				}
			}

			if(!found){
				if(i + 1d < f.length){
					c2 = f[(int)(i + 1d)];

					if(c1 == '<' && c2 == '='){
						LinkedListAddString(ll, "<=".toCharArray());
						i = i + 2d;
						found = true;
					}else if(c1 == '>' && c2 == '='){
						LinkedListAddString(ll, ">=".toCharArray());
						i = i + 2d;
						found = true;
					}else if(c1 == '!' && c2 == '='){
						LinkedListAddString(ll, "!=".toCharArray());
						i = i + 2d;
						found = true;
					}else if(c1 == '-' && c2 == '>'){
						LinkedListAddString(ll, "->".toCharArray());
						i = i + 2d;
						found = true;
					}
				}
			}

			if(!found){
				if(c1 == '('){
					LinkedListAddString(ll, "(".toCharArray());
					i = i + 1d;
					found = true;
				}else if(c1 == ')'){
					LinkedListAddString(ll, ")".toCharArray());
					i = i + 1d;
					found = true;
				}else if(c1 == '!'){
					LinkedListAddString(ll, "!".toCharArray());
					i = i + 1d;
					found = true;
				}else if(c1 == '&'){
					LinkedListAddString(ll, "&".toCharArray());
					i = i + 1d;
					found = true;
				}else if(c1 == '^'){
					LinkedListAddString(ll, "^".toCharArray());
					i = i + 1d;
					found = true;
				}else if(c1 == '|'){
					LinkedListAddString(ll, "|".toCharArray());
					i = i + 1d;
					found = true;
				}else if(c1 == '='){
					LinkedListAddString(ll, "=".toCharArray());
					i = i + 1d;
					found = true;
				}else if(c1 == '<'){
					LinkedListAddString(ll, "<".toCharArray());
					i = i + 1d;
					found = true;
				}else if(c1 == '>'){
					LinkedListAddString(ll, ">".toCharArray());
					i = i + 1d;
					found = true;
				}else if(c1 == ' ' || c1 == '\t' || c1 == '\n'){
					i = i + 1d;
					found = true;
				}else if(charIsLetter(c1)){
					for(; i < f.length && (charIsLetter(f[(int)(i)]) || charIsNumber(f[(int)(i)]) || f[(int)(i)] == '_'); i = i + 1d){
						LinkedListAddCharacter(llc, f[(int)(i)]);
					}
					str = LinkedListCharactersToArray(llc);
					LinkedListAddString(ll, str);
					FreeLinkedListCharacter(llc);
					llc = CreateLinkedListCharacter();

					found = true;
				}else if(charIsNumber(c1)){
					for(; i < f.length && (charIsNumber(f[(int)(i)]) || f[(int)(i)] == '.'); i = i + 1d){
						LinkedListAddCharacter(llc, f[(int)(i)]);
					}
					str = LinkedListCharactersToArray(llc);
					LinkedListAddString(ll, str);
					FreeLinkedListCharacter(llc);
					llc = CreateLinkedListCharacter();

					found = true;
				}
			}

			if(!found){
				success = false;
				errorMessage.string = "Unknown token in formula.".toCharArray();
			}
		}

		FreeLinkedListCharacter(llc);
		if(success){
			LinkedListAddString(ll, "<end>".toCharArray());
			tokens.stringArray = LinkedListStringsToArray(ll);
		}
		FreeLinkedListString(ll);

		return success;
	}

	public static boolean ParseBooleanTokens(StringReference [] tokens, NumberReference cur, ASTNode ast, StringReference message){
		boolean success;

		success = ParseIff(tokens, cur, ast, message);

		if(success){
			success = TokenIs(tokens, cur, "<end>".toCharArray());

			if(!success){
				message.string = "Expected the end of the formula.".toCharArray();
			}
		}

		return success;
	}

	public static boolean ParseIff(StringReference [] tokens, NumberReference cur, ASTNode ast, StringReference message){
		boolean success;
		ASTNode t, t1;
		char [] op;

		t = new ASTNode();
		success = ParseImplies(tokens, cur, t, message);

		for(; success && TokenIs(tokens, cur, "<->".toCharArray()); ){
			op = Index(tokens, cur);
			AddToNumberReference(cur, 1d);
			t1 = new ASTNode();
			success = ParseImplies(tokens, cur, t1, message);
			if(success){
				t = CreateASTNode(t, t1, op);
			}
		}

		if(success){
			AssignASTNode(ast, t);
		}

		return success;
	}

	public static boolean ParseImplies(StringReference [] tokens, NumberReference cur, ASTNode ast, StringReference message){
		boolean success;
		ASTNode t, t1;
		char [] op;

		t = new ASTNode();
		success = ParseLogicalOr(tokens, cur, t, message);

		for(; success && TokenIs(tokens, cur, "->".toCharArray()); ){
			op = Index(tokens, cur);
			AddToNumberReference(cur, 1d);
			t1 = new ASTNode();
			success = ParseLogicalOr(tokens, cur, t1, message);
			if(success){
				t = CreateASTNode(t, t1, op);
			}
		}

		if(success){
			AssignASTNode(ast, t);
		}

		return success;
	}

	public static boolean ParseLogicalOr(StringReference [] tokens, NumberReference cur, ASTNode ast, StringReference message){
		boolean success;
		ASTNode t, t1;
		char [] op;

		t = new ASTNode();
		success = ParseLogicalAnd(tokens, cur, t, message);

		for(; success && TokenIs(tokens, cur, "|".toCharArray()); ){
			op = Index(tokens, cur);
			AddToNumberReference(cur, 1d);
			t1 = new ASTNode();
			success = ParseLogicalAnd(tokens, cur, t1, message);
			if(success){
				t = CreateASTNode(t, t1, op);
			}
		}

		if(success){
			AssignASTNode(ast, t);
		}

		return success;
	}

	public static boolean ParseLogicalAnd(StringReference [] tokens, NumberReference cur, ASTNode ast, StringReference message){
		boolean success;
		ASTNode t, t1;
		char [] op;

		t = new ASTNode();
		success = ParseLogicalXor(tokens, cur, t, message);

		for(; success && TokenIs(tokens, cur, "&".toCharArray()); ){
			op = Index(tokens, cur);
			AddToNumberReference(cur, 1d);
			t1 = new ASTNode();
			success = ParseLogicalXor(tokens, cur, t1, message);
			if(success){
				t = CreateASTNode(t, t1, op);
			}
		}

		if(success){
			AssignASTNode(ast, t);
		}

		return success;
	}

	public static boolean ParseLogicalXor(StringReference [] tokens, NumberReference cur, ASTNode ast, StringReference message){
		boolean success;
		ASTNode t, t1;
		char [] op;

		t = new ASTNode();
		success = ParseLogicalComparison(tokens, cur, t, message);

		for(; success && TokenIs(tokens, cur, "^".toCharArray()); ){
			op = Index(tokens, cur);
			AddToNumberReference(cur, 1d);
			t1 = new ASTNode();
			success = ParseLogicalComparison(tokens, cur, t1, message);
			if(success){
				t = CreateASTNode(t, t1, op);
			}
		}

		if(success){
			AssignASTNode(ast, t);
		}

		return success;
	}

	public static boolean ParseLogicalComparison(StringReference [] tokens, NumberReference cur, ASTNode ast, StringReference message){
		boolean success;
		ASTNode t, t1;
		char [] op;

		t = new ASTNode();
		success = ParseLogicalLiteralVariableNotParenthesis(tokens, cur, t, message);

		for(; success && (TokenIs(tokens, cur, "=".toCharArray()) || TokenIs(tokens, cur, "!=".toCharArray()) || TokenIs(tokens, cur, "<".toCharArray()) || TokenIs(tokens, cur, "<=".toCharArray()) || TokenIs(tokens, cur, ">".toCharArray()) || TokenIs(tokens, cur, ">=".toCharArray())); ){
			op = Index(tokens, cur);
			AddToNumberReference(cur, 1d);
			t1 = new ASTNode();
			success = ParseLogicalLiteralVariableNotParenthesis(tokens, cur, t1, message);
			if(success){
				t = CreateASTNode(t, t1, op);
			}
		}

		if(success){
			AssignASTNode(ast, t);
		}

		return success;
	}

	public static boolean ParseLogicalLiteralVariableNotParenthesis(StringReference [] tokens, NumberReference cur, ASTNode ast, StringReference message){
		boolean success;
		ASTNode t;
		char [] token;

		success = true;

		token = Index(tokens, cur);
		if(charIsLetter(token[0]) || charIsNumber(token[0])){
			ast.leaf = true;
			ast.value = token;
			AddToNumberReference(cur, 1d);
		}else if(TokenIs(tokens, cur, "(".toCharArray())){
			AddToNumberReference(cur, 1d);

			t = new ASTNode();
			success = ParseIff(tokens, cur, t, message);

			if(success){
				ast.value = "()".toCharArray();
				ast.l = t;
				ast.leaf = false;

				success = TokenIs(tokens, cur, ")".toCharArray());
				if(success){
					AddToNumberReference(cur, 1d);
				}else{
					message.string = "Expected \')\'.".toCharArray();
				}
			}
		}else if(TokenIs(tokens, cur, "!".toCharArray())){
			AddToNumberReference(cur, 1d);

			t = new ASTNode();
			success = ParseLogicalLiteralVariableNotParenthesis(tokens, cur, t, message);

			if(success){
				ast.value = "!".toCharArray();
				ast.l = t;
				ast.leaf = false;
			}
		}else{
			success = false;
			message.string = "Unexpected token.".toCharArray();
		}

		return success;
	}

  public static void delete(Object object){
    // Java has garbage collection.
  }
}
