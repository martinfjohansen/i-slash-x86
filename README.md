# I/x86
I/x86 is a programming language that focuses on control and performance.


## About

### Control
The language is designed on the principle of what you write is what gets executed. This means the programmer has complete control over the program: Nothing will be added; nothing will be removed; nothing will be changed.

### Performance
You can utilize all the instructions the CPU offers. After having coded a correct program using the fastest algorithm, you perform final hand-tuned changes for close to 100% theoretical performance.

## Use cases
The intended use of the language is for performance critical functions or for when complete control over the code is needed, such as for cryptography or other security related code.

The language can also be seen as an enthusiast language for those who like computers and want to get direct control of it. It is not meant as a replacement for hand-coded assembly, but rather as a tool to assist writing hand-coded assembly.


## Learn I/x86

### Video series
Check out the optimization series where various functions are optimized using I/x86.

Coming soon.

### Examples

There are many examples available here: [https://github.com/martinfjohansen/i-slash-x86/tree/main/tutorial](https://github.com/martinfjohansen/i-slash-x86/tree/main/tutorial).


## Features

### All instructions of the x86
All instructions of the x86 will gradually become available.

### Type detection and checking
The type of the instruction used is detected based on the types of the variables passed to it.

Types and instructions are checked for validity.

### Immediate extraction
Immediates are extracted into separate instructions.

### Labels
Labels for control flow is computed.

### Expressions
Various expression types can be used to generate instruction sequences. These include arithmetic, bitwise, boolean-relational and declaration expressions.


## Language design

### Variables: Input, Output and Working
Variables are a source of complexity in assembly programming, this has been greatly simplified in I/x86 without losing much functionality.

All variables a function uses is placed in a structure. This is called the function structure. A function structure is created before a function is called. Then, the input variables are set, the structure is passed as the single argument to the function. The function uses the working variables to do its thing, including calling other functions. After the function is done, the output variables can be read from the function structure.

All the function structures are exported as C structures, allowing the functions to be easily called from C.

### Arrays and Structures
Both arrays and structures are pointers and not inlined. This simplifies the code. If a proper allocator is used, it should not affect the speed much.

### Instruction Sequences
Plain I/x86 is simply a sequence of instructions. Expressions are separated out as its own thing. The programmer can choose to only write instructions, or, if it simplifies coding, choose to use an expression to generate the instruction sequences for him.

### Instructions: Ordinary and Polyfill
Instructions in I/x86 can be a wrapper around a native x86 instruction, or it can fill in gaps int he x86 instruction set. The latter are called Polyfill instructions. For the best performance and control, focus on ordinary instructions, but for convenience, you can choose to use polyfill instructions.

### Single-typed Instructions
All instruictions are implemented as single-type instructions. For example, a division instruction takes the same type for all three variables. If there is a need to divide a signed 64-bit integer by an unsigned 8 bit integer, then convert one of them first.

### Type Names
A type name uses the following convention.

First, a single letter denotes the main type:

 * `u`: unsigned integer, two's compliment
 * `s`: signed integer, two's compliment
 * `f`: floating point, IEEE 754 floating point number
 * `b`: bit array
 
Second, the amount of bits used:

 * 1: Only one bit is used.
 * 8: 8 bit, and so on.
 * 16
 * 32
 * 64
 * Future: 128, 256, 512.

Thirdly, an optional multi-type:

 * x2, x4, x8, x16, x32 and so on.
 
Fourthly, the postfix `a`, meaning that the variable is an array of the preceeding type.

For example:

 * u16x8 -- A SIMD-type with 8 u16 variables.
 * b16 -- 16-bits interpreted as 16-bits and nothing else.
 * s32a -- An array of s32 numbers.
 * b1 -- A single bit, only the first bit of an 8-bit type is used.

### Reinterpretations
A variable can be reinterpreted if it has the same number of bits as another type. This is not a  conversion, as the bits are not changed. However, it is useful for type-checking and understandability.

To reinterpret from a s16 to a u16, first reinterpret the variable as a b16. This makes it clear what is going on.

A reinterpretation instruction is named `Xt`, where `t` is the name of a type. For example `Xb16` reinterprets a variable as a `b16`.

### Conversions
If we have an f32 and want a u8, we need to convert it.

A conversion instruction is always named `atob` where `a` and `b` are type names. For example, `f32tos64` converts from an `f32` to an `s64`.

There are a number of complexities when it comes to conversions. In this language, the following have been chosen as the default:

 * Total: A conversion is total if all values of the source can be represented in the destination.
 * Clamping: When converting between integer types, a value must be clamped if it is too high or too low of the target type. This is also called saturation. For example, the value -50 is clamped to 0 when converted to an unsigned type.
 * Truncation: If a source value has too much precesion for the destination, the value is truncated. For example, the value 1.123456789012345 is truncated to 1.123456 when converted from a double precision integer to a single precision integer.
 * Indefinate integer: When converting from an IEEE 754 floating point number to an integer, and the value is too high, too low or a special value, then the result is set to the indefinate integer value. It has a one if the most significant bit and zero in the rest. Examples include too high, too low, `Inf` and `NaN`.
  * Infinities: When converting from an IEEE 754 floating point number to another one, and the value is too high or too low, then the result is the corresponding infinity: `+Inf` if too large and `-Inf` if too low.
 
If rounding or modulus is desired, then those must be applied before the conversion.

## Calling Functions of Other Calling Conversions
To call functions of other calling conventions, use a wrapper with a function structure and call the other function from, for example, C.


## Other ISAs
There are also plans for similar langiuages, I/arm, I/riscv and I/power. The suite of languages are called I/x.


## Roadmap

### v0.1 

### Types

The following types can be used:

 * Unsigned integer types: u8, u16, u32, u64
 * Signed integer types: s8, s16, s32, s64
 * Floating point types: f32, f64
 * SIMD-types: u16x8, s16x8, b16x8
 * Binary types: b1, b8, b16, b32, b64
 * Array types for all of the above.
 * Structures will all of the above.

### Instructions

 * Basic: Bgs, Ens, Fnc, Ret, If, Else, Endb, Loop
 * Numeric: Add, Mov.i, Mov.m, Mul, Div, Mod, Abs, Lt, Lte, Gt, Gte, Eq, Neq, Inc, Dec, Min, Max, Sqrt
 * Boolean: Mov.i, Mov.m, And, Or, Not, Shiftleft, Shiftright, Eq, Neq, If, Xor
 * Arrays: Idr.mm, Idr.mi, Idw.mm, Idw.im, Idro.mmm, Idro.mmi

### Expression types

Use the following expression types to automate the coding of assembly:

 * Arithmetic: `a`
 * Bitwise: `bw`
 * Boolean: `b`
 * Declaration: `decl`














