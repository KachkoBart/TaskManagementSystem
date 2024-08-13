package org.example.taskmanagementsystem.Api.Repository.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    private Long id;
    private String email;
    private String comment;
}
