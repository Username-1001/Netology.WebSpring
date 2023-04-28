package ru.netology.utils;

import ru.netology.dto.PostDto;
import ru.netology.model.Post;

import java.beans.PropertyEditorSupport;

public class MappingUtils {
    public static PostDto mapToPostDto(Post post) {
        PostDto result = new PostDto();
        result.setContent(post.getContent());
        result.setId(post.getId());
        return result;
    }

    public static Post mapToPost(PostDto postDto) {
        Post result = new Post();
        result.setContent(postDto.getContent());
        result.setId(postDto.getId());
        return result;
    }
}
