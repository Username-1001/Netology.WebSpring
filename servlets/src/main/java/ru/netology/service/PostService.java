package ru.netology.service;

import org.springframework.stereotype.Service;
import ru.netology.dto.PostDto;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;
import ru.netology.repository.PostRepository;
import ru.netology.utils.MappingUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
  private final PostRepository repository;

  public PostService(PostRepository repository) {
    this.repository = repository;
  }

  public List<PostDto> all() {
    List<Post> posts = repository.all();
    return posts.stream().map(MappingUtils::mapToPostDto).collect(Collectors.toList());
  }

  public PostDto getById(long id) {
    Post post = repository.getById(id).orElseThrow(NotFoundException::new);
    return MappingUtils.mapToPostDto(post);
  }

  public PostDto save(PostDto postDto) {
    Post postToSave = MappingUtils.mapToPost(postDto);
    Post postSaved = repository.save(postToSave);
    return MappingUtils.mapToPostDto(postSaved);
  }

  public void removeById(long id) {
    repository.removeById(id);
  }
}

