# About
- N-gram https://en.wikipedia.org/wiki/N-gram
- Markov Chain

# Setup
- Install Java 8+ JDK
- Install maven
- Install your favorite version of LaTeX (Either MikTeX or LiveTeX should work on Windows)

# Basic Execution
## Build app
- mvn test
## Running generator
- _steps..._
## Generating pdf document:
- mvn latex:latex
 - Generate LaTeX output with `mvn latex:latex`
 - requires "story.txt" in top directory (change path in `\\input{}` to change location)
 - pdf file is in target/latex/book/book.pdf

_(find mvn cmdline that runs all at once)_

# Suggested Commandline Arguments
- Word count
- Source filename 
- Destination file (and/or directory?)
- IMarkov implementation (default to MarkovBigram)
- Formatted 
 - LaTeX does its own line formatting, so maybe just always format?

# Next Tasks
- [ ] Reader reads from Internet
- [ ] implement 3-gram to get better sentences
- [ ] More unit tests
- [ ] Create `main()` that implements calls in `testGenerate()`
 - [x] half-done, methods moved out of `MarkovBigramTest`
 - [ ] implement parsing of cmdline args

# Bugs
- [ ] sometimes period appears after other punctuation
  - [ ] Create unit test demonstrating it

# Ideas:
- html: hover display words that could have been chosen and probabilities for each
- variable-length sentences (would require bayesian-type methods?)

# Questions
- should `freqs` be an instance field or input/returned making the class "pure functional" with no stored state?

----

# Done
- [x] better determination of proper nouns
- [x] move docs to separate directory 
- [x] rename "First" to MarkovBigram
- [x] have `store()` return `freqs` object instead of requiring it to be passed in
 - Made `freqs` an instance field so it's not passed around
- [x] find lib to do word-wrap
 - Good old Apache Commons
- [x] LaTeX output
 - generates PDF file programmatically easier than using some pdf library,
but the downside is it requires installation of a LaTeX distribution
