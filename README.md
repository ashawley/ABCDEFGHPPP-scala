ABCDEFGHPPP problem in Scala
============================

Problem: (_AB_ − _CD_ = _EF_) + _GH_ = _PPP_

       AB
     - CD
    -----
       EF
     + GH
    -----
      PPP

The values for _A_, _B_, _C_, _D_, _E_, _F_, _G_, _H_, and _P_
are distinct and in the range of [0, 9].

```scala
ABCDEFGHPPP.solve(10) // Decimal
```

It's possible to solve for other base number systems than just
decimal.

```scala
ABCDEFGHPPP.solve(16) // Hex system
```

### Overview

This solution is written in [Scala] 2.12 and only depends on the Scala
standard library, and [Java 8].

The primary routine is called `solve`.  It can give the set of all
solutions for the problem in decimal:

```scala
ABCDEFGHPPP.solve(10).foreach(println)
(9,0,2,7,6,3,4,8,1)
(9,0,6,3,2,7,8,4,1)
(8,5,4,6,3,9,7,2,1)
(8,6,5,4,3,2,7,9,1)
(9,5,2,7,6,8,4,3,1)
```

Alternatively, an application entry point, `main`, is provided:

```scala
ABCDEFGHPPP.main(Array())
```

Gives a listing of all the solutions:

```
Solution:
   85
 - 46
-----
   39
 + 72
-----
  111

Solution:
   86
 - 54
-----
   32
 + 79
-----
  111

Solution:
   90
 - 27
-----
   63
 + 48
-----
  111

Solution:
   90
 - 63
-----
   27
 + 84
-----
  111

Solution:
   95
 - 27
-----
   68
 + 43
-----
  111

Found 5 solutions
```

### Getting started

Use [sbt](http://www.scala-sbt.org/) to interact with the build.

```
$ sbt
```

You can run Scala from the sbt console:

```
sbt> console
scala> ABCDEFGHPPP.main(Array("16"))
```

```
Solution:
    6 0
  - 2 4
-------
    3 c
  + d 5
-------
  1 1 1

Solution:
    6 0
  - 3 4
-------
    2 c
  + e 5
-------
  1 1 1

...
```

Alternatively, you can interact with program as if it was built as a
command-line application but from sbt:

```
sbt> run 10
[info] Running ABCDEFGHPPP 10
Solution:
    9 0
  - 2 7
-------
    6 3
  + 4 8
-------
  1 1 1

...

Found 5 solutions
[success] Total time: 9 s, completed Nov 2, 2018 8:24:19 PM
```

### Caveats

Passing the empty string as an argument:

```
scala> ABCDEFGHPPP.main(Array(""))
java.lang.NumberFormatException: For input string: ""
```

Passing an argument that is something other than an Integer:

```
scala> ABCDEFGHPPP.main(Array("f"))
java.lang.NumberFormatException: For input string: "f"
```

Passing an argument that isn't a valid Integer:

```
scala> Int.MaxValue
res1: Int = 2147483647

scala> ABCDEFGHPPP.main(Array("2147483648"))
java.lang.NumberFormatException: For input string: ""
```

### References

- https://github.com/mingchuno/ABCDEFGHPPP

[Java 8]: http://docs.oracle.com/javase/8/docs/api/
[sbt]: http://scala-sbt.org
[Scala]: http://scala-lang.org
