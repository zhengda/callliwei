package controllers

import play.api._
import play.api.mvc._
import play.api.cache._
import scala.concurrent.duration._

import javax.inject.Inject

import play.api.db.DB
import play.api.db.Database
import play.api.db.DBApi

//import play.api.db._

import anorm.SqlParser._
import anorm._

import java.net.URLDecoder


/**
  * Created by Leon on 2016/10/2.
  */
class Application @Inject()(db: Database, dbapi: DBApi, cache: CacheApi, cached: play.api.cache.Cached)
  extends Controller {

  def category(group: String) = cached.status(_ => "category200." + group, 200, 24 * 60 * 60) {
    Action {
      implicit request =>
        import anorm.{Macro, RowParser}
        db.withConnection {
          implicit conn =>
            val parser: RowParser[Category] = Macro.indexedParser[Category]
            group match {
              case "district" =>
                val result: List[Category] = SQL("select * from district2name ").as(parser.*)
                Ok(views.html.category(result, group))
              case "profession" =>
                val result: List[Category] = SQL("select * from profession2name ").as(parser.*)
                Ok(views.html.category(result, group))
              case "party" =>
                val result: List[Category] = SQL("select * from party2name ").as(parser.*)
                Ok(views.html.category(result, group))
              case _ =>
                Ok(views.html.category(List(), group))
            }
        }
    }
  }

  def about = cached("about") {
    Action {
      Ok(views.html.about())
    }
  }

  def list(q: Option[String], group: String, category: String) = cached.status(_ => "list200." + q + "." + group + "." + category, 200, 24 * 60 * 60) {
    Action {
      implicit request =>
        import anorm.{Macro, RowParser}
        db.withConnection {
          implicit conn =>
            val parser: RowParser[Liwei] = Macro.indexedParser[Liwei]

            if (category != null) {
              val cparser: RowParser[Category] = Macro.indexedParser[Category]

              group match {
                case "district" =>
                  val cat: Category = SQL("select * from district2name where name={name}")
                    .on("name" -> category).as(cparser.single)
                  val ids: List[String] = cat.indexies.split(' ').toList
                  val result: List[Liwei] = SQL("select * from t9a where name in ({ids})")
                    .on("ids" -> ids).as(parser.*)
                  Ok(views.html.list(result.map {
                    x => repack(x)
                  }, group, category))
                case "profession" =>
                  val cat: Category = SQL("select * from profession2name where name={name}")
                    .on("name" -> category).as(cparser.single)
                  val ids: List[String] = cat.indexies.split(' ').toList
                  val result: List[Liwei] = SQL("select * from t9a where name in ({ids})")
                    .on("ids" -> ids).as(parser.*)
                  Ok(views.html.list(result.map {
                    x => repack(x)
                  }, group, category))
                case "party" =>
                  val cat: Category = SQL("select * from party2name where name={name}")
                    .on("name" -> category).as(cparser.single)
                  val ids: List[String] = cat.indexies.split(' ').toList
                  val result: List[Liwei] = SQL("select * from t9a where name in ({ids})")
                    .on("ids" -> ids).as(parser.*)
                  Ok(views.html.list(result.map {
                    x => repack(x)
                  }, group, category))
              }

            } else {

              q match {
                case Some(value) =>
                  val result: List[Liwei] = SQL("select a.* from t9a a left join t9asearch b on a.name=b.name where b.content like {q} ")
                    .on("q" -> ("%" + value + "%")).as(parser.*)
                  Ok(views.html.list(result.map {
                    x => repack(x)
                  }, null, null))
                case None =>
                  val result: List[Liwei] = SQL"select * from t9a ".as(parser.*)
                  Ok(views.html.list(result.map {
                    x => repack(x)
                  }, null, "all"))
              }
            }
        }
    }
  }


  def profile(name: String) = cached.status(_ => "profile200." + name, 200, 24 * 60 * 60) {
    Action {
      implicit request =>

        import anorm.{Macro, RowParser}

        db.withConnection {
          implicit conn =>
            val parser: RowParser[Liwei] = Macro.indexedParser[Liwei]
            val x: LiweiX = cache.getOrElse[LiweiX]("profile" + name) {
              repack(SQL("select * from t9a where name={name} or ename={ename}")
                .on("name" -> name, "ename" -> name).as(parser.single)
              )
              //cache.set("profile" + name, x, 10.minutes)
            }
            Ok(views.html.profile(x))
        }
    }
  }


  def repack(x: Liwei) = {
    val email = if (x.email contains "不提供") {
      ""
    } else {
      if (x.email.endsWith("/")) {
        x.email.substring(0, x.email.length - "/".length())
      } else {
        x.email
      }
    }

    var facebook = x.facebook
    facebook = URLDecoder.decode(facebook, "utf8")
    if (facebook.startsWith("\"")) {
      facebook = facebook.substring(1)
    }
    if (facebook.endsWith("\"")) {
      facebook = facebook.substring(0, facebook.length - 1)
    }

    var facebook2 = ""
    for (indexString <- List("/https://", "/http://")) {
      if (facebook.lastIndexOf(indexString) > -1) {
        facebook2 = facebook.substring(facebook.lastIndexOf(indexString) + 1)
        facebook = facebook.substring(0, facebook.lastIndexOf(indexString))
      }
    }

    if (facebook2.endsWith("/")) {
      facebook2 = facebook2.substring(0, facebook2.length - "/".length())
    }
    facebook2 = facebook2.replace("?fref=ts", "")
    facebook2 = facebook2.replace("&fref=ts", "")
    if (facebook2.endsWith("/timeline")) {
      facebook2 = facebook2.substring(0, facebook2.length - "/timeline".length())
    }
    if (facebook2.endsWith("/")) {
      facebook2 = facebook2.substring(0, facebook2.length - "/".length())
    }

    if (facebook.endsWith("/")) {
      facebook = facebook.substring(0, facebook.length - "/".length())
    }
    facebook = facebook.replace("?fref=ts", "")
    facebook = facebook.replace("&fref=ts", "")
    if (facebook.endsWith("/timeline")) {
      facebook = facebook.substring(0, facebook.length - "/timeline".length())
    }
    if (facebook.endsWith("/")) {
      facebook = facebook.substring(0, facebook.length - "/".length())
    }

    var lineid2 = ""
    var lineid = x.lineid
    if (lineid.lastIndexOf("/") > -1) {
      lineid2 = lineid.substring(lineid.lastIndexOf("/") + 1)
      lineid = lineid.substring(0, lineid.lastIndexOf("/"))
    }

    var labtel = x.labtel
    //labtel=labtel.replace("國會辦公室：","");

    val wiki = URLDecoder.decode(x.wiki, "utf8")
    LiweiX(x.term, x.name, x.ename, x.sex, x.party, x.partygroup, x.areaname, x.district, email, x.committee, x.onboarddate, x.degree, x.profession, x.experience, x.alltel, labtel, x.servicetel1, x.servicetel2, x.servicetel3, x.servicetel4, x.servicetel5, x.labfax, x.servicefax1, x.servicefax2, x.servicefax3, x.servicefax4, x.servicefax5, x.picurl, x.leavedate, x.alladdr, x.labaddr, x.serviceaddr1, x.serviceaddr2, x.serviceaddr3, x.serviceaddr4, x.serviceaddr5, facebook, wiki, lineid, facebook2, lineid2)
  }

  def index = cached.status(_ => "index200", 200, 10) {
    Action {
      implicit request =>

        import anorm.{Macro, RowParser}

        db.withConnection {
          implicit conn =>
            val parser: RowParser[Liwei] = Macro.indexedParser[Liwei]
            val result: List[Liwei] = SQL"select * from t9a order by rand() limit 4".as(parser.*)
            Ok(views.html.index(result))
        }
    }
  }
}

