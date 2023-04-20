package ru.netology.controller;

import org.springframework.web.bind.annotation.*;
import ru.netology.model.Post;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    @GetMapping
    public List<Post> all(HttpServletResponse response) throws IOException {
        return service.all();
    }
    @GetMapping("/{id}")
    public Post getById(@PathVariable long id, HttpServletResponse response) {
        return service.getById(id);
    }
    @PostMapping
    public Post save(@RequestBody Post post) throws IOException {
        return service.save(post);
    }
    @DeleteMapping
    public void removeById(@PathVariable long id, HttpServletResponse response) {
        service.removeById(id);
    }
}
