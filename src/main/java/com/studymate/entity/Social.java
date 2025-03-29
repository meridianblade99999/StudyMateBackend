package com.studymate.entity;

import com.studymate.entity.authentication.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "socials")
@AllArgsConstructor
@NoArgsConstructor
public class Social {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "ok")
    private String ok;

    @Column(name = "vk")
    private String vk;

    @Column(name = "telegram")
    private String telegram;

    @Column(name = "whatsapp")
    private String whatsApp;

}

