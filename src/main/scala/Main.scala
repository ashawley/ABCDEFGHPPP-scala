/**
 * Main.scala --- Command-line application
 *
 * Copyright (C) 2018-2019  Aaron S. Hawley
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
import scala.PartialFunction
import scala.StringContext
import scala.Unit
import scala.util.Try

import scala.Predef.augmentString
import scala.Predef.genericArrayOps
import scala.Predef.String

import java.lang.NumberFormatException

/**
 * Command-line driver of [[ABCDEFGHPPP]] problem.
 */
object Main {

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
    * @throws java.lang.IllegalArgumentException 
    *         For radix argument to [[ABCDEFGHPPP.solve]]
    */
  def main(args: Array[String]): Unit = {
    val radix = args.headOption.getOrElse("10").toInt
    val (solns1, solns2) = ABCDEFGHPPP.solve(radix).duplicate
    solns1.foreach(output(radix)(_))
    val n = solns2.size
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
          ABCDEFGHPPP.solve(radix)
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
        ABCDEFGHPPP.solve(radix).foreach {
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
    * @throws java.lang.IllegalArgumentException 
    *         For radix argument to [[ABCDEFGHPPP.solve]]
    */
  def require2(args: Array[String]): Unit = {
    args.headOption
      .filter(_.trim.nonEmpty)
      .map { arg: String =>
        Try(arg.toInt)
          .recover {
            case _: NumberFormatException =>
              Console.err.println(
                s"Error: Failed to convert '$arg' to integer"
              )
          }
          .foreach {
            case radix: Int =>
              ABCDEFGHPPP.solve(radix).foreach {
                output(radix)(_)
              }
          }
      }
      .getOrElse {
        Console.err.println("Error: expected argument")
      }
  }
}
