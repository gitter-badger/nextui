package com.outr.nextui

import org.powerscala.collection.HierarchicalIterator
import pl.metastack.metarx.Sub

trait UI extends Container {
  val title: Sub[String] = Sub("")
  def ui: UI = this

  def allChildren: Iterator[Component] = new HierarchicalIterator[Component](this, {
    case container: Container => container.children.iterator
    case _ => Iterator.empty
  })
}