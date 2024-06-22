package momo.app.schedule.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import momo.app.common.domain.BaseTime;
import momo.app.gathering.domain.Gathering;
import momo.app.user.domain.User;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    private Color color;

    private String title;

    @Column(length = 100)
    private String content;

    private LocalDate date;

    private LocalTime time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gathering_id")
    private Gathering gathering;

    @Builder
    public Schedule(
            String nickname,
            Color color,
            String title,
            String content,
            LocalDate date,
            LocalTime time,
            Gathering gathering
    ) {
        this.nickname = nickname;
        this.color = color;
        this.title = title;
        this.content = content;
        this.date = date;
        this.time = time;
        this.gathering = gathering;
    }
}
