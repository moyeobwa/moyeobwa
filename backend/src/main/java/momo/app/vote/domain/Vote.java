package momo.app.vote.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import momo.app.common.domain.BaseTime;
import momo.app.gathering.domain.Gathering;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gathering_id")
    private Gathering gathering;

    @Column(nullable = false)
    private Long creatorId;

    private VoteStatus status;

    @Builder
    public Vote(
            String title,
            Gathering gathering,
            Long creatorId
    ) {
        this.title = title;
        this.gathering = gathering;
        this.creatorId = creatorId;
        this.status = VoteStatus.PROGRESS;
    }

    public boolean isEnd() {
        if (status.equals(VoteStatus.END)) {
            return true;
        }
        return false;
    }

    public void terminate() {
        status = VoteStatus.END;
    }
}
