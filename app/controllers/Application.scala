package controllers

import play.api._
import play.api.mvc._

import javax.inject.Inject

import play.api.db.DB
import play.api.db.Database
import play.api.db.DBApi

//import play.api.db._

import anorm.SqlParser._
import anorm._

/**
  * Created by Leon on 2016/10/2.
  */
class Application @Inject()(db: Database, dbapi: DBApi) extends Controller {

  def about = Action {
    Ok(views.html.about())
  }

  def category() = Action {
    //TODO
    val x = List(Category("test"), Category("a1"), Category("c3"))
    Ok(views.html.category(x))
  }

  def list(q: Option[String], category: Option[String]) = Action {
    import anorm.{Macro, RowParser}
    db.withConnection { implicit conn =>
      val parser: RowParser[Liwei] = Macro.indexedParser[Liwei]
      q match {
        case Some(value) =>
          val result: List[Liwei] = SQL("select * from t9a where name like {name} or ename like {ename}")
            .on("name" -> ("%" + value + "%"), "ename" -> ("%" + value + "%")).as(parser.*)
          Ok(views.html.list(result.map { x => repack(x) }))
        case None =>
          val result: List[Liwei] = SQL"select * from t9a ".as(parser.*)
          //result.foreach(println)
          Ok(views.html.list(result.map { x => repack(x) }))
      }
    }
  }

  def profile(name: String) = Action {
    import anorm.{Macro, RowParser}
    db.withConnection { implicit conn =>
      val parser: RowParser[Liwei] = Macro.indexedParser[Liwei]
      val x: Liwei = SQL(
        """
           select * from t9a where name={name} or ename={ename}
        """).on("name" -> name,"ename" -> name).as(parser.single)
      Ok(views.html.profile(repack(x)))
    }
  }

  def repack(x: Liwei) = {
    import java.net.URLDecoder
    val email = if (x.email contains "不提供") {
      ""
    } else {
      x.email
    }
    var facebook2 = ""
    var facebook = x.facebook
    facebook = URLDecoder.decode(facebook, "utf8")
    if (facebook.startsWith("\"")) {
      facebook = facebook.substring(1)
    }
    if (facebook.endsWith("\"")) {
      facebook = facebook.substring(1, facebook.length)
    }
    if (facebook.lastIndexOf("/https://") > -1) {
      facebook2 = facebook.substring(facebook.lastIndexOf("/https://") + 1)
      facebook = facebook.substring(0, facebook.lastIndexOf("/https://"))
    }
    facebook2 = facebook2.replace("?fref=ts", "")
    facebook = facebook.replace("?fref=ts", "")
    facebook2 = facebook2.replace("&fref=ts", "")
    facebook = facebook.replace("&fref=ts", "")

    var lineid2 = ""
    var lineid = x.lineid
    if (lineid.lastIndexOf("/") > -1) {
      lineid2 = lineid.substring(lineid.lastIndexOf("/") + 1)
      lineid = lineid.substring(0, lineid.lastIndexOf("/"))
    }

    val labtel = x.labtel.replace("國會辦公室：","");

    val wiki = URLDecoder.decode(x.wiki, "utf8")
    LiweiX(x.term, x.name, x.ename, x.sex, x.party, x.partygroup, x.areaname, x.district, email, x.committee, x.onboarddate, x.degree, x.profession, x.experience, x.alltel, labtel, x.servicetel1, x.servicetel2, x.servicetel3, x.servicetel4, x.servicetel5, x.labfax, x.servicefax1, x.servicefax2, x.servicefax3, x.servicefax4, x.servicefax5, x.picurl, x.leavedate, x.alladdr, x.labaddr, x.serviceaddr1, x.serviceaddr2, x.serviceaddr3, x.serviceaddr4, x.serviceaddr5, facebook, wiki, lineid, facebook2, lineid2)
  }

  def index = Action {
    import anorm.{Macro, RowParser}
    db.withConnection { implicit conn =>
      val parser: RowParser[Liwei] = Macro.indexedParser[Liwei]
      val result: List[Liwei] = SQL"select * from t9a order by rand() limit 4".as(parser.*)
      Ok(views.html.index(result))
    }
  }
}

case class Liwei(term: String, name: String, ename: String, sex: String, party: String, partygroup: String, areaname: String, district: String, email: String, committee: String, onboarddate: String, degree: String, profession: String, experience: String, alltel: String, labtel: String, servicetel1: String, servicetel2: String, servicetel3: String, servicetel4: String, servicetel5: String, labfax: String, servicefax1: String, servicefax2: String, servicefax3: String, servicefax4: String, servicefax5: String, picurl: String, leavedate: String, alladdr: String, labaddr: String, serviceaddr1: String, serviceaddr2: String, serviceaddr3: String, serviceaddr4: String, serviceaddr5: String, facebook: String, wiki: String, lineid: String)

case class LiweiX(term: String, name: String, ename: String, sex: String, party: String, partygroup: String, areaname: String, district: String, email: String, committee: String, onboarddate: String, degree: String, profession: String, experience: String, alltel: String, labtel: String, servicetel1: String, servicetel2: String, servicetel3: String, servicetel4: String, servicetel5: String, labfax: String, servicefax1: String, servicefax2: String, servicefax3: String, servicefax4: String, servicefax5: String, picurl: String, leavedate: String, alladdr: String, labaddr: String, serviceaddr1: String, serviceaddr2: String, serviceaddr3: String, serviceaddr4: String, serviceaddr5: String, facebook: String, wiki: String, lineid: String, facebook2: String, lineid2: String)

case class Category(name: String, quantity: Int = 0)

case class Tag(name: String, quantity: Int)