package fr.cytech.icc.rooms

import zio.json.{JsonDecoder, JsonEncoder}

import java.time.OffsetDateTime
import java.util.UUID
import scala.collection.SortedSet

given [A: Ordering: JsonDecoder]: JsonDecoder[SortedSet[A]] = JsonDecoder.set[A].map(SortedSet.from)

case class Room(name: String, posts: SortedSet[Post]) derives JsonDecoder, JsonEncoder
case class Post(id: UUID, author: String, postedAt: OffsetDateTime, content: String) derives JsonDecoder, JsonEncoder

given Ordering[Post] = Ordering.by(_.postedAt)
