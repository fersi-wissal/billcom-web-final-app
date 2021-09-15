package com.billcom.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.billcom.app.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
