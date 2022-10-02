package parser

import cats.parse.{Parser => P, Parser0 => P0}

import syntax.*
import Lexing.*
import cats.parse.Caret

object TypeParser {

  val tyField =
    ((token(P.caret.with1 ~ identifier) <* token(P.char(':'))) ~
      token(identifier)).map { case ((pos, id), tid) =>
      Field(id, tid, pos)
    }

  val tyFields = tyField.repSep0(P.char(','))

  val ty: P[Ty[Caret]] =
    ((token(P.caret.with1 <* P.string("array")) <* token(P.string("of"))) ~
      token(identifier)).map { case (pos, id) =>
      Ty.ArrayTy(id, pos)
    }.backtrack | token(P.caret.with1 ~ identifier).map { case (pos, id) =>
      Ty.Name(id, pos)
    }.backtrack | token(P.char('{')) *>
      tyFields.map(Ty.Record.apply) <* token(P.char('}'))

}
