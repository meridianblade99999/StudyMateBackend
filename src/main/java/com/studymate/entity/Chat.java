package com.studymate.entity;

import com.studymate.entity.authentication.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "chat")
@AllArgsConstructor
@NoArgsConstructor
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String title;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @OneToMany(mappedBy = "chat")
    private List<ChatUser> chatUsers = new ArrayList<>();
}
