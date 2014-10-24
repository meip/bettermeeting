package extensions

import play.api.libs.json._
import play.modules.reactivemongo.json.BSONFormats.PartialFormat
import reactivemongo.bson.{BSONValue, BSONObjectID}

object BSONFormatsBM {

  implicit object BSONObjectIDFormat extends PartialFormat[BSONObjectID] {
    def partialReads: PartialFunction[JsValue, JsResult[BSONObjectID]] = {
      case JsObject(("oid", JsString(v)) +: Nil) => JsSuccess(BSONObjectID(v))
    }

    val partialWrites: PartialFunction[BSONValue, JsValue] = {
      case oid: BSONObjectID => Json.obj("oid" -> oid.stringify)
    }
  }

}