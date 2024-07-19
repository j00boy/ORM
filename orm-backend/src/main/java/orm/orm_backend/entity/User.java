package orm.orm_backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nickname;

    private String imageSrc;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "char(1)")
    private UserStatus isActive = UserStatus.Y;

    @Column(nullable = false)
    private String kakaoAccessToken;

    @Column(nullable = false)
    private String kakaoRefreshToken;

    private Long kakaoId;

    public void delete() {
        this.isActive = UserStatus.N;
    }

    @Builder
    public User(Long kakaoId, String nickname, String imageSrc, String kakaoAccessToken, String kakaoRefreshToken) {
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.imageSrc = imageSrc;
        this.kakaoAccessToken = kakaoAccessToken;
        this.kakaoRefreshToken = kakaoRefreshToken;
    }
}