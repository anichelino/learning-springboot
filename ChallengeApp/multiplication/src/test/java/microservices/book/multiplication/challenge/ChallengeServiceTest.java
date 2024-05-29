package microservices.book.multiplication.challenge;

import microservices.book.multiplication.user.User;
import microservices.book.multiplication.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class ChallengeServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ChallengeAttemptRepository attemptRepository;
    @InjectMocks
    private ChallengeServiceImpl challengeService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void checkCorrectAttemptTest() {

        // given
        given(attemptRepository.save(any())).will(returnsFirstArg());

        ChallengeAttemptDTO attemptDTO =
                new ChallengeAttemptDTO(300, 60, "Luca", ChallengeType.Subtraction,240);
        User user = new User(1L, "Luca");
        ChallengeAttempt expectedAttempt = new ChallengeAttempt(null, user, 5, 3, 8, ChallengeType.Subtraction, true);

        given(userRepository.findByAlias("Luca")).willReturn(Optional.empty());

        //given(attemptRepository.save(any(ChallengeAttempt.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        ChallengeAttempt resultAttempt =
                challengeService.verifyAttempt(attemptDTO);

        System.out.println("returned challenge attempt: "+resultAttempt);

        then(resultAttempt.isCorrect()).isTrue();

        //John user not present--> verify that save method of the userRepository is really called;
        // fixed that by calling findAlias with argument luca will return an empty optional, the save method must be called with a new User with alias Luca
        verify(userRepository).findByAlias("Luca");
        verify(userRepository).save(new User("Luca"));
        verify(attemptRepository).save(resultAttempt);
    }

    @Test
    public void checkWrongAttemptTest() {
        // given
        ChallengeAttemptDTO attemptDTO =
                new ChallengeAttemptDTO(50, 60, "john_doe", ChallengeType.Multiplication,500);

        // when
        ChallengeAttempt resultAttempt =
                challengeService.verifyAttempt(attemptDTO);

        // then
        then(resultAttempt.isCorrect()).isFalse();
    }
    @Test
    public void retrieveStatsTest() {
        // given
        User user = new User("john_doe");
        ChallengeAttempt attempt1 = new ChallengeAttempt(1L, user, 50, 60, 3010,ChallengeType.Multiplication, false);
        ChallengeAttempt attempt2 = new ChallengeAttempt(2L, user, 50, 60, 3051,ChallengeType.Multiplication, false);
        ChallengeAttempt attempt3 = new ChallengeAttempt(3L, user, 50, 60, 3051,ChallengeType.Multiplication, false);
        ChallengeAttempt attempt4 = new ChallengeAttempt(4L, user, 50, 60, 3051,ChallengeType.Multiplication, false);
        ChallengeAttempt attempt5 = new ChallengeAttempt(5L, user, 50, 60, 3051,ChallengeType.Multiplication, false);
        ChallengeAttempt attempt6 = new ChallengeAttempt(6L, user, 50, 60, 3051,ChallengeType.Multiplication, false);
        ChallengeAttempt attempt7 = new ChallengeAttempt(7L, user, 50, 60, 3051,ChallengeType.Multiplication, false);
        List<ChallengeAttempt> lastAttempts = List.of(attempt1, attempt2,attempt3,attempt4,attempt5);

        //in this case 7 attempts have been created for the user john_doe , but we are stubbing the method findTop5ByUserAliasOrderByIdDesc such that only 5 last attempts must be returned --> it's better to perform test with postman
        given(attemptRepository.findTop5ByUserAliasOrderByIdDesc("john_doe"))
                .willReturn(lastAttempts);

        // when
        List<ChallengeAttempt> latestAttemptsResult =
                challengeService.getStatsForUser("john_doe");

        // then
        then(latestAttemptsResult).isEqualTo(lastAttempts);
    }
}