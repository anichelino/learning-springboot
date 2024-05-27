package microservices.book.multiplication.challenge;

import microservices.book.multiplication.user.User;
import org.springframework.stereotype.Service;

@Service
public class ChallengeServiceImpl implements ChallengeService {

    @Override
    public ChallengeAttempt verifyAttempt(ChallengeAttemptDTO attemptDTO) {
        // Check if the attempt is correct
        boolean isCorrect=false;
        switch (attemptDTO.getOperationType()){
            case Addition ->{
                isCorrect = attemptDTO.getGuess() ==
                        attemptDTO.getFactorA() + attemptDTO.getFactorB();
            }
            case Division -> {
                isCorrect = attemptDTO.getGuess() ==
                        attemptDTO.getFactorA() / attemptDTO.getFactorB();
            }
            case Multiplication -> {
                isCorrect = attemptDTO.getGuess() ==
                        attemptDTO.getFactorA() * attemptDTO.getFactorB();
            }
            case Subtraction -> {
                isCorrect = attemptDTO.getGuess() ==
                        attemptDTO.getFactorA() - attemptDTO.getFactorB();
            }
        }

        // We don't use identifiers for now
        User user = new User(null, attemptDTO.getUserAlias());

        // Builds the domain object. Null id for now.
        return new ChallengeAttempt(null,
                user,
                attemptDTO.getFactorA(),
                attemptDTO.getFactorB(),
                attemptDTO.getOperationType(),
                attemptDTO.getGuess(),
                isCorrect
        );
    }
}