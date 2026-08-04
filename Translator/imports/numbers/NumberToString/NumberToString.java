package numbers.NumberToString;

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


import static numbers.NumberComputations.NumberComputations.*;

import static numbers.StringToNumber.StringToNumber.*;

public class NumberToString{
	public static char [] CreateStringScientificNotationDecimalFromNumber(double n){
		StringReference mantissaReference, exponentReference;
		double e;
		boolean isPositive;
		char [] result;

		mantissaReference = new StringReference();
		exponentReference = new StringReference();
		result = new char [0];

		if(n < 0d){
			isPositive = false;
			n = -n;
		}else{
			isPositive = true;
		}

		if(n == 0d){
			e = 0d;
		}else{
			e = GetFirstDecimalDigitPosition(n);

			if(e < 0d){
				n = n*pow(10d, abs(e));
			}else{
				n = n/pow(10d, e);
			}
		}

		mantissaReference.string = CreateStringDecimalFromNumber(n);
		exponentReference.string = CreateStringDecimalFromNumber(e);

		if(!isPositive){
			result = AppendString(result, "-".toCharArray());
		}

		result = AppendString(result, mantissaReference.string);
		result = AppendString(result, "e".toCharArray());
		result = AppendString(result, exponentReference.string);

		return result;
	}

	public static char [] CreateStringDecimalFromNumber(double number){
		double d, factor, a, x, tz, extra, dotpos, p, zero;
		boolean isPositive, done, lessThan1, isInt;
		char [] str, ds;
		NumberReference factorRef;

		factorRef = new NumberReference();

		isPositive = true;

		if(number < 0d){
			isPositive = false;
			number = abs(number);
		}

		if(number == 0d){
			str = "0".toCharArray();
		}else if(number > 999999999999999e99){
			/* Guard the number against relaxations.*/
			if(isPositive){
				str = "Infinity".toCharArray();
			}else{
				str = "-Infinity".toCharArray();
			}
		}else{
			lessThan1 = number < 1d;

			/* Guard the number against relaxations.*/
			if(number < 1e-99){
				number = 0d;
			}

			/* 1. Turn number into an integer with 15 digits.*/
			number = NumberTo15DigitInteger(number, factorRef);
			factor = factorRef.numberValue;
			delete(factorRef);

			/* 2. Extract the 15 digits*/
			ds = new char [15];

			a = number;
			zero = '0';
			for(d = 0d; d < 15d; d = d + 1d){
				x = a - floor(a/10d)*10d;
				ds[(int)(15d - d - 1d)] = (char)((x + zero));
				a = floor(a/10d);
			}

			/* 3. Remove trailing zeros*/
			tz = 0d;
			done = false;
			for(d = 0d; d < 15d && !done; d = d + 1d){
				if(ds[(int)(15d - d - 1d)] == '0'){
					tz = tz + 1d;
				}else{
					done = true;
				}
			}
			ds = Substring(ds, 0d, 15d - tz);

			/* 4. Determine if integer*/
			isInt = factor + tz >= 0d;

			/* 5. Fill into formats*/
			if(isInt){
				/* |-----|*/
				/* AAAAAAA00000000*/
				str = new char [(int)(15d + factor)];
				for(d = 0d; d < str.length; d = d + 1d){
					str[(int)(d)] = '0';
				}
				for(d = 0d; d < ds.length; d = d + 1d){
					str[(int)(d)] = ds[(int)(d)];
				}
			}else if(lessThan1){
				/*       |-----|*/
				/* 0.0000AAAAAAA*/
				extra = -factor - 15d;
				str = new char [(int)(2d + extra + 15d - tz)];
				for(d = 0d; d < str.length; d = d + 1d){
					str[(int)(d)] = '0';
				}
				str[1] = '.';
				for(d = 0d; d < ds.length; d = d + 1d){
					str[(int)(2d + extra + d)] = ds[(int)(d)];
				}
			}else{
				/* |-------|*/
				/* AAAA.AAAA*/
				str = new char [(int)(1d + 15d - tz)];
				dotpos = 15d + factor;
				p = 0d;
				for(d = 0d; d < str.length; d = d + 1d){
					if(d == dotpos){
						str[(int)(d)] = '.';
					}else{
						str[(int)(d)] = ds[(int)(p)];
						p = p + 1d;
					}
				}
			}
		}

		/* Done*/
		if(!isPositive){
			str = ConcatenateString("-".toCharArray(), str);
		}

		return str;
	}

