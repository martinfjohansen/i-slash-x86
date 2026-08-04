package numbers.NumberComputations;

import static java.lang.Math.*;

import static strings.stream.stream.*;

import static strings.strings.strings.*;

import references.references.*;
import static references.references.references.*;

import static math.math.math.*;

import static math.Decimal15E2.Decimal15E2.*;

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


import static numbers.NumberToString.NumberToString.*;

import static numbers.StringToNumber.StringToNumber.*;

public class NumberComputations{
	public static boolean IsValidNumber(char [] str){
		boolean valid;
		NumberReference numberRef;
		StringReference message;

		numberRef = new NumberReference();
		message = new StringReference();

		valid = CreateNumberFromDecimalStringWithCheck(str, numberRef, message);

		delete(numberRef);
		delete(message);

		return valid;
	}

	public static boolean IsValidInteger(char [] str){
		boolean valid;
		NumberReference numberRef;
		StringReference message;

		numberRef = new NumberReference();
		message = new StringReference();

		valid = CreateNumberFromDecimalStringWithCheck(str, numberRef, message);

		if(valid){
			valid = IsInteger(numberRef.numberValue);
		}

		delete(numberRef);
		delete(message);

		return valid;
	}

	public static boolean IsValidPositiveInteger(char [] str){
		boolean valid;
		NumberReference numberRef;
		StringReference message;

		numberRef = new NumberReference();
		message = new StringReference();

		valid = CreateNumberFromDecimalStringWithCheck(str, numberRef, message);

		if(valid){
			valid = IsInteger(numberRef.numberValue);
			if(valid){
				valid = numberRef.numberValue >= 0d;
			}
		}

		delete(numberRef);
		delete(message);

		return valid;
	}

  public static void delete(Object object){
    // Java has garbage collection.
  }
}
