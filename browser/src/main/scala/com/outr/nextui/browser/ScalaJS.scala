package com.outr.nextui.browser

import com.outr.nextui._
import com.outr.nextui.model.{Image, ImagePeer, Resource, ResourcePeer}
import com.outr.scribe.Logging
import org.scalajs.dom.raw.HTMLImageElement
import org.scalajs.dom.{Event, document, window}

import scala.scalajs.js.JSApp

trait ScalaJS extends JSApp with ScalaJSContainer with UIImplementation with Logging {
  this: UI =>

  override val component: Component = this

  def main(): Unit = {
    logger.info("Starting ScalaJS Application...")
    initialize()
  }

  def initialize(): Unit = {
    allChildren.map(_.peer).foreach {
      case sjs: ScalaJSComponent => sjs.init()
      case c => throw new RuntimeException(s"Component peer is not a ScalaJSComponent: $c.")
    }

    impl.style.width = "100%"
    impl.style.height = "100%"

    width._actual := window.innerWidth.toDouble
    height._actual := window.innerHeight.toDouble
    window.addEventListener("resize", { (evt: Event) =>
      width._actual := window.innerWidth.toDouble
      height._actual := window.innerHeight.toDouble
    }, true)

    document.body.appendChild(impl)
  }

  override def peerFor(component: Component): Option[Peer[_]] = component match {
    case b: Button => Some(new ScalaJSButton(b))
    case i: ImageView => Some(new ScalaJSImageView(i))
    case js: ScalaJS => Some(js)
    case c: Container => Some(ScalaJSContainer(c))
    case _ => None
  }

  override def peerFor(resource: Resource): ResourcePeer = new ScalaJSResource(resource)

  override def peerFor(image: Image): ImagePeer = new ScalaJSImage(image)
}

class ScalaJSResource(resource: Resource) extends ResourcePeer

class ScalaJSImage(image: Image) extends ImagePeer {
  val element = document.createElement("img").asInstanceOf[HTMLImageElement]
  element.src = image.url.toString
}