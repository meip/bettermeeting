package extensions

import play.api.libs.json._
import play.modules.reactivemongo.json.BSONFormats.PartialFormat
import reactivemongo.bson.{BSONValue, BSONObjectID}

import scala.util.Try

object BSONFormatsBM {

  implicit object BSONObjectIDFormat extends PartialFormat[BSONObjectID] {
    def partialReads: PartialFunction[JsValue, JsResult[BSONObjectID]] = {
      case JsObject(("oid", JsString(v)) +: Nil) =>
        val maybeOID: Try[BSONObjectID] = BSONObjectID.parse(v)
        if(maybeOID.isSuccess) JsSuccess(maybeOID.get) else {
          JsError("Expected BSONObjectID as JsString in field oid")
        }
    }

    val partialWrites: PartialFunction[BSONValue, JsValue] = {
      case oid: BSONObjectID => Json.obj("oid" -> oid.stringify)
    }
  }

}