package bujo.domain

import java.time.LocalDateTime

case class Tag(val name: String)

enum TaskStatus:
  case ToDo
  case Done
  case Cancelled
  case Moved

enum Entry:
  case Note(text: String, tags: List[Tag], dateCreated: LocalDateTime)
  case Task(text: String, tags: List[Tag], due_date: Option[LocalDateTime], status: TaskStatus)
  
object Entry:
  object Note:
    def apply(text: String) =
      new Note(text, List.empty, LocalDateTime.now)
