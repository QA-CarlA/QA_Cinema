package backend.Controllers

import akka.stream.Materializer
import controllers.DiscussionController
import dao.discussionDAO
import org.scalatest.matchers.should.Matchers._
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.libs.json.Json
import play.api.mvc._
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class DiscussionControllerTest extends PlaySpec with Results with GuiceOneAppPerSuite {

  implicit lazy val materializer: Materializer = app.materializer
  implicit lazy val Action                     = app.injector.instanceOf(classOf[DefaultActionBuilder])
  val controller = new DiscussionController(Helpers.stubControllerComponents())

  "Discussion page #index" should {
    "be valid" in {
      val result: Future[Result] = controller.index.apply(FakeRequest())
      val bodyText = contentAsString(result)
      bodyText mustBe contentAsString(discussionDAO.getReviews map {
        results => Ok(views.html.discussionPage(results))
      })
    }
  }

  "Discussion page #discForm" should {
    "be valid" in {
      val result: Future[Result] = controller.discForm().apply(FakeRequest())
      val bodyText = contentAsString(result)
      bodyText should include ("<h1 class=\"card-title\" style=\"color: white\">Welcome to our discussion board movie lovers!</h1>")
    }
  }

  "Discussion page #discPost" should {
    "be valid" in {
      val result: Future[Result] = controller.discFormPost().apply(FakeRequest())
      val bodyText = contentAsString(result)
      bodyText should include ("<h1 class=\"card-title\" style=\"color: white\">Welcome to our discussion board movie lovers!</h1>")
    }
  }

  "Discussion page #discPosts" should {
    "be valid" in {
      val request = FakeRequest(POST, "/discussions/entry").withJsonBody(Json.parse("""{ "id": "1", "title": "shrek", "description ": "best movie", "rating ": "10", "onApproved ": "0" }"""))
      //val result = Await.result(call(controller.discForm, request), Duration.Inf)
      val result = call(controller.discFormPost(), request)
      //result.body should include ("<h1> Welcome to our discussion board movie lovers! </h1>") //Ok(views.html.discussionPage2(discussionForm.form))
      //result. must contain ("<h1> Welcome to our discussion board movie lovers! </h1>")
      contentAsString(result) should include ("<h1 class=\"card-title\" style=\"color: white\">Welcome to our discussion board movie lovers!</h1>")
    }
  }
}
