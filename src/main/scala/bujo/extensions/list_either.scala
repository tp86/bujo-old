package bujo.extensions

extension [A, B](le: Seq[Either[A, B]])
  def sequenceLeft(combineRight: (B, B) => B): Either[Seq[A], B] =
    le match
      case s if s.isEmpty => Left(Vector.empty)
      case Seq(e, es*) => {
        val acc: Either[Seq[A], B] = e match
          case Left(l) => Left(Vector(l))
          case Right(r) => Right(r)
        es.foldLeft(acc) {(a: Either[Seq[A], B], b: Either[A, B]) => (a, b) match
          case (Right(r1), Right(r2)) => Right(combineRight(r1, r2))
          case (l@Left(_), Right(_)) => l
          case (Right(_), Left(l)) => Left(Vector(l))
          case (Left(ls), Left(l)) => Left(ls.appended(l))
        }
      }
  
  def sequenceLeft: Either[Seq[A], B] = le sequenceLeft((r, _) => r)
