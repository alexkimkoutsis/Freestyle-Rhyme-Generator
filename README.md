# Freestyle Rhyme Project

This program is designed to function as a rhyme generator.
It was created to aid in freestyle rapping practice, so that users
could focus on perfecting their flow and sentence/context generation abilities
without having to worry about coming up with rhymes at the same time.
It can also be used by more experienced freestyle rappers to simply expand
their rhyming vocabulary and encourage use of words that they normally wouldn't use.

# Demo

![Rhyme Generator Demo](img/rhyme generator demo.gif)

# How to Use

* This program begins by asking how many rhymes the user would like to receive
per input word. 
 
* Then, the user may input any standard English word and the program will return words that rhyme with it. If in the first step, the user specified that they wanted
x rhymes per input word, then x rhymes would be printed.

* The program will then prompt the user for an input word again.

* The program will keep prompting the user for input words until they input "!",
at which point the program will terminate.

Note that if the user inputs any non-standard English words, no rhymes will be generated,
and there can be no spaces in the input word.

# Built With
* [Datamuse API](https://www.datamuse.com/api/) - For rhyme generation.
* [org.json Maven Repository](https://mvnrepository.com/artifact/org.json/json) - To assist in parsing JSON data.
