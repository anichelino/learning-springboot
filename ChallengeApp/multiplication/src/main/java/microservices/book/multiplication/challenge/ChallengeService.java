package microservices.book.multiplication.challenge;

import microservices.book.multiplication.rank.UserRank;

import java.util.List;

public interface ChallengeService {

    /**
     * Verifies if an attempt coming from the presentation layer is correct or not.
     *
     * @return the resulting ChallengeAttempt object
     */
    ChallengeAttempt verifyAttempt(ChallengeAttemptDTO attemptDTO);

    List<ChallengeAttempt> getStatsForUser(String alias);

    List<UserRank> getAllStats();
}