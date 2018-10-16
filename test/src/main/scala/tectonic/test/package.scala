/*
 * Copyright 2014–2018 SlamData Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tectonic

import org.specs2.matcher.{Matcher, MatchersImplicits}

import tectonic.json.AsyncParser

import scala.StringContext
import scala.util.{Left, Right}

import java.lang.String

package object test {
  private object MatchersImplicits extends MatchersImplicits

  import MatchersImplicits._

  def parseRowAs(expected: Event*): Matcher[String] =
    parseAs(expected :+ Event.FinishRow: _*)

  def parseAs(expected: Event*): Matcher[String] = { input: String =>
    val parser = AsyncParser(new ReifiedTerminalPlate, AsyncParser.ValueStream)

    (parser.absorb(input), parser.finish()) match {
      case (Right(init), Right(tail)) =>
        val results = init ++ tail
        (results == expected.toList, s"expected $expected and got $results")

      case (Left(err), _) =>
        (false, s"failed to parse with error '${err.getMessage}' at ${err.line}:${err.col} (i=${err.index})")

      case (_, Left(err)) =>
        (false, s"failed to parse with error '${err.getMessage}' at ${err.line}:${err.col} (i=${err.index})")
    }
  }
}