case class Liwei(term: String, name: String, ename: String, sex: String, party: String, partygroup: String, areaname: String, district: String, email: String, committee: String, onboarddate: String, degree: String, profession: String, experience: String, alltel: String, labtel: String, servicetel1: String, servicetel2: String, servicetel3: String, servicetel4: String, servicetel5: String, labfax: String, servicefax1: String, servicefax2: String, servicefax3: String, servicefax4: String, servicefax5: String, picurl: String, leavedate: String, alladdr: String, labaddr: String, serviceaddr1: String, serviceaddr2: String, serviceaddr3: String, serviceaddr4: String, serviceaddr5: String, facebook: String, wiki: String, lineid: String)

case class LiweiX(term: String, name: String, ename: String, sex: String, party: String, partygroup: String, areaname: String, district: String, email: String, committee: String, onboarddate: String, degree: String, profession: String, experience: String, alltel: String, labtel: String, servicetel1: String, servicetel2: String, servicetel3: String, servicetel4: String, servicetel5: String, labfax: String, servicefax1: String, servicefax2: String, servicefax3: String, servicefax4: String, servicefax5: String, picurl: String, leavedate: String, alladdr: String, labaddr: String, serviceaddr1: String, serviceaddr2: String, serviceaddr3: String, serviceaddr4: String, serviceaddr5: String, facebook: String, wiki: String, lineid: String, facebook2: String, lineid2: String)

case class Category(name: String, amount: Int = 0, indexies: String)

case class Tag(name: String, quantity: Int)