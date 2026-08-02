# I/x86

I/x86 is a programming language that focuses on control and performance.

## About

### Control
The language is designed on the principle of what you write is what gets executed. This means the programmer has complete control over the program: Nothing will be added; nothing will be removed; nothing will be changed.

### Performance
You can utilize all the instructions the CPU offers. After having coded a correct program using the fastest algorithm, you perform final hand-tuned changes for close to 100% theoretical performance.

### Use cases
The intended use of the language is for performance critical functions or for when complete control over the code is needed, such as for cryptography or other security related code.

The language can also be seen as an enthusiast language for those who like computers and want to get direct control of it. It is not meant as a replacement for hand-coded assembly, but rather as a tool to assist writing hand-coded assembly.


## Learn I/x86

### Video series
Check out the optimization series where various functions are optimized using I/x86.

Coming soon.

### Examples

There are many examples available here: [https://github.com/martinfjohansen/i-slash-x86/tree/main/tutorial](https://github.com/martinfjohansen/i-slash-x86/tree/main/tutorial).


## Features

Coming soon.

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














