/**
 * ABCDEFGHPPP.scala --- ABCDEFGHPPP problem
 *
 * Copyright (C) 2018  Aaron S. Hawley
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import scala.Array
import scala.Byte
import scala.Console
import scala.Int
import scala.Iterator
import scala.PartialFunction
import scala.StringContext
import scala.Unit
import scala.collection.immutable.Seq
import scala.util.Try

import scala.Predef.augmentString
import scala.Predef.intWrapper
import scala.Predef.genericArrayOps
import scala.Predef.String

import java.lang.IllegalArgumentException
import java.lang.NumberFormatException

/**
 * Problem: (''AB'' &minus; ''CD'' = ''EF'') + ''GH'' = ''PPP''
 *
 * <pre>
 *    AB
 *  - CD
 * -----
 *    EF
 *  + GH
 * -----
 *   PPP
 * </pre>
 *
 * The values for ''A'', ''B'', ''C'', ''D'', ''E'', ''F'', ''G'',
 * ''H'', and ''P'' are distinct and in the range of [0, 9].
 *
 * Example: {{{
 * ABCDEFGHPPP.solve(10) // Decimal
 * }}}
 *
 * It's possible to [[solve]] for other base number systems than just
 * decimal: {{{
 * ABCDEFGHPPP.solve(16) // Hex system
 * }}}
 *
 * However, a brute-force heuristic is used, so complexity severely limits
 * the capability to solve bases larger than 16.
 *
 * There is an important optimization available for filtering out
 * possible solutions based on the following assumptions about ''P'':
 *
 *  1. ''PPP'' can't be 000, by adding two 2-digit numbers.
 *  1. ''PPP'' can't be 222 or greater, by subtracting two 2-digit numbers.
 *  1. ''PPP'' can't be anything else, so PPP can only be 111.
 */
object ABCDEFGHPPP {

  /**
    * Convert list of 9 integers to a 9 integer tuple.
    *
    * Otherwise, throw a runtime error.
    *
    * @param  xs List of 9 integers
    * @return The 9 integers of the list in the same order
    * @throws java.lang.IllegalArgumentException When not exactly 9 elements
    */
  def listToTuple9(xs: Seq[Byte]): (Byte,Byte,Byte,Byte,Byte,Byte,Byte,Byte,Byte) = {
    xs match {
      case Seq(a, b, c, d, e, f, g, h, p) =>
        (a, b, c, d, e, f, g, h, p)
      case xs =>
        val msg = s"Expected 9 elements, got ${xs.size}"
        throw new IllegalArgumentException(msg)
    }
  }

  /**
    * Solve ABCDEFGHPPP for `radix` using brute-force.
    *
    * Generates all permutations of the distinct range 0 to `radix`:
    *
    * For `radix` of ''n'', tries ''n!'' / (''n'' &minus; 9)''!''
    * possible solutions.
    *
    *  1. For octal (base 8), there are zero cases of 9 values.
    *  1. For decimal (base 10), this is 3,628,800 values.
    *  1. For hex (base 16), this is 4,151,347,200 values.
    *  1. For vigesimal (base 20), this is 60,949,324,800.
    *
    * @param  radix Base number system
    * @return Nothing
    * @throws java.lang.IllegalArgumentException When radix 8 or less
    */
  def solve(radix: Int): Iterator[(Byte,Byte,Byte,Byte,Byte,Byte,Byte,Byte,Byte)] = {
    if (radix < 8) {
      throw new IllegalArgumentException(s"Radix was less than 8: $radix")
    }
    (0 to radix - 1)
      .map(_.toByte)
      .combinations(9)
      .flatMap(_.permutations)
      .map(_.toSeq)
      .map(listToTuple9)
      .filter {
        case (0, _, _, _, _, _, _, _, _)           => false
        case (_, _, 0, _, _, _, _, _, _)           => false
        case (_, _, _, _, 0, _, _, _, _)           => false
        case (_, _, _, _, _, _, 0, _, _)           => false
        // PPP can't be 000, 
        //   by adding two 2-digit numbers.
        // PPP can't be 222 or greater,
        //   by subtracting two 2-digit numbers.
        case (_, _, _, _, _, _, _, _, p) if p != 1 => false
        // PPP can't be anything else,
        //   so PPP can only be 111.
        case (a, b, c, d, e, f, g, h, p) if p == 1 => {
          val AB  = a * radix + b
          val CD  = c * radix + d
          val EF  = e * radix + f
          val GH  = g * radix + h
          val PPP = p * radix * radix +
                    p * radix + p
          (
             AB
           - CD
             ==
             EF
          )  &&  (
             EF
           + GH
             ==
            PPP
          )
        }
      }
  }

