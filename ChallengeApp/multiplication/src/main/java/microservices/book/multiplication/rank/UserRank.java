package microservices.book.multiplication.rank;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class UserRank {

    private String userAlias;
    private int totalAttempts;
    private int correctAttempts;
    private int rank;

    @Override
    public String toString() {
        return "UserRank{" +
                "userAlias : '" + userAlias + '\'' +
                ", totalAttempts : " + totalAttempts +
                ", correctAttempts : " + correctAttempts +
                ", rank : " + rank +
                ", successPercentage : " + successPercentage*100 +
                "%}";
    }

    private double successPercentage;
}
