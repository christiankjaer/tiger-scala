type Symbol = String
enum Var[A] {
  case Simple(x: Symbol, ann: A)
  case Field(v: Var[A], x: Symbol, ann: A)
  case Subscript(v: Var[A], e: Exp[A], ann: A)
}

final case class RecordField[A](key: Symbol, v: Exp[A], ann: A)
final case class Field[A](name: Symbol, typ: Symbol, ann: A)
final case class FunDec[A](
    name: Symbol,
    params: List[Field[A]],
    result: Option[(Symbol, A)],
    body: Exp[A],
    ann: A
)

enum Exp[A] {
  case VarE(v: Var[A])
  case NilLit(ann: A)
  case IntLit(n: Int, ann: A)
  case StringLit(s: String, ann: A)
  case Call(func: Symbol, args: List[Exp[A]], ann: A)
  case Op(left: Exp[A], op: Oper, right: Exp[A], ann: A)
  case Record(fields: List[RecordField[A]], typ: Symbol, ann: A)
  case SeqE(elems: List[(Exp[A], A)])
  case Assign(v: Var[A], e: Exp[A], ann: A)
  case If(test: Exp[A], b1: Exp[A], b2: Option[Exp[A]], ann: A)
  case While(test: Exp[A], body: Exp[A], ann: A)
  case For(v: Symbol, lo: Exp[A], hi: Exp[A], body: Exp[A], ann: A)
  case Break(ann: A)
  case Let(decs: List[Dec[A]], body: Exp[A], ann: A)
  case ArrayE(typ: Symbol, size: Exp[A], init: Exp[A], ann: A)
}

enum Dec[A] {
  case Function(fs: List[FunDec[A]])
  case VarDec(name: Symbol, typ: Option[(Symbol, A)], init: Exp[A], ann: A)
  case TypeDec(tys: List[(Symbol, Ty[A], A)])
}

enum Ty[A] {
  case Name(n: Symbol, ann: A)
  case Record(fields: List[Field[A]])
  case ArrayTy(s: Symbol, ann: A)
}

enum Oper {
  case Plus, Minus, Times, Divide, Eq, Neq, Lt, Le, Gt, Ge
}
