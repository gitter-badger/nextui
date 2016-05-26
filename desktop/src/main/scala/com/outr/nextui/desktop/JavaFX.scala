package com.outr.nextui.desktop

import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage

import com.outr.nextui._
import com.outr.scribe.Logging

import scala.language.postfixOps

trait JavaFX extends JavaFXContainer with UIImplementation with Logging {
  this: UI =>

  override def component: Component = this

  def main(args: Array[String]): Unit = {
    logger.info("Starting JavaFX...")
    JavaFXApplication.prepare(this)
    Application.launch(classOf[JavaFXApplication])
  }

  def initialize(primaryStage: Stage, application: JavaFXApplication): Unit = {
    primaryStage.setTitle(title.get)
    val scene = new Scene(impl, width.pref.get, height.pref.get)
    primaryStage.setScene(scene)
    allChildren.map(_.peer).foreach {
      case jfx: JavaFXComponent => jfx.init()
      case c => throw new RuntimeException(s"Component peer is not a JavaFXComponent: $c.")
    }
    doubleBind(width.pref, primaryStage.setWidth, primaryStage.widthProperty(), scene.getX * 2.0, silent = true)
    doubleBind(height.pref, primaryStage.setHeight, primaryStage.heightProperty(), scene.getY, silent = true)
    width.pref.attach(width._actual :=)
    height.pref.attach(height._actual :=)

    primaryStage.show()
  }

  override def peerFor(component: Component): Option[Peer[_]] = component match {
    case b: Button => Some(new JavaFXButton(b))
    case fx: JavaFX => Some(fx)
    case _ => None
  }
}

class JavaFXApplication extends Application {
  override def start(primaryStage: Stage): Unit = {
    val ui = JavaFXApplication.use()
    ui.initialize(primaryStage, this)
  }
}

object JavaFXApplication {
  private var instance: Option[JavaFX] = None

  def use(): JavaFX = synchronized {
    instance match {
      case Some(ui) => {
        instance = None
        ui
      }
      case None => throw new RuntimeException("No UI defined for JavaFX.")
    }
  }

  def prepare(ui: JavaFX): Unit = synchronized {
    instance match {
      case Some(existing) => throw new RuntimeException(s"Cannot define multiple UIs before initialization. Already defined: $existing, Trying to define: $ui.")
      case None => instance = Some(ui)
    }
  }
}