package com.outr.nextui.desktop

import javafx.application.Application
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage

import com.outr.nextui.{Button, Component, Peer, UI, UIImplementation}
import pl.metastack.metarx.Sub

abstract class JavaFX extends JavaFXContainer with UI with UIImplementation {
  UIImplementation.instance = Some(this)

  override def component: Component = this

  val width: Sub[Double] = Sub[Double](800.0)
  val height: Sub[Double] = Sub[Double](600.0)

  def main(args: Array[String]): Unit = {
    println("Starting JavaFX...")
    JavaFXApplication.prepare(this)
    Application.launch(classOf[JavaFXApplication])
  }

  def initialize(primaryStage: Stage, application: JavaFXApplication): Unit = {
    primaryStage.setTitle(title.get)
    val scene = new Scene(peer.asInstanceOf[Parent])
    primaryStage.setScene(scene)
    allChildren.foreach { c =>
      c.peer.asInstanceOf[JavaFXComponent].init() // TODO: verify all are JavaFXComponents
    }
    width.attach { d =>
      primaryStage.setWidth(d)
    }
    height.attach { d =>
      primaryStage.setHeight(d)
    }
    primaryStage.show()
  }

  override def peerFor(component: Component): Option[Peer] = component match {
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