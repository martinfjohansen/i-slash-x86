package numbers.StringToNumber;

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

import static numbers.NumberComputations.NumberComputations.*;

public class StringToNumber{
	public static boolean CreateNumberFromDecimalStringWithCheck(char [] string, NumberReference decimalReference, StringReference message){
		return CreateNumberFromStringWithCheck(string, 10d, decimalReference, message);
	}

	public static double CreateNumberFromDecimalString(char [] string){
		NumberReference doubleReference;
		StringReference stringReference;
		double number;

		doubleReference = CreateNumberReference(0d);
		stringReference = CreateStringReference("".toCharArray());
		CreateNumberFromStringWithCheck(string, 10d, doubleReference, stringReference);
		number = doubleReference.numberValue;

		delete(doubleReference);
		delete(stringReference);

		return number;
	}

	public static boolean CreateNumberFromStringWithCheck(char [] string, double base, NumberReference numberReference, StringReference message){
		boolean success;
		BooleanReference numberIsPositive, exponentIsPositive;
		NumberArrayReference beforePoint, afterPoint, exponent;

		numberIsPositive = CreateBooleanReference(true);
		exponentIsPositive = CreateBooleanReference(true);
		beforePoint = new NumberArrayReference();
		afterPoint = new NumberArrayReference();
		exponent = new NumberArrayReference();

		if(base >= 2d && base <= 36d){
			success = ExtractPartsFromNumberString(string, base, numberIsPositive, beforePoint, afterPoint, exponentIsPositive, exponent, message);

			if(success){
				numberReference.numberValue = CreateNumberFromParts(base, numberIsPositive.booleanValue, beforePoint.numberArray, afterPoint.numberArray, exponentIsPositive.booleanValue, exponent.numberArray);
			}
		}else{
			success = false;
			message.string = "Base must be from 2 to 36.".toCharArray();
		}

		return success;
	}

	public static double CreateNumberFromParts(double base, boolean numberIsPositive, double [] beforePoint, double [] afterPoint, boolean exponentIsPositive, double [] exponent){
		double n, i, p, e;

		n = 0d;

		for(i = 0d; i < beforePoint.length; i = i + 1d){
			p = beforePoint[(int)(beforePoint.length - i - 1d)];

			n = n + p*pow(base, i);
		}

		for(i = 0d; i < afterPoint.length; i = i + 1d){
			p = afterPoint[(int)(i)];

			n = n + p/pow(base, i + 1d);
		}

		if(exponent.length > 0d){
			e = 0d;
			for(i = 0d; i < exponent.length; i = i + 1d){
				p = exponent[(int)(exponent.length - i - 1d)];

				e = e + p*pow(base, i);
			}

			if(!exponentIsPositive){
				e = -e;
			}

			n = n*pow(base, e);
		}

		if(!numberIsPositive){
			n = -n;
		}

		return n;
	}

