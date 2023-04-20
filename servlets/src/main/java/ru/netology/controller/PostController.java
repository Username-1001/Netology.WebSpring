package ru.netology.controller;

import org.springframework.web.bind.annotation.*;
import ru.netology.dto.PostDto;
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
    public List<PostDto> all() throws IOException {
        return service.all();
    }
    @GetMapping("/{id}")
    public PostDto getById(@PathVariable long id) {
        return service.getById(id);
    }
    @PostMapping
    public PostDto save(@RequestBody PostDto postDto) throws IOException {
        return service.save(postDto);
    }
    @DeleteMapping
    public void removeById(@PathVariable long id) {
        service.removeById(id);
    }
}
