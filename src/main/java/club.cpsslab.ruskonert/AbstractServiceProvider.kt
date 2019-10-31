package club.cpsslab.ruskonert

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

abstract class AbstractServiceProvider : HttpServlet()
{
    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        resp.writer.println("hello world!")
    }

    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        resp.writer.println("hello world!")
    }
}