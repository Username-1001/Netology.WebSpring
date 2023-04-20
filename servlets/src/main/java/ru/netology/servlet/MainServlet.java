package ru.netology.servlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
  private PostController controller;
  private final String PATH_POSTS = "/api/posts";
  private final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("ru.netology");

  @Override
  public void init() {
    controller = (PostController) context.getBean("postController");
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    // если деплоились в root context, то достаточно этого
    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();

      if (path.equals(PATH_POSTS)) {
        if (method.equals(requestMethods.GET.getMethod())) {
          controller.all(resp);
          return;
        }
        if (method.equals(requestMethods.POST.getMethod())) {
          controller.save(req.getReader(), resp);
          return;
        }
      }

      if (path.matches(getRegex(path))) {
        final var id = Long.parseLong(path.substring(path.lastIndexOf("/")));

        if (method.equals(requestMethods.GET.getMethod())) {
          controller.getById(id, resp);
          return;
        }
        if (method.equals(requestMethods.DELETE.getMethod())) {
          controller.removeById(id, resp);
          return;
        }
      }

      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  private String getRegex(String path) {
    return path + "/" + "\\d+";
  }
}

