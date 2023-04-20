package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@Repository
public class PostRepository {
  private ConcurrentHashMap<Long, Post> repository = new ConcurrentHashMap<>();
  private AtomicLong lastId = new AtomicLong();
  public List<Post> all() {
    return repository.values().stream().collect(Collectors.toList());
  }

  public Optional<Post> getById(long id) {
    return Optional.ofNullable(repository.get(id));
  }

  public Post save(Post post) {
    if (post.getId() == 0) {
      post.setId(lastId.addAndGet(1));
    } else if(getById(post.getId()).isEmpty()) {
      throw new NotFoundException("Wrong post id");
    }
    repository.put(post.getId(), post);
    return post;
  }

  public void removeById(long id) {
    repository.remove(id);
  }
}