	public static boolean ExtractPartsFromNumberString(char [] n, double base, BooleanReference numberIsPositive, NumberArrayReference beforePoint, NumberArrayReference afterPoint, BooleanReference exponentIsPositive, NumberArrayReference exponent, StringReference errorMessages){
		double i, j, count;
		boolean success, done, complete;

		i = 0d;
		complete = false;

		if(i < n.length){
			if(n[(int)(i)] == '-'){
				numberIsPositive.booleanValue = false;
				i = i + 1d;
			}else if(n[(int)(i)] == '+'){
				numberIsPositive.booleanValue = true;
				i = i + 1d;
			}

			success = true;
		}else{
			success = false;
			errorMessages.string = "Number cannot have length zero.".toCharArray();
		}

		if(success){
			done = false;
			count = 0d;
			for(; i + count < n.length && !done; ){
				if(CharacterIsNumberCharacterInBase(n[(int)(i + count)], base)){
					count = count + 1d;
				}else{
					done = true;
				}
			}

			if(count >= 1d){
				beforePoint.numberArray = new double [(int)(count)];

				for(j = 0d; j < count; j = j + 1d){
					beforePoint.numberArray[(int)(j)] = GetNumberFromNumberCharacterForBase(n[(int)(i + j)], base);
				}

				i = i + count;

				if(i < n.length){
					success = true;
				}else{
					afterPoint.numberArray = new double [0];
					exponent.numberArray = new double [0];
					success = true;
					complete = true;
				}
			}else{
				success = false;
				errorMessages.string = "Number must have at least one number after the optional sign.".toCharArray();
			}
		}

		if(success && !complete){
			if(n[(int)(i)] == '.'){
				i = i + 1d;

				if(i < n.length){
					done = false;
					count = 0d;
					for(; i + count < n.length && !done; ){
						if(CharacterIsNumberCharacterInBase(n[(int)(i + count)], base)){
							count = count + 1d;
						}else{
							done = true;
						}
					}

					if(count >= 1d){
						afterPoint.numberArray = new double [(int)(count)];

						for(j = 0d; j < count; j = j + 1d){
							afterPoint.numberArray[(int)(j)] = GetNumberFromNumberCharacterForBase(n[(int)(i + j)], base);
						}

						i = i + count;

						if(i < n.length){
							success = true;
						}else{
							exponent.numberArray = new double [0];
							success = true;
							complete = true;
						}
					}else{
						success = false;
						errorMessages.string = "There must be at least one digit after the decimal point.".toCharArray();
					}
				}else{
					success = false;
					errorMessages.string = "There must be at least one digit after the decimal point.".toCharArray();
				}
			}else if(base <= 14d && (n[(int)(i)] == 'e' || n[(int)(i)] == 'E')){
				if(i < n.length){
					success = true;
					afterPoint.numberArray = new double [0];
				}else{
					success = false;
					errorMessages.string = "There must be at least one digit after the exponent.".toCharArray();
				}
			}else{
				success = false;
				errorMessages.string = "Expected decimal point or exponent symbol.".toCharArray();
			}
		}

		if(success && !complete){
			if(base <= 14d && (n[(int)(i)] == 'e' || n[(int)(i)] == 'E')){
				i = i + 1d;

				if(i < n.length){
					if(n[(int)(i)] == '-'){
						exponentIsPositive.booleanValue = false;
						i = i + 1d;
					}else if(n[(int)(i)] == '+'){
						exponentIsPositive.booleanValue = true;
						i = i + 1d;
					}

					if(i < n.length){
						done = false;
						count = 0d;
						for(; i + count < n.length && !done; ){
							if(CharacterIsNumberCharacterInBase(n[(int)(i + count)], base)){
								count = count + 1d;
							}else{
								done = true;
							}
						}

						if(count >= 1d){
							exponent.numberArray = new double [(int)(count)];

							for(j = 0d; j < count; j = j + 1d){
								exponent.numberArray[(int)(j)] = GetNumberFromNumberCharacterForBase(n[(int)(i + j)], base);
							}

							i = i + count;

							if(i == n.length){
								success = true;
							}else{
								success = false;
								errorMessages.string = "There cannot be any characters past the exponent of the number.".toCharArray();
							}
						}else{
							success = false;
							errorMessages.string = "There must be at least one digit after the decimal point.".toCharArray();
						}
					}else{
						success = false;
						errorMessages.string = "There must be at least one digit after the exponent symbol.".toCharArray();
					}
				}else{
					success = false;
					errorMessages.string = "There must be at least one digit after the exponent symbol.".toCharArray();
				}
			}else{
				success = false;
				errorMessages.string = "Expected exponent symbol.".toCharArray();
			}
		}

		return success;
	}

	public static double GetNumberFromNumberCharacterForBase(char c, double base){
		char [] numberTable;
		double i;
		double position;

		numberTable = GetDigitCharacterTable();
		position = 0d;

		for(i = 0d; i < base; i = i + 1d){
			if(numberTable[(int)(i)] == c){
				position = i;
			}
		}

		return position;
	}

	public static boolean CharacterIsNumberCharacterInBase(char c, double base){
		char [] numberTable;
		double i;
		boolean found;

		numberTable = GetDigitCharacterTable();
		found = false;

		for(i = 0d; i < base; i = i + 1d){
			if(numberTable[(int)(i)] == c){
				found = true;
			}
		}

		return found;
	}

	public static double [] StringToNumberArray(char [] str){
		NumberArrayReference numberArrayReference;
		StringReference stringReference;
		double [] numbers;

		numberArrayReference = new NumberArrayReference();
		stringReference = new StringReference();

		StringToNumberArrayWithCheck(str, numberArrayReference, stringReference);

		numbers = numberArrayReference.numberArray;

		delete(numberArrayReference);
		delete(stringReference);

		return numbers;
	}

	public static boolean StringToNumberArrayWithCheck(char [] str, NumberArrayReference numberArrayReference, StringReference errorMessage){
		StringReference [] numberStrings;
		double [] numbers;
		double i;
		char [] numberString, trimmedNumberString;
		boolean success;
		NumberReference numberReference;

		numberStrings = SplitByString(str, ",".toCharArray());

		numbers = new double [(int)(numberStrings.length)];
		success = true;
		numberReference = new NumberReference();

		for(i = 0d; i < numberStrings.length; i = i + 1d){
			numberString = numberStrings[(int)(i)].string;
			trimmedNumberString = Trim(numberString);
			success = CreateNumberFromDecimalStringWithCheck(trimmedNumberString, numberReference, errorMessage);
			numbers[(int)(i)] = numberReference.numberValue;

			FreeStringReference(numberStrings[(int)(i)]);
			delete(trimmedNumberString);
		}

		delete(numberStrings);
		delete(numberReference);

		numberArrayReference.numberArray = numbers;

		return success;
	}

  public static void delete(Object object){
    // Java has garbage collection.
  }
}
