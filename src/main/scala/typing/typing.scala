package typing

type Name = String
final case class FieldTy(field: Name, ty: Ty)
enum Ty {
  case TInt, TString, TNil, TUnit
  case TRecord(fields: FieldTy)
  case TArray(ty: Ty)
  case TName(n: Name, ty: Ty)
}

enum EnvEntry {
  case Var(ty: Ty)
  case Fun(formals: List[Ty], result: Ty)
}

type TEnv = Map[Name, Ty]
type VEnv = Map[Name, EnvEntry]

val baseTEnv = Map(
  "int" -> Ty.TInt,
  "string" -> Ty.TString
)

val baseFEnv = Map(
  "print" -> EnvEntry.Fun(List(Ty.TString), Ty.TUnit),
  "flush" -> EnvEntry.Fun(List.empty, Ty.TUnit),
  "getchar" -> EnvEntry.Fun(List.empty, Ty.TString),
  "exit" -> EnvEntry.Fun(List(Ty.TInt), Ty.TUnit)
)
