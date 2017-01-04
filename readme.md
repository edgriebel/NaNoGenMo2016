# About
- N-gram https://en.wikipedia.org/wiki/N-gram
- Markov Chain

# Next
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
- html: hover display words that could have been chosen and probs
- generate PDF file programmatically
- variable-length sentences (would require bayesian-type methods?)

# Questions
- should `freqs` be an instance field or input/returned making the class "super-functional" with no stored state?

----

# Done
- [x] better determination of proper nouns
- [x] move docs to separate directory 
- [x] rename "First" to MarkovBigram
- [x] have `store()` return `freqs` object instead of requiring it to be passed in
 - Made `freqs` an instance field so it's not passed around
- [x] find lib to do word-wrap
 - Good old Apache Commons
- LaTeX output
