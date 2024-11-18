package fr.cytech.icc.rooms

import sttp.model.StatusCode
import sttp.tapir.*
import sttp.tapir.json.zio.*
import sttp.tapir.generic.auto.*
import zio.json.{JsonDecoder, JsonEncoder}

object endpoints {
  case class RoomInput(name: String) derives JsonDecoder, JsonEncoder

  val listRooms: Endpoint[Unit, Unit, Unit, List[Room], Any] =
    endpoint.get
      .in("rooms")
      .out(jsonBody[List[Room]] and statusCode(StatusCode.Ok))

  val getRoom: Endpoint[Unit, String, StatusCode, Room, Any] = endpoint.get
    .in("rooms" / path[String]("roomId"))
    .out(jsonBody[Room] and statusCode(StatusCode.Ok))
    .errorOut(statusCode)

  val createRoom: Endpoint[Unit, RoomInput, StatusCode, Room, Any] =
    endpoint.post
      .in("rooms")
      .in(jsonBody[RoomInput])
      .out(jsonBody[Room] and statusCode(StatusCode.Created))
      .errorOut(statusCode)

  val patchRoomName: Endpoint[Unit, (String, RoomInput), StatusCode, Room, Any] =
    endpoint.patch
      .in("rooms" / path[String]("roomId"))
      .in(jsonBody[RoomInput])
      .out(jsonBody[Room] and statusCode(StatusCode.Created))
      .errorOut(statusCode)

  val deleteRoom: Endpoint[Unit, String, StatusCode, Unit, Any] =
    endpoint.delete
      .in("rooms" / path[String]("roomId"))
      .out(statusCode(StatusCode.NoContent))
      .errorOut(statusCode)
}
