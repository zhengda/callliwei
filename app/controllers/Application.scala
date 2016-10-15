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

  def list(q: Option[String]) = Action {
    import anorm.{Macro, RowParser}
    db.withConnection { implicit conn =>
      val parser: RowParser[Liwei] = Macro.indexedParser[Liwei]
      q match {
        case Some(value) =>
          val result: List[Liwei] = SQL("select * from t9a where name like {name} or ename like {ename}")
            .on("name" -> ("%" + value + "%"), "ename" -> ("%" + value + "%")).as(parser.*)
          //result.foreach(println)
          Ok(views.html.list(result))
        case None =>
          val result: List[Liwei] = SQL"select * from t9a ".as(parser.*)
          //result.foreach(println)
          Ok(views.html.list(result))
      }
    }
  }


  def profile(name: String) = Action {
    import anorm.{Macro, RowParser}
    db.withConnection { implicit conn =>
      val parser: RowParser[Liwei] = Macro.indexedParser[Liwei]
      val result: Liwei = SQL(
        """
           select * from t9a where name={name}
        """).on("name" -> name).as(parser.single)
      Ok(views.html.profile(result))
    }

    //    var name = "";
    //    var ename = "";
    //    var picUrl = "";
    //    db.withConnection { implicit conn =>
    //      val st = conn.createStatement()
    //      val res = st.executeQuery("select * from t9a where degree like '%法律系%'")
    //      var i: Int = 0
    //      while (res.next()) {
    //        i += 1
    //        print(i)
    //        print(" ")
    //        print(res.getString("term"))
    //        print(" ")
    //        print(res.getString("name"))
    //        print(" ")
    //        print(res.getString("ename"))
    //        print(" ")
    //        println(res.getString("LineID"))
    //        if (i == 1) {
    //          name = res.getString("name")
    //          ename = res.getString("ename")
    //          picUrl = res.getString("picurl")
    //        }
    //      }
    //    }
    //    Ok(views.html.profile(name, ename, picUrl))
  }


  def index = Action {
    //    val db = dbapi.database("default")
//    db.withConnection { implicit c =>
//      val result: Boolean = SQL("Select 1").execute()
//      println(result)
//    }
//    db.withConnection { implicit c =>
//      val result = SQL("Select * from User").execute()
//      println(result)
//    }
//    var name = "";
//    var picUrl = "";
//    db.withConnection { implicit conn =>
//      val st = conn.createStatement()
//      val res = st.executeQuery("SELECT * FROM t9a")
//      var i: Int = 0
//      while (res.next()) {
//        i += 1
//        print(i)
//        print(" ")
//        print(res.getString("term"))
//        print(" ")
//        print(res.getString("name"))
//        print(" ")
//        print(res.getString("ename"))
//        print(" ")
//        println(res.getString("LineID"))
//        if (i == 1) {
//          name = res.getString("name")
//          picUrl = res.getString("picurl")
//        }
//      }
//    }

//    db.withConnection { conn =>
//      val st = conn.createStatement()
//      val res = st.executeQuery("SELECT * FROM PLAY_EVOLUTIONS")
//      while (res.next()) {
//        print(res.getInt("id"))
//        print(" ")
//        print(res.getString("hash"))
//        print(" ")
//        println(res.getString("state"))
//      }
//    }

    //    import java.util.Date
    //    import java.text.SimpleDateFormat
    //    val date = new Date()
    //    val dateStr = new SimpleDateFormat().format(date)
    //    Ok(views.html.index(dateStr, name, picUrl))

    import anorm.{Macro, RowParser}
    db.withConnection { implicit conn =>
      val parser: RowParser[Liwei] = Macro.indexedParser[Liwei]
      val result: List[Liwei] = SQL"select * from t9a order by rand() limit 4".as(parser.*)
      //result.foreach(println)
      Ok(views.html.index(result))
    }


  }

}


case class Liwei(term: String, name: String, ename: String, sex: String, party: String, partygroup: String, areaname: String, district: String, email: String, committee: String, onboarddate: String, degree: String, profession: String, experience: String, alltel: String, labtel: String, servicetel1: String, servicetel2: String, servicetel3: String, servicetel4: String, servicetel5: String, labfax: String, servicefax1: String, servicefax2: String, servicefax3: String, servicefax4: String, servicefax5: String, picurl: String, leavedate: String, alladdr: String, labaddr: String, serviceaddr1: String, serviceaddr2: String, serviceaddr3: String, serviceaddr4: String, serviceaddr5: String, facebook: String, wiki: String, lineid: String)