	public static boolean CreateStringFromNumberWithCheck(double number, double base, StringReference stringRef){
		DynamicArrayCharacters string;
		double maximumDigits, i, d, digitPosition, trailingZeros;
		boolean success, hasPrintedPoint, isPositive, done;
		CharacterReference characterReference;
		char c;

		string = CreateDynamicArrayCharacters();
		isPositive = true;

		if(number < 0d){
			isPositive = false;
			number = -number;
		}

		if(number == 0d){
			DynamicArrayAddCharacter(string, '0');
			success = true;
		}else{
			characterReference = new CharacterReference();

			if(IsInteger(base)){
				success = true;

				maximumDigits = GetMaximumDigitsForBase(base);

				digitPosition = GetFirstDigitPosition(number, base);

				hasPrintedPoint = false;

				if(!isPositive){
					DynamicArrayAddCharacter(string, '-');
				}

				/* Print leading zeros.*/
				if(digitPosition < 0d){
					DynamicArrayAddCharacter(string, '0');
					DynamicArrayAddCharacter(string, '.');
					hasPrintedPoint = true;
					for(i = 0d; i < -digitPosition - 1d; i = i + 1d){
						DynamicArrayAddCharacter(string, '0');
					}
				}

				/* Count trailing zeros*/
				trailingZeros = 0d;
				done = false;
				for(i = 0d; i < maximumDigits && !done; i = i + 1d){
					d = GetDigit(number, base, maximumDigits - i - 1d);
					if(d == 0d){
						trailingZeros = trailingZeros + 1d;
					}else{
						done = true;
					}
				}

				/* Print number.*/
				for(i = 0d; i < maximumDigits && success; i = i + 1d){
					d = GetDigit(number, base, i);

					if(d >= base){
						d = base - 1d;
					}

					if(!hasPrintedPoint && digitPosition - i + 1d == 0d){
						if(maximumDigits - i > trailingZeros){
							DynamicArrayAddCharacter(string, '.');
						}
						hasPrintedPoint = true;
					}

					if(maximumDigits - i <= trailingZeros && hasPrintedPoint){
					}else{
						success = GetSingleDigitCharacterFromNumberWithCheck(d, base, characterReference);
						if(success){
							c = characterReference.characterValue;
							DynamicArrayAddCharacter(string, c);
						}
					}
				}

				if(success){
					/* Print trailing zeros.*/
					for(i = 0d; i < digitPosition - maximumDigits + 1d; i = i + 1d){
						DynamicArrayAddCharacter(string, '0');
					}
				}
			}else{
				success = false;
			}
		}

		if(success){
			stringRef.string = DynamicArrayCharactersToArray(string);
			FreeDynamicArrayCharacters(string);
		}

		/* Done*/
		return success;
	}

	public static double GetMaximumDigitsForBase(double base){
		double t;

		t = pow(10d, 15d);
		return floor(log10(t)/log10(base));
	}

	public static double GetMaximumDigitsForDecimal(){
		return 15d;
	}

	public static double NumberTo15DigitInteger(double n, NumberReference factorRef){
		double i, dp;
		double [] factors;

		factors = GetPowersOfTenFor15d2e();
		dp = GetFirstDecimalDigitPosition(n);
		factorRef.numberValue = dp - 14d;

		i = 14d + -dp;

		n = MultiplyWithIntegerPowerOf10(n, factors, i);

		delete(factors);

		n = Round(n);

		if(n >= 1e15){
			n = n/10d;
			factorRef.numberValue = factorRef.numberValue + 1d;
		}

		return n;
	}

	public static double MultiplyWithIntegerPowerOf10(double n, double [] factors, double power){
		n = n*factors[(int)(power + 99d)];

		return n;
	}

	public static double GetFirstDecimalDigitPosition(double n){
		double power, i;
		double [] factors;
		boolean found;

		n = abs(n);

		factors = GetPowersOfTenFor15d2e();

		power = 1d;

		if(n == 0d){
			power = 0d;
		}else if(n > 999999999999999e99){
			/* This guards against relaxed variables' max value*/
			power = 114d;
		}else if(n < 1e-99){
			/* This guards against relaxed variables' min value*/
			power = -100d;
		}else{
			found = false;
			/* Search the most likely space first.*/
			for(i = 99d - 20d; i < 99d + 20d && !found; i = i + 1d){
				if(n >= factors[(int)(i)] && n < factors[(int)(i + 1d)]){
					power = i - 99d;
					found = true;
				}
			}
			/* Search the whole space*/
			for(i = 0d; i < factors.length - 1d && !found; i = i + 1d){
				if(n >= factors[(int)(i)] && n < factors[(int)(i + 1d)]){
					power = i - 99d;
					found = true;
				}
			}
			if(!found){
				if(n >= 100000000000000e99 && n <= 999999999999999e99){
					power = i - 99d;
				}
			}
		}

		delete(factors);

		/* Normal returns are -99 to 113. If -100 or 114 is returned, it means a relaxation is used.*/
		return power;
	}

	public static double [] GetPowersOfTenFor15d2e(){
		double [] factors;

		factors = new double [213];

		factors[0] = 1e-99;
		factors[1] = 1e-98;
		factors[2] = 1e-97;
		factors[3] = 1e-96;
		factors[4] = 1e-95;
		factors[5] = 1e-94;
		factors[6] = 1e-93;
		factors[7] = 1e-92;
		factors[8] = 1e-91;
		factors[9] = 1e-90;
		factors[10] = 1e-89;
		factors[11] = 1e-88;
		factors[12] = 1e-87;
		factors[13] = 1e-86;
		factors[14] = 1e-85;
		factors[15] = 1e-84;
		factors[16] = 1e-83;
		factors[17] = 1e-82;
		factors[18] = 1e-81;
		factors[19] = 1e-80;
		factors[20] = 1e-79;
		factors[21] = 1e-78;
		factors[22] = 1e-77;
		factors[23] = 1e-76;
		factors[24] = 1e-75;
		factors[25] = 1e-74;
		factors[26] = 1e-73;
		factors[27] = 1e-72;
		factors[28] = 1e-71;
		factors[29] = 1e-70;
		factors[30] = 1e-69;
		factors[31] = 1e-68;
		factors[32] = 1e-67;
		factors[33] = 1e-66;
		factors[34] = 1e-65;
		factors[35] = 1e-64;
		factors[36] = 1e-63;
		factors[37] = 1e-62;
		factors[38] = 1e-61;
		factors[39] = 1e-60;
		factors[40] = 1e-59;
		factors[41] = 1e-58;
		factors[42] = 1e-57;
		factors[43] = 1e-56;
		factors[44] = 1e-55;
		factors[45] = 1e-54;
		factors[46] = 1e-53;
		factors[47] = 1e-52;
		factors[48] = 1e-51;
		factors[49] = 1e-50;
		factors[50] = 1e-49;
		factors[51] = 1e-48;
		factors[52] = 1e-47;
		factors[53] = 1e-46;
		factors[54] = 1e-45;
		factors[55] = 1e-44;
		factors[56] = 1e-43;
		factors[57] = 1e-42;
		factors[58] = 1e-41;
		factors[59] = 1e-40;
		factors[60] = 1e-39;
		factors[61] = 1e-38;
		factors[62] = 1e-37;
		factors[63] = 1e-36;
		factors[64] = 1e-35;
		factors[65] = 1e-34;
		factors[66] = 1e-33;
		factors[67] = 1e-32;
		factors[68] = 1e-31;
		factors[69] = 1e-30;
		factors[70] = 1e-29;
		factors[71] = 1e-28;
		factors[72] = 1e-27;
		factors[73] = 1e-26;
		factors[74] = 1e-25;
		factors[75] = 1e-24;
		factors[76] = 1e-23;
		factors[77] = 1e-22;
		factors[78] = 1e-21;
		factors[79] = 1e-20;
		factors[80] = 1e-19;
		factors[81] = 1e-18;
		factors[82] = 1e-17;
		factors[83] = 1e-16;
		factors[84] = 1e-15;
		factors[85] = 1e-14;
		factors[86] = 1e-13;
		factors[87] = 1e-12;
		factors[88] = 1e-11;
		factors[89] = 1e-10;
		factors[90] = 1e-9;
		factors[91] = 1e-8;
		factors[92] = 1e-7;
		factors[93] = 1e-6;
		factors[94] = 1e-5;
		factors[95] = 1e-4;
		factors[96] = 1e-3;
		factors[97] = 1e-2;
		factors[98] = 1e-1;
		factors[99] = 1e0;
		factors[100] = 1e1;
		factors[101] = 1e2;
		factors[102] = 1e3;
		factors[103] = 1e4;
		factors[104] = 1e5;
		factors[105] = 1e6;
		factors[106] = 1e7;
		factors[107] = 1e8;
		factors[108] = 1e9;
		factors[109] = 1e10;
		factors[110] = 1e11;
		factors[111] = 1e12;
		factors[112] = 1e13;
		factors[113] = 1e14;
		factors[114] = 1e15;
		factors[115] = 1e16;
		factors[116] = 1e17;
		factors[117] = 1e18;
		factors[118] = 1e19;
		factors[119] = 1e20;
		factors[120] = 1e21;
		factors[121] = 1e22;
		factors[122] = 1e23;
		factors[123] = 1e24;
		factors[124] = 1e25;
		factors[125] = 1e26;
		factors[126] = 1e27;
		factors[127] = 1e28;
		factors[128] = 1e29;
		factors[129] = 1e30;
		factors[130] = 1e31;
		factors[131] = 1e32;
		factors[132] = 1e33;
		factors[133] = 1e34;
		factors[134] = 1e35;
		factors[135] = 1e36;
		factors[136] = 1e37;
		factors[137] = 1e38;
		factors[138] = 1e39;
		factors[139] = 1e40;
		factors[140] = 1e41;
		factors[141] = 1e42;
		factors[142] = 1e43;
		factors[143] = 1e44;
		factors[144] = 1e45;
		factors[145] = 1e46;
		factors[146] = 1e47;
		factors[147] = 1e48;
		factors[148] = 1e49;
		factors[149] = 1e50;
		factors[150] = 1e51;
		factors[151] = 1e52;
		factors[152] = 1e53;
		factors[153] = 1e54;
		factors[154] = 1e55;
		factors[155] = 1e56;
		factors[156] = 1e57;
		factors[157] = 1e58;
		factors[158] = 1e59;
		factors[159] = 1e60;
		factors[160] = 1e61;
		factors[161] = 1e62;
		factors[162] = 1e63;
		factors[163] = 1e64;
		factors[164] = 1e65;
		factors[165] = 1e66;
		factors[166] = 1e67;
		factors[167] = 1e68;
		factors[168] = 1e69;
		factors[169] = 1e70;
		factors[170] = 1e71;
		factors[171] = 1e72;
		factors[172] = 1e73;
		factors[173] = 1e74;
		factors[174] = 1e75;
		factors[175] = 1e76;
		factors[176] = 1e77;
		factors[177] = 1e78;
		factors[178] = 1e79;
		factors[179] = 1e80;
		factors[180] = 1e81;
		factors[181] = 1e82;
		factors[182] = 1e83;
		factors[183] = 1e84;
		factors[184] = 1e85;
		factors[185] = 1e86;
		factors[186] = 1e87;
		factors[187] = 1e88;
		factors[188] = 1e89;
		factors[189] = 1e90;
		factors[190] = 1e91;
		factors[191] = 1e92;
		factors[192] = 1e93;
		factors[193] = 1e94;
		factors[194] = 1e95;
		factors[195] = 1e96;
		factors[196] = 1e97;
		factors[197] = 1e98;
		factors[198] = 1e99;
		factors[199] = 10e99;
		factors[200] = 100e99;
		factors[201] = 1000e99;
		factors[202] = 10000e99;
		factors[203] = 100000e99;
		factors[204] = 1000000e99;
		factors[205] = 10000000e99;
		factors[206] = 100000000e99;
		factors[207] = 1000000000e99;
		factors[208] = 10000000000e99;
		factors[209] = 100000000000e99;
		factors[210] = 1000000000000e99;
		factors[211] = 10000000000000e99;
		factors[212] = 100000000000000e99;

		return factors;
	}

	public static double GetFirstDigitPosition(double n, double base){
		double power, m, i, maximumDigits;
		boolean multiply, done;

		maximumDigits = GetMaximumDigitsForBase(base);
		n = abs(n);

		if(n != 0d){
			if(floor(n) < pow(base, maximumDigits)){
				multiply = true;
			}else{
				multiply = false;
			}

			done = false;
			m = 0d;
			for(i = 0d; !done; i = i + 1d){
				if(multiply){
					m = n*pow(base, i);
					if(floor(m) >= pow(base, maximumDigits - 1d)){
						done = true;
					}
				}else{
					m = n/pow(base, i);
					if(floor(m) < pow(base, maximumDigits)){
						done = true;
					}
				}
			}

			if(multiply){
				power = maximumDigits - i;
			}else{
				power = maximumDigits + i - 2d;
			}

			if(Round(m) >= pow(base, maximumDigits)){
				power = power + 1d;
			}
		}else{
			power = 1d;
		}

		return power;
	}

	public static boolean GetSingleDigitCharacterFromNumberWithCheck(double c, double base, CharacterReference characterReference){
		char [] numberTable;
		boolean success;

		numberTable = GetDigitCharacterTable();

		if(c < base || c < numberTable.length){
			success = true;
			characterReference.characterValue = numberTable[(int)(c)];
		}else{
			success = false;
		}

		return success;
	}

	public static boolean GetDecimalDigitCharacterFromNumberWithCheck(double c, CharacterReference characterRef){
		char [] numberTable;
		boolean success;

		numberTable = "0123456789".toCharArray();

		if(c >= 0d && c < 10d){
			success = true;
			characterRef.characterValue = numberTable[(int)(c)];
		}else{
			success = false;
		}

		return success;
	}

	public static char [] GetDigitCharacterTable(){
		char [] numberTable;

		numberTable = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

		return numberTable;
	}

	public static double GetDecimalDigit(double n, double index){
		double digitPosition;

		digitPosition = GetFirstDecimalDigitPosition(n);

		return GetDecimalDigitWithFirstDigitPosition(n, digitPosition, index);
	}

	public static double GetDecimalDigitWithFirstDigitPosition(double n, double digitPosition, double index){
		double d, m, i;
		NumberReference factorRef;

		n = abs(n);

		factorRef = new NumberReference();
		n = NumberTo15DigitInteger(n, factorRef);
		delete(factorRef);

		m = n;
		d = 0d;
		for(i = 0d; i < 15d - index; i = i + 1d){
			d = (double)round(m%10d);
			m = m - d;
			m = (double)round(m/10d);
		}

		return d;
	}

	public static double GetDigit(double n, double base, double index){
		double d, digitPosition, e, m, maximumDigits, i;

		n = abs(n);
		maximumDigits = GetMaximumDigitsForBase(base);
		digitPosition = GetFirstDigitPosition(n, base);

		e = maximumDigits - digitPosition - 1d;
		if(e < 0d){
			n = (double)round(n/pow(base, abs(e)));
		}else{
			n = (double)round(n*pow(base, e));
		}

		m = n;
		d = 0d;
		for(i = 0d; i < maximumDigits - index; i = i + 1d){
			d = (double)round(m%base);
			m = m - d;
			m = (double)round(m/base);
		}

		return d;
	}

	public static char [] NumberToHumanReadableShortScale(double n){
		char [] res, suffix;
		boolean hasSuffix;
		double k, M, B, T, Q;

		k = 1000d;
		M = k*1000d;
		B = M*1000d;
		T = B*1000d;
		Q = T*1000d;
		suffix = " ".toCharArray();

		if(n < k){
			hasSuffix = false;
		}else{
			hasSuffix = true;
		}

		if(n >= k && n < M){
			if(n < 10d*k){
				n = Round(n/100d);
				n = n/10d;
			}else{
				n = Round(n/k);
			}
			suffix = "k".toCharArray();
		}else if(n >= M && n < B){
			if(n < 10d*M){
				n = Round(n/(k*100d));
				n = n/10d;
			}else{
				n = Round(n/M);
			}
			suffix = "M".toCharArray();
		}else if(n >= B && n < T){
			if(n < 10d*B){
				n = Round(n/(M*100d));
				n = n/10d;
			}else{
				n = Round(n/B);
			}
			suffix = "B".toCharArray();
		}else if(n >= T && n < Q){
			if(n < 10d*T){
				n = Round(n/(B*100d));
				n = n/10d;
			}else{
				n = Round(n/T);
			}
			suffix = "T".toCharArray();
		}else if(n >= Q){
			if(n < 10d*Q){
				n = Round(n/(T*100d));
				n = n/10d;
			}else{
				n = Round(n/Q);
			}
			suffix = "Q".toCharArray();
		}

		res = CreateStringDecimalFromNumber(n);
		if(hasSuffix){
			res = AppendString(res, suffix);
		}
        
		return res;
	}

	public static char [] NumberToHumanReadableBinary(double n){
		char [] res, suffix;
		boolean hasSuffix;
		double Ki, Mi, Gi, Ti, Pi, Ei, Zi, Yi;

		Ki = 1024d;
		Mi = Ki*1024d;
		Gi = Mi*1024d;
		Ti = Gi*1024d;
		Pi = Ti*1024d;
		Ei = Pi*1024d;
		Zi = Ei*1024d;
		Yi = Zi*1024d;
		suffix = " ".toCharArray();

		if(n < Ki){
			hasSuffix = false;
		}else{
			hasSuffix = true;
		}

		if(n >= Ki && n < Mi){
			if(n < 10d*Ki){
				n = Round(n/(Ki/10d));
				n = n/10d;
			}else{
				n = Round(n/Ki);
			}
			suffix = "Ki".toCharArray();
		}else if(n >= Mi && n < Gi){
			if(n < 10d*Mi){
				n = Round(n/(Mi/10d));
				n = n/10d;
			}else{
				n = Round(n/Mi);
			}
			suffix = "Mi".toCharArray();
		}else if(n >= Gi && n < Ti){
			if(n < 10d*Gi){
				n = Round(n/(Gi/10d));
				n = n/10d;
			}else{
				n = Round(n/Gi);
			}
			suffix = "Gi".toCharArray();
		}else if(n >= Ti && n < Pi){
			if(n < 10d*Ti){
				n = Round(n/(Ti/10d));
				n = n/10d;
			}else{
				n = Round(n/Ti);
			}
			suffix = "Ti".toCharArray();
		}else if(n >= Pi && n < Ei){
			if(n < 10d*Pi){
				n = Round(n/(Pi/10d));
				n = n/10d;
			}else{
				n = Round(n/Pi);
			}
			suffix = "Pi".toCharArray();
		}else if(n >= Ei && n < Zi){
			if(n < 10d*Ei){
				n = Round(n/(Ei/10d));
				n = n/10d;
			}else{
				n = Round(n/Ei);
			}
			suffix = "Ei".toCharArray();
		}else if(n >= Zi && n < Yi){
			if(n < 10d*Zi){
				n = Round(n/(Zi/10d));
				n = n/10d;
			}else{
				n = Round(n/Zi);
			}
			suffix = "Zi".toCharArray();
		}else if(n >= Yi){
			if(n < 10d*Yi){
				n = Round(n/(Yi/10d));
				n = n/10d;
			}else{
				n = Round(n/Yi);
			}
			suffix = "Yi".toCharArray();
		}

		res = CreateStringDecimalFromNumber(n);
		if(hasSuffix){
			res = AppendString(res, suffix);
		}

		return res;
	}

	public static char [] NumberToHumanReadableMetric(double n){
		char [] res, suffix;
		boolean hasSuffix;
		double k, M, G, T, P, Ex, Z, Y, R, Q;

		k = 1000d;
		M = k*1000d;
		G = M*1000d;
		T = G*1000d;
		P = T*1000d;
		Ex = P*1000d;
		Z = Ex*1000d;
		Y = Z*1000d;
		R = Y*1000d;
		Q = R*1000d;
		suffix = " ".toCharArray();

		if(n < k){
			hasSuffix = false;
		}else{
			hasSuffix = true;
		}

		if(n >= k && n < M){
			if(n < 10d*k){
				n = Round(n/100d);
				n = n/10d;
			}else{
				n = Round(n/k);
			}
			suffix = "k".toCharArray();
		}else if(n >= M && n < G){
			if(n < 10d*M){
				n = Round(n/(k*100d));
				n = n/10d;
			}else{
				n = Round(n/M);
			}
			suffix = "M".toCharArray();
		}else if(n >= G && n < T){
			if(n < 10d*G){
				n = Round(n/(M*100d));
				n = n/10d;
			}else{
				n = Round(n/G);
			}
			suffix = "G".toCharArray();
		}else if(n >= T && n < P){
			if(n < 10d*T){
				n = Round(n/(G*100d));
				n = n/10d;
			}else{
				n = Round(n/T);
			}
			suffix = "T".toCharArray();
		}else if(n >= P && n < Ex){
			if(n < 10d*P){
				n = Round(n/(T*100d));
				n = n/10d;
			}else{
				n = Round(n/P);
			}
			suffix = "P".toCharArray();
		}else if(n >= Ex && n < Z){
			if(n < 10d*Ex){
				n = Round(n/(P*100d));
				n = n/10d;
			}else{
				n = Round(n/Ex);
			}
			suffix = "E".toCharArray();
		}else if(n >= Z && n < Y){
			if(n < 10d*Z){
				n = Round(n/(Ex*100d));
				n = n/10d;
			}else{
				n = Round(n/Z);
			}
			suffix = "Z".toCharArray();
		}else if(n >= Y && n < R){
			if(n < 10d*Y){
				n = Round(n/(Z*100d));
				n = n/10d;
			}else{
				n = Round(n/Y);
			}
			suffix = "Y".toCharArray();
		}else if(n >= R && n < Q){
			if(n < 10d*R){
				n = Round(n/(Y*100d));
				n = n/10d;
			}else{
				n = Round(n/R);
			}
			suffix = "R".toCharArray();
		}else if(n >= Q){
			if(n < 10d*Q){
				n = Round(n/(R*100d));
				n = n/10d;
			}else{
				n = Round(n/Q);
			}
			suffix = "Q".toCharArray();
		}

		res = CreateStringDecimalFromNumber(n);
		if(hasSuffix){
			res = AppendString(res, suffix);
		}

		return res;
	}

  public static void delete(Object object){
    // Java has garbage collection.
  }
}
