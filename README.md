Untrue 
======
UNlimited Term RedUcEr  
This is an unbounded parallel graph rewriter engine for a lisp-like language. The idea is simple:
each computational "clock", everything that is ready to be rewritten, will be rewritten, e.g.  
	(+ (+ 6 2) (* 3 4))  
will be evaluated in 2 clock cycles.  
The worker graph is "floating in vacuum" and each tick nodes of the graph can request to be reduced 
either by a beta reduction (access to memory) or a delta reduction (ALU operation on literals). 
After the evaluation you can measure memory accesses and stuff, to get the idea of the amount of 
used resources, like functional units or generated memory bandwidth.

Status
======
This project was started just a while ago, but following stuff is already implemented:
* parsing source first to AST and then to applicative expression graph with additional metadata
* splitting the execution in two categories: delta & beta reductions
* delta reduction (alu operations)
* some of beta reductions (conditional + list operations)
* currying

Why?
======
I wanted to know how much local parallelism different programs have, how big memory bandwidth they 
generate and how much functional units they require. Of course you can do such analysis per hand, 
but I like to experiment with things :)  
In the long run I would like to experiment on:
* scheduling in limited resource environment
* using unique/linear types
* implementing new paradigms for highly parallel evaluators

General Information
======
The language is very similar to Scheme in syntax, but differs in evaluation. Everything, which is
ready to be evaluated - will be evaluated. Also, there is no need in quotations, as list of literals
like (1 2 3 4) (which should be quoted in scheme/lisp), just evaluates as application of 1 with 
arguments 2 3 4, and it does nothing.  
Primitives overview:  
* *hd* primitive will remove all children from the expression and leave only the head
* *tl* primitive will remove head and apply next argument to all subsequent arguments. If there 
are none, void will be returned.
* *if* primitive will take a predicate, "then" and "else" expressions and will be rewritten to one 
of "then" and "else", according to the predicate
* *seq* primitive executes a series of expressions _sequentially_, returning the value of a last 
one. If any of the first expressions do return a value, this is discarded.
* *define* primitive defines a new environment entry in the current scope, which also used to bind
variables. Usage: (define (label arg1 arg2 argN) (body))

TODO
======
* proper logging
* proper error handling
* beta reduction/environment
* ready flag propagation should be implemented properly - children must notify parents after 
a successful rewrite
* statistics tracer
* remove static stuff and make it proper
* custom test runner with aggregated report generator

LOG
======
* 10.01.2015: started writing this log. Mainly for historical reasons (it is somewhat funny to read
those after a long time, reliving "the struggle" with the code :)), but also can be used to track 
the slow progress and help in context switching.

License
======
Attribution-NonCommercial 2.0 Generic (CC BY-NC 2.0)