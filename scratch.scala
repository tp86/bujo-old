val perFirstDay = 2
val fullDays = 6
val perDay = 8

val done = List(2, 6, 7, 8, 5, 6, 5, 7)
val lastDaySoFar = 2

val totalDone = done.sum + lastDaySoFar
val expected = (fullDays + 1) * perDay + perFirstDay
val percentage = totalDone.toDouble / expected

@main def echo =
  println(percentage)
