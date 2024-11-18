package fr.cytech.icc.rooms

import sttp.model.StatusCode
import sttp.tapir.*
import sttp.tapir.ztapir.*
import zio.ZIO

import scala.collection.{SortedSet, mutable}

case class RoomController(private val rooms: mutable.Map[String, Room]) {
  private val listLogic: ZServerEndpoint[Any, Any] = endpoints.listRooms.zServerLogic { _ =>
    ZIO.succeed(rooms.values.toList)
  }

  private val getLogic: ZServerEndpoint[Any, Any] = endpoints.getRoom.zServerLogic { name =>
    ZIO.getOrFailWith(StatusCode.NotFound)(rooms.get(name))
  }

  private val createLogic: ZServerEndpoint[Any, Any] = endpoints.createRoom.zServerLogic { input =>
    val room = Room(input.name, SortedSet.empty)
    rooms.addOne(input.name -> room)
    ZIO.succeed(room)
  }

  private val updateLogic: ZServerEndpoint[Any, Any] = endpoints.patchRoomName.zServerLogic { (name, input) =>
    rooms.get(name) match {
      case Some(room) =>
        val newRoom = room.copy(name = input.name)
        rooms.remove(name)
        rooms.addOne(input.name, newRoom)
        ZIO.succeed(newRoom)
      case None =>
        ZIO.fail(StatusCode.NotFound)
    }
  }

  private val deleteLogic: ZServerEndpoint[Any, Any] = endpoints.deleteRoom.zServerLogic { name =>
    if rooms.contains(name) then
      ZIO.succeed {
        rooms.remove(name)
      }
    else ZIO.fail(StatusCode.NotFound)
  }

  val serverEndpoints: List[ZServerEndpoint[Any, Any]] = List(
    listLogic,
    getLogic,
    createLogic,
    updateLogic,
    deleteLogic
  )
}
