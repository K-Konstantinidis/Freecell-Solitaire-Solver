# Freecell-Solitaire-Solver-With-Algorithms
# Purpose 
This project was creaded in order to solve any FreeCell Solitaire puzzle

# Implemented Algorithms
- Breadth 1st Search
- Depth 1st Search
- Best 1st Search
- A* Search

# Generator
The generator.c is a program written by @Ioannis Refanidis and its purpose is to generate
random FreeCell Solitaire puzzles with the highest card being number N

# How to use the Generator
compile: 
```
  gcc generator.c -o generator
```

Install GCC: https://dev.to/gamegods3/how-to-install-gcc-in-windows-10-the-easier-way-422j

If the executable file is called generator.exe run:
```Windows 
  generator.exe <filname> <id1> <id2>
```

```Where 
  <filaneme>: the prefix of the file
  <id1> <id2>: 2 numbers for the suffix of the file
```
  
e.g.
```
  generator.exe test 11 20
```
###### Will create 10 files with the names: test11.txt, test12.txt, ..., test20.txt

## Example of a test file
 
```test1.txt
C2 C1 D10 C0 S10 D3 S1
S2 S6 H9 D8 C6 S3 C12
H3 C11 D2 H4 D0 D4 S12
C3 C10 H6 C5 D7 H7 D11
S0 H0 H11 S7 S11 S5
S4 D12 H5 H10 H12 D6
C7 D1 H2 S8 S9 C9
C8 C4 H1 D9 D5 H8 
```

# How to run Java Main
compile: 
```Windows
  javac Main.java
```
## Javac is not recognized as an internal or external command
- https://stackoverflow.com/questions/7709041/javac-is-not-recognized-as-an-internal-or-external-command-operable-program-or
- https://www.javatpoint.com/javac-is-not-recognized

Then run: 
```Windows 
  javac Main <method> <input.txt> <solution.txt>
```

```Where 
  <method>: The algorithm used to solve the puzzle
  <input.txt>: The file with the puzzle
  <output.txt>: The file for the solution
```
  
e.g.
```
 java Main best test1.txt best1.txt
```

You could also just run the: <a href="https://github.com/K-Konstantinidis/Solitaire-Freecell-Solver-AI-With-Algorithms/blob/master/run.bat">run.bat</a> file

## Example of an output file
```
freecell S1
freecell C12
freecell S12
freecell D4
source D0
stack S3 H4
stack S5 D6
stack D4 S5
freecell D3
stack S10 D11
source C0
stack D10 S11
source C1
source C2
newstack S1
stack S3 D4
freecell H4
stack S1 D2
newstack C12
freecell S10
stack D11 C12
stack S10 D11
freecell C9
stack S9 D10
stack H8 S9
stack D5 C6
stack S8 D9
stack H2 S3
source D1
stack S1 H2
source D2
source D3
freecell S1
stack C7 H8
newstack S12
freecell D5
stack H7 S8
stack C6 D7
stack C7 D8
stack D5 C6
freecell S12
newstack H4
freecell H2
stack S3 H4
source D4
source D5
stack H2 S3
freecell S5
source D6
stack C6 H7
source D7
stack S1 H2
freecell C7
source D8
stack C7 H8
freecell H12
stack C9 H10
freecell C7
stack H8 C9
stack C7 H8
freecell S9
stack D10 C11
stack S9 D10
freecell C7
stack H8 S9
stack C7 H8
freecell C5
stack H6 C7
stack S5 H6
freecell C10
source C3
newstack S12
freecell S5
stack C5 H6
freecell S12
newstack H12
freecell C5
stack S11 H12
stack H6 S7
stack S5 H6
freecell C9
stack H10 S11
stack C9 H10
freecell S10
stack C10 D11
stack H9 C10
stack H5 S6
freecell D12
stack S4 H5
newstack S12
freecell S5
stack H6 C7
stack C5 H6
freecell S7
stack H11 S12
source H0
source S0
source S1
newstack H9
stack S10 H11
freecell C6
stack H9 C10
newstack D12
freecell H7
stack S8 H9
source D9
source H1
source H2
source C4
source C5
source C6
freecell H6
source C7
source C8
source C9
newstack S8
stack H9 S10
source C10
stack H7 S8
freecell H8
stack S9 H10
source D10
source D11
source C11
source H3
newstack S4
source C12
newstack H5
source D12
newstack S6
source S2
source S3
source S4
source H4
source S5
source H5
source H6
source S6
source H7
source S7
source H8
source H9
source S8
source S9
source H10
source S10
source S11
source H11
source H12
source S12
```
