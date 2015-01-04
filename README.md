Untrue 
======
UNlimited Term RedUcEr  
This is an unbounded parallel graph rewriter engine for a lisp-like language. The idea is simple:
each computational "clock", everything that is ready to be rewritten, will be rewritten, e.g.  
	(+ (+ 6 2) (* 3 4))  
will be evaluated in 2 clock cycles.  
The worker graph is "floating in vacuum" and each tick nodes of the graph can request to be reduced 
either by a beta reduction (access to memory) or a delta reduction (ALU operation on literals). After 
the evaluation you can measure memory accesses and stuff, to get the idea of your algorithms memory bandwidths.

Status
======
This project was started just a while ago and reduction is still not working :) 

Why?
======
I wanted to know how much local parallelism different programs have, how big memory bandwidth they 
generate and how much functional units they require. Of course you can do such analysis per hand, but
I like to experiment with things :)  
In the long run I would like to experiment on:
* scheduling in limited resource environment
* using unique/linear types
* implementing new paradigms for highly parallel evaluators#

TODO
======
* ready flag propagation should be implemented (currently (+ 1 2) is not ready)
* currying/partial application (how to handle a funcall, if it is another list and without full 
argument list). 


License
======
Attribution-NonCommercial 2.0 Generic (CC BY-NC 2.0)