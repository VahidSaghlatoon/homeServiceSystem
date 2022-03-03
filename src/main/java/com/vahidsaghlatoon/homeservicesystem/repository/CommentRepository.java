package com.vahidsaghlatoon.homeservicesystem.repository;

import com.vahidsaghlatoon.homeservicesystem.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
