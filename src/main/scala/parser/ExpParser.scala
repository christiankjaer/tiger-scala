package parser

import cats.parse.{Parser => P, Parser0 => P0}

import syntax.*
import Lexing.*
import cats.parse.Caret

object ExpParser {

  val exp = token(P.caret.with1 ~ identifier).map { case (pos, x) =>
    Exp.VarE(Var.Simple(x, pos))
  }
}
