package com.studymate.entity;

import com.studymate.entity.authentication.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "chat_setting")
@AllArgsConstructor
@NoArgsConstructor
public class ChatSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "chat_user_id")
    private ChatUser chatUser;

    @Column(name = "muted")
    private Boolean muted;

    @Column(name = "notifications")
    private Boolean notifications;

}
