package FormulaTranslation.ASTNodes;

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

import static FormulaTranslation.ArithmeticFormulaEvaluator.ArithmeticFormulaEvaluator.*;

import static FormulaTranslation.StructuralFormulaSymbolicWriter.StructuralFormulaSymbolicWriter.*;

import static FormulaTranslation.BooleanFormulaSymbolicWriter.BooleanFormulaSymbolicWriter.*;

import static FormulaTranslation.BitwiseFormulaFunctionWriter.BitwiseFormulaFunctionWriter.*;

import static FormulaTranslation.ArithmeticFormulaSymbolicWriter.ArithmeticFormulaSymbolicWriter.*;

import static FormulaTranslation.BitwiseFormulaSymbolicWriter.BitwiseFormulaSymbolicWriter.*;

import static FormulaTranslation.BooleanFormulaFunctionWriter.BooleanFormulaFunctionWriter.*;

public class ASTNodes{
	public static void AddToNumberReference(NumberReference cur, double val){
		cur.numberValue = cur.numberValue + val;
	}

	public static boolean TokenIs(StringReference [] tokens, NumberReference cur, char [] s){
		return StringsEqual(Index(tokens, cur), s);
	}

	public static boolean NextTokenIs(StringReference [] tokens, NumberReference cur, char [] s){
		return StringsEqual(NextIndex(tokens, cur), s);
	}

	public static char [] NextIndex(StringReference [] stringArray, NumberReference index){
		return stringArray[(int)(index.numberValue + 1d)].string;
	}

	public static char [] Index(StringReference [] stringArray, NumberReference index){
		return stringArray[(int)(index.numberValue)].string;
	}

	public static void AssignASTNode(ASTNode x, ASTNode a){
		x.leaf = a.leaf;
		x.l = a.l;
		x.r = a.r;
		x.value = a.value;
	}

	public static ASTNode CreateASTNode(ASTNode l, ASTNode r, char [] op){
		ASTNode t;

		t = new ASTNode();
		t.value = op;
		t.l = l;
		t.r = r;
		t.leaf = false;

		return t;
	}

  public static void delete(Object object){
    // Java has garbage collection.
  }
}
