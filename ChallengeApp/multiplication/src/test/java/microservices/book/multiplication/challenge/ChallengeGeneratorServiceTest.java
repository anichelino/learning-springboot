package microservices.book.multiplication.challenge;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
@Slf4j
@ExtendWith(MockitoExtension.class)
public class ChallengeGeneratorServiceTest {

    private ChallengeGeneratorService challengeGeneratorService;

    @Spy
    private Random random;

    @BeforeEach
    public void setUp() {
        challengeGeneratorService = new ChallengeGeneratorServiceImpl(random);
    }

    @Test
    public void generateRandomFactorIsBetweenExpectedLimitsMultiplication() {
        // 89 is max - min range
        given(random.nextInt(89)).willReturn(20, 30);
        doReturn(2).when(random).nextInt(3);

        // when we generate a challenge
        Challenge challenge = challengeGeneratorService.randomChallenge();

        // then the challenge contains factors as expected
        String descriptionText = then(challenge).isEqualTo(new Challenge(31, 41, ChallengeType.Multiplication)).descriptionText();
        log.info(descriptionText);
    }
    @Test
    public void generateRandomFactorIsBetweenExpectedLimitsDivision() {
        // 89 is max - min range
        given(random.nextInt(89)).willReturn(20, 30);
        doReturn(3).when(random).nextInt(3);

        // when we generate a challenge
        Challenge challenge = challengeGeneratorService.randomChallenge();

        // then the challenge contains factors as expected
        String descriptionText = then(challenge).isEqualTo(new Challenge(31, 41, ChallengeType.Division)).descriptionText();
        log.info(descriptionText);
    }

}