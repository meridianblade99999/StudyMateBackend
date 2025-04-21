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
@Table(name = "announcement")
@AllArgsConstructor
@NoArgsConstructor
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "title")
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "bg_color")
    private String bgColor;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @ManyToMany(mappedBy = "announcements")
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "announcement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorite> favorites = new ArrayList<>();

    @PreRemove
    private void removeTagAssociations() {
        for (Tag tag : tags) {
            tag.getAnnouncements().remove(this);
        }
    }
}
