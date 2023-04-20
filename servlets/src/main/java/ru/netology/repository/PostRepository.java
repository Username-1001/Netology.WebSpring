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
    return repository.values().stream().filter(o -> !o.isDeleted()).collect(Collectors.toList());
  }

  public Optional<Post> getById(long id) {
    Post post = repository.get(id);
    if (post.isDeleted()) {
      post = null;
    }
    return Optional.ofNullable(post);
  }

  public Post save(Post post) {
    Optional<Post> oldPost = getById(post.getId());

    if (post.getId() == 0) {
      post.setId(lastId.addAndGet(1));
    } else if(oldPost.isEmpty() || !oldPost.isEmpty() && oldPost.get().isDeleted()) {
      throw new NotFoundException("Wrong post id");
    }
    repository.put(post.getId(), post);
    return post;
  }

  public void removeById(long id) {
    Optional<Post> postToDeleteOpt = getById(id);
    if (!postToDeleteOpt.isEmpty()) {
      Post postToDelete = postToDeleteOpt.get();
      postToDelete.setDeleted(true);
      repository.put(id, postToDelete);
    }
  }
}
