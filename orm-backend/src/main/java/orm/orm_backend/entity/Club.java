package orm.orm_backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn
    private User manager;

    @OneToOne
    @JoinColumn
    private Mountain mountain;

    @Column(nullable = false)
    private String clubName;

    private String description;
    private String imageSrc;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public Club(User manager, String clubName, String description, String imageSrc) {
        this.manager = manager;
        this.clubName = clubName;
        this.description = description;
        this.imageSrc = imageSrc;
    }
}
