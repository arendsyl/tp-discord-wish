package fr.cytech.icc

import fr.cytech.icc.rooms.endpoints
import sttp.tapir.*
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import sttp.tapir.ztapir.ZServerEndpoint
import zio.Task

object Endpoints {
  private val ses: List[Endpoint[?, ?, ?, ?, ?]] = List(
    endpoints.listRooms,
    endpoints.getRoom,
    endpoints.createRoom,
    endpoints.patchRoomName,
    endpoints.deleteRoom
  )

  val doc: List[ZServerEndpoint[Any, Any]] = SwaggerInterpreter()
    .fromEndpoints[Task](ses, "discord-wish", "1.0.0")
}