  /**
    * Display a solution in the terminal screen.
    *
    * @param  radix Base number system
    * @param  tuple 9 Integers
    * @return Nothing
    */
  def output(radix: Int): PartialFunction[(Byte,Byte,Byte,Byte,Byte,Byte,Byte,Byte,Byte),Unit] = {
    case (a, b, c, d, e, f, g, h, p) =>
      val A = java.lang.Integer.toString(a.toInt, radix)
      val B = java.lang.Integer.toString(b.toInt, radix)
      val C = java.lang.Integer.toString(c.toInt, radix)
      val D = java.lang.Integer.toString(d.toInt, radix)
      val E = java.lang.Integer.toString(e.toInt, radix)
      val F = java.lang.Integer.toString(f.toInt, radix)
      val G = java.lang.Integer.toString(g.toInt, radix)
      val H = java.lang.Integer.toString(h.toInt, radix)
      val P = java.lang.Integer.toString(p.toInt, radix)
      val solution =
        s"""|Solution:
            |    $A $B
            |  - $C $D
            |-------
            |    $E $F
            |  + $G $H
            |-------
            |  $P $P $P
            |""".stripMargin
      Console.out.println(solution)
  }

  /**
    * Solve ABCDEFGHPPP for radix parameter in first argument,
    * else use base 10.
    *
    * @param  args Arguments
    * @return Nothing
    * @throws java.lang.NumberFormatException If arg isn't an Int
    * @throws java.lang.IllegalArgumentException For radix argument to [[solve]]
    */
  def main(args: Array[String]): Unit = {
    val radix = args.headOption.getOrElse("10").toInt
    val (solns, solnsCopy) = solve(radix).duplicate
    solns.foreach(output(radix)(_))
    val n = solnsCopy.size
    if (n == 1) {
      Console.out.println("Found 1 solution")
    } else {
      Console.out.println(s"Found $n solutions")
    }
  }

  /**
    * Same as [[main]], but silently quit if failure or no argument.
    *
    * @param  args Arguments
    * @return Nothing
    */
  def main2(args: Array[String]): Unit = {
    for (arg <- args.headOption if arg.trim.nonEmpty)
      for (radix <- Try(arg.toInt).filter(9 < _).filter(_ < 22))
        yield
          solve(radix)
  }

  /**
    * Same as [[main]], but fail if no argument provided.
    *
    * @param  args Arguments
    * @return Nothing
    */
  def require(args: Array[String]): Unit = {
    args.headOption
      .filter(_.trim.nonEmpty)
      .map {
        _.toInt
      }
      .map { radix =>
        solve(radix).foreach {
          output(radix)(_)
        }
      }
      .getOrElse {
        Console.err.println("Error: expected argument")
      }
  }

  /**
    * Same as [[require]], but fail if invalid argument provided.
    *
    * @param  args Arguments
    * @return Nothing
    * @throws java.lang.IllegalArgumentException For radix argument to [[solve]]
    */
  def require2(args: Array[String]): Unit = {
    args.headOption
      .filter(_.trim.nonEmpty)
      .map { arg: String =>
        Try(arg.toInt)
          .recover {
            case _: NumberFormatException =>
              Console.err.println(s"Error: Failed to convert '$arg' to integer")
          }
          .foreach {
            case radix: Int =>
              solve(radix).foreach {
                output(radix)(_)
              }
          }
      }
      .getOrElse {
        Console.err.println("Error: expected argument")
      }
  }
}
