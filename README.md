Spell Checker

Demonstrate Mastery of Trees by Generating a Spell Checker Using External Data

Input File: input.txt
Output File: output.txt

*REQUIREMENT 1:*

Read and Persist all words from Jazzy English Dictionary

Only happens at time the implementation is constructed.

File must be read properly based on the platform its running on.
(Mac, Linux, Windows)

Trie - Node: Key Value (null if not a word), String Data
    Takes a String in
    Assigns a temporary key in the order its imputed
    Key designates that is a valid word in itself
    Traverse first level referencing first letter of the string
    Then traverse to second letter in the string
    If second letter does not exist
    Create a node Key = null, insert the letters into String data
    If the current String exists as a Node during the search
    Check to see if its Key value is null, if so, assign it the key
    If all letters are checked and does not exist
    Create a new node with the key it was assigned and String Data

*REQUIREMENT 2:*

Read a config file which contains a key and a data structure Value

storage=trie
storage=tree

Indicates which data structure will store contents of englsh.0 file

Read properties file with java.util.Properties class


*REQUIREMENT 3:*

Create functions to determine if a word is a misspelling.

Function takes in word as parameter.

Returns 1-3 proposed spellings.

*REQUIREMENT 4:*

Must read file as passed in from command line.

Each entry of the input file must produce one line in the output file.
