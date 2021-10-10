package example

import scala.scalajs.js
import org.scalajs.dom.{document, window, Element}
import scala.collection.mutable.Buffer

import org.scalajs.dom.raw.HTMLTextAreaElement
import org.scalajs.dom.raw.HTMLParagraphElement

object Timer:
  private var last: Double = 0
  def tic() =
    last = window.performance.now()
  def toc() =
    f"${(window.performance.now() - last)}%.2f ms"

object Basic:
  import scala.meta.*
  import Timer.*

  def textNode(text: String) =
    val p = document.createElement("p").asInstanceOf[HTMLParagraphElement]
    p.innerHTML = text
    document.body.appendChild(p)
    p

  def traverseSimple(elt: Element, t: Tree) =
    val buff = Buffer[String]()
    var tabIndex = 0
    def indent = "&ensp;" * tabIndex
    val traverser = new Traverser:
      override def apply(t: Tree) = t match
        case d: Defn.Def =>
          buff.append(s"${indent}DEFINITION: ${d.name}")
          tabIndex += 1
          super.apply(d)
          tabIndex -= 1

        case node =>
          buff.append(s"${indent}${node.productPrefix.toString}: \"${node.toString}\"")
          super.apply(node)

    traverser(t)
    elt.innerHTML = buff.mkString("<br>")

  def run() =
    def updateListener(element: Element, display: Element) =
      val src = element.asInstanceOf[HTMLTextAreaElement]
      try {
        val tree = dialects.Scala3(src.value).parse[Source].get
        traverseSimple(display, tree)
      } catch {
        case e: Exception => ()
      }

    def addListener(query: String) =
      val textArea = document.getElementById(query).asInstanceOf[HTMLTextAreaElement]
      val display = textNode("")
      tic()
      textArea.addEventListener("input", _ => updateListener(textArea, display))
      println(toc())
      updateListener(textArea, display)

    addListener("exprSource")
    addListener("programSource")

object Main extends App {
  document.addEventListener(
    "DOMContentLoaded",
    _ => Basic.run()
  )
}
