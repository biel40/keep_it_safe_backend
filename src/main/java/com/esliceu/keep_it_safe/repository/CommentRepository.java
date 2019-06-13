package com.esliceu.keep_it_safe.repository;

import com.esliceu.keep_it_safe.entities.Comment;
import com.esliceu.keep_it_safe.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository <Comment, Integer> {

    List<Comment> findCommentsByUser(User user);

}
