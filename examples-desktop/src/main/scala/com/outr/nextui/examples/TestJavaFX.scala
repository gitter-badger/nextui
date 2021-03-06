package com.outr.nextui.examples

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.{Background, BackgroundFill, Pane}
import javafx.scene.paint.Color
import javafx.stage.Stage

object TestJavaFX {
  def main(args: Array[String]): Unit = {
    Application.launch(classOf[TestJavaFX])
  }
}

class TestJavaFX extends Application {
  override def start(primaryStage: Stage): Unit = {
    primaryStage.setTitle("Test JavaFX")

    val stackPane = new Pane
    val fill = new BackgroundFill(Color.RED, null, null)
    stackPane.setBackground(new Background(fill))
    val imgView = new ImageView()
    val img = new Image("tucker.jpg")
    imgView.setImage(img)
    stackPane.getChildren.add(imgView)

    val scene = new Scene(stackPane)
    primaryStage.setScene(scene)
    primaryStage.show()
  }
}