package controllers

import org.specs2.mutable.Specification

trait ApiTest extends Specification {

  val apiPrefix = "/api"

  def endPoint(): String


  def apiUrl = apiPrefix + "/" + endPoint()

}
