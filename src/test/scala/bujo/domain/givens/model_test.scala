package bujo.domain.givens

import bujo.domain.Note

given print_saver: (Note => Unit) = note =>
  println(s"Saving note with text ${note.text}")
  println("Saved")

given saver: (Note => Unit) = _ => ()
