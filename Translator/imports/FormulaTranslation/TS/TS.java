package FormulaTranslation.TS;

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

import static FormulaTranslation.ArithmeticFormulaEvaluator.ArithmeticFormulaEvaluator.*;

import static FormulaTranslation.StructuralFormulaSymbolicWriter.StructuralFormulaSymbolicWriter.*;

import static FormulaTranslation.BooleanFormulaSymbolicWriter.BooleanFormulaSymbolicWriter.*;

import static FormulaTranslation.BitwiseFormulaFunctionWriter.BitwiseFormulaFunctionWriter.*;

import FormulaTranslation.ASTNodes.*;
import static FormulaTranslation.ASTNodes.ASTNodes.*;

import static FormulaTranslation.ArithmeticFormulaSymbolicWriter.ArithmeticFormulaSymbolicWriter.*;

import static FormulaTranslation.BitwiseFormulaSymbolicWriter.BitwiseFormulaSymbolicWriter.*;

import static FormulaTranslation.BooleanFormulaFunctionWriter.BooleanFormulaFunctionWriter.*;

public class TS{
	public static double AllocateTVariable(BooleanArrayReference ts){
		double i, newT;
		boolean found;
		boolean [] newArray;

		newT = 0d;
		found = false;

		for(i = 0d; i < ts.booleanArray.length && !found; i = i + 1d){
			if(!ts.booleanArray[(int)(i)]){
				found = true;
				newT = i;
			}
		}

		if(found){
		}else{
			newT = i;
			newArray = new boolean [(int)(ts.booleanArray.length + 1d)];
			for(i = 0d; i < ts.booleanArray.length; i = i + 1d){
				newArray[(int)(i)] = ts.booleanArray[(int)(i)];
			}
			newArray[(int)(newT)] = false;
			delete(ts.booleanArray);
			ts.booleanArray = newArray;
		}

		/* Mark as allocated:*/
		ts.booleanArray[(int)(newT)] = true;

		return newT;
	}

	public static void FreeTVariable(BooleanArrayReference ts, double t){
		ts.booleanArray[(int)(t)] = false;
	}

  public static void delete(Object object){
    // Java has garbage collection.
  }
}
