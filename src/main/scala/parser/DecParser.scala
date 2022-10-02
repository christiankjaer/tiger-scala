package parser

import cats.parse.{Parser => P, Parser0 => P0}

import syntax.*
import Lexing.*
import cats.parse.Caret

object DecParser {

  type LocalDec = FunDec[Caret] | TyDec[Caret] | VarDec[Caret]

  def toDec(ld: LocalDec): Dec[Caret] =
    ld match {
      case x: FunDec[Caret] => Dec.Fun(List(x))
      case x: VarDec[Caret] => Dec.Var(x)
      case x: TyDec[Caret]  => Dec.Type(List(x))
    }

  val tyDec: P[LocalDec] =
    (token(P.caret.with1 <* P.string("type")) ~
      token(identifier) ~
      (token(P.char('=')) *> TypeParser.ty)).map { case ((pos, id), t) =>
      TyDec(id, t, pos)
    }

  val varDec: P[LocalDec] =
    ((token(P.caret.with1 <* P.string("var"))) ~
      token(identifier) ~
      (token(P.char(':')).soft *> (P.caret.with1 ~ token(identifier))).? ~
      (token(P.string(":=")) *> ExpParser.exp)).map {
      case (((pos, id), res), exp) =>
        VarDec(id, res.map(_.swap), exp, pos)
    }

  val funDec: P[LocalDec] =
    ((token(P.caret.with1 <* P.string("function"))) ~
      token(identifier) ~
      (token(P.char('(')) *> TypeParser.tyFields <* token(P.char(')'))) ~
      (token(P.char(':')) *> (P.caret.with1 ~ token(identifier))).? ~
      (token(P.char('=')) *> ExpParser.exp)).map {
      case ((((pos, id), tys), res), exp) =>
        FunDec(id, tys, res.map(_.swap), exp, pos)
    }

  def groupAdjacent(xs: List[LocalDec]): List[Dec[Caret]] = {
    def go(current: Dec[Caret], ds: List[LocalDec]): List[Dec[Caret]] =
      (current, ds) match {
        case (c, Nil) => List(c)
        case (x: Dec.Fun[Caret], (fd: FunDec[Caret]) :: xs) =>
          go(x.copy(fs = x.fs :+ fd), xs)
        case (x: Dec.Type[Caret], (td: TyDec[Caret]) :: xs) =>
          go(x.copy(tys = x.tys :+ td), xs)
        case (c, (x :: xs)) => c :: go(toDec(x), xs)
      }

    xs match {
      case Nil         => Nil
      case dec :: rest => go(toDec(dec), rest)
    }
  }

  val decs: P0[List[Dec[Caret]]] =
    (tyDec.backtrack | funDec.backtrack | varDec).rep0.map(groupAdjacent)

}
