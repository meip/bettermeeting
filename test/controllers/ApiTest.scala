package controllers

import org.specs2.mutable.Specification

trait ApiTest extends Specification {

  val apiPrefix = "/api"

  val endPoint: String

  def apiUrl = apiPrefix + "/" + endPoint

}
