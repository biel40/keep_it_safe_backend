package com.esliceu.keep_it_safe.managers.entities;

import com.esliceu.keep_it_safe.entities.Comment;
import com.esliceu.keep_it_safe.entities.User;
import com.esliceu.keep_it_safe.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommentManager {

    private CommentRepository commentRepository;

    @Autowired
    public CommentManager(CommentRepository commentRepository){
        this.commentRepository = commentRepository;
    }

    public List<Comment> getCommentsByUser(User user){
        return commentRepository.findCommentsByUser(user);
    }

    public List<Comment> getCommentsByDelimitter(int numberOfComments){

        List<Comment> allCommentsList = (List<Comment>) commentRepository.findAll();

        if (numberOfComments <= allCommentsList.size()){

            int listLength = allCommentsList.size();
            int startIndex = (listLength - 1) - numberOfComments;

            return allCommentsList.subList(startIndex, (listLength - 1));

        } else {
            return allCommentsList;
        }

    }

    public void saveCommentInDataBase(Comment comment){
        commentRepository.save(comment);
    }

    public String commentsToJSON(List<Comment> comments) {

        StringBuilder commentsToJSON = new StringBuilder();
        commentsToJSON.append("[");

        for (Comment comment : comments) {
            commentsToJSON.append(comment.stringToJSON() +",");
        }

        commentsToJSON.deleteCharAt(commentsToJSON.length() - 1);
        commentsToJSON.append("]");

        return commentsToJSON.toString();

    }

    public List<Comment> getAllComments() {
        return (List<Comment>) commentRepository.findAll();
    }
}
