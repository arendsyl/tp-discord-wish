package fr.cytech.icc

import fr.cytech.icc.rooms.RoomController
import zio.{Console, Scope, ZIO, ZIOAppArgs, ZIOAppDefault}
import sttp.tapir.server.netty.zio.NettyZioServer
import sttp.tapir.server.netty.zio.NettyZioServerOptions
import sttp.tapir.ztapir.ZServerEndpoint
import zio.ExitCode

import scala.collection.mutable

object Main extends ZIOAppDefault:
  override implicit val runtime: zio.Runtime[Any] = zio.Runtime.default

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] =

    val port = sys.env.get("HTTP_PORT").flatMap(_.toIntOption).getOrElse(8080)

    val options = NettyZioServerOptions.default[Any]

    val server = NettyZioServer.apply(options).port(port)
    
    val controller = RoomController(mutable.Map.empty)

    val endpoints: List[ZServerEndpoint[Any, Any]] = 
      controller.serverEndpoints ++ Endpoints.doc
    
    for
      bind <- server.addEndpoints(endpoints).start()
      _ <- Console.printLine(s"Go to http://localhost:${bind.port}/docs to open SwaggerUI. Press ENTER key to exit.")
      _ <- Console.readLine
      _ <- bind.stop()
    yield ExitCode.success
