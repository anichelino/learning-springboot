package microservices.book.multiplication.challenge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ChallengeGeneratorServiceImpl implements ChallengeGeneratorService {

    private final Random random;
    private static final int MINIMUM_FACTOR = 11;
    private static final int MAXIMUM_FACTOR = 100;

    @Autowired
    ChallengeGeneratorServiceImpl() {
        this.random = new Random();
    }

    protected ChallengeGeneratorServiceImpl(final Random random) {
        this.random = random;
    }

    private int next() {
        return random.nextInt(MAXIMUM_FACTOR - MINIMUM_FACTOR )   + MINIMUM_FACTOR;
    }

    private ChallengeType getChallengeType(){
        ChallengeType[] values = ChallengeType.values();
        int randomIndex = random.nextInt(values.length-1);
        return values[randomIndex];
    }
    private ChallengeType getChallengeOfAGivenOperation(String operationType){
        return ChallengeType.valueOf(operationType);
    }

    @Override
    public Challenge randomChallenge() {
        return new Challenge(next(), next(),getChallengeType());
    }

    @Override
    public <T> Challenge randomChallengeOfGivenOperation(T input) {
        String operationType= (String) input;
        ChallengeType challengeType = ChallengeType.fromString(operationType);
        return new Challenge(next(), next(),challengeType);
    }
}
