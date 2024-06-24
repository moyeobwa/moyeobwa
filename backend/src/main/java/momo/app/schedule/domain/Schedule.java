package momo.app.schedule.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import momo.app.common.domain.BaseTime;
import momo.app.common.error.exception.BusinessException;
import momo.app.gathering.domain.Gathering;
import momo.app.schedule.dto.request.ScheduleUpdateRequest;
import momo.app.user.domain.User;

import java.time.LocalDate;
import java.time.LocalTime;

import static momo.app.schedule.exception.ScheduleErrorCode.SCHEDULE_NOT_EQUAL_USER;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Color color;

    private String title;

    @Column(length = 100)
    private String content;

    private LocalDate date;

    private LocalTime time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gathering_id")
    private Gathering gathering;

    @Builder
    public Schedule(
            Color color,
            String title,
            String content,
            LocalDate date,
            LocalTime time,
            User user,
            Gathering gathering
    ) {
        this.color = color;
        this.title = title;
        this.content = content;
        this.date = date;
        this.time = time;
        this.user = user;
        this.gathering = gathering;
    }

    public void update(ScheduleUpdateRequest request) {
        this.color = request.color();
        this.title = request.title();
        this.content = request.content();
        this.time = request.time();
    }

    public void validateUser(User user) {
        if (!user.getId().equals(this.user.getId())) {
            throw new BusinessException(SCHEDULE_NOT_EQUAL_USER);
        }
    }
}
