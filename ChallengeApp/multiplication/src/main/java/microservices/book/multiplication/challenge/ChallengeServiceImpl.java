package microservices.book.multiplication.challenge;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservices.book.multiplication.rank.UserRank;
import microservices.book.multiplication.user.User;
import microservices.book.multiplication.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChallengeServiceImpl implements ChallengeService {


    private final UserRepository userRepository;

    private final ChallengeAttemptRepository attemptRepository;
    @Override
    public ChallengeAttempt verifyAttempt(ChallengeAttemptDTO attemptDTO) {
        // Check if the attempt is correct
        boolean isCorrect = false;
        switch (attemptDTO.getOperationType()) {
            case Addition -> {
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

        User user = userRepository.findByAlias(attemptDTO.getUserAlias()).orElseGet(() -> {
            log.info("Creating new user with alias {}", attemptDTO.getUserAlias());
            return userRepository.save(new User(attemptDTO.getUserAlias()));
        });
        // We don't use identifiers for now
        ChallengeAttempt checkedAttempt = new ChallengeAttempt(null,
                user,
                attemptDTO.getFactorA(),
                attemptDTO.getFactorB(),
                attemptDTO.getGuess(),
                attemptDTO.getOperationType(),
                isCorrect
        );

        // Stores the attempt
        ChallengeAttempt storedAttempt = attemptRepository.save(checkedAttempt);

        return storedAttempt;

    }

    @Override
    public List<ChallengeAttempt> getStatsForUser(String userAlias) {
        return attemptRepository.findTop5ByUserAliasOrderByIdDesc(userAlias);
    }
    @Override
    public List<UserRank> getAllStats() {
        Iterable<User> users = userRepository.findAll();
        Iterator<User> userIterator = users.iterator();
        List<User> usersList= new ArrayList<>();
        List<ChallengeAttempt> attemptList= new ArrayList<>();
        List<UserRank> userRankList= new ArrayList<>();
        while (userIterator.hasNext()){
            usersList.add(userIterator.next());
        }
        Iterable<ChallengeAttempt> allAttempts = attemptRepository.findAll();
        Iterator<ChallengeAttempt> attemptIterator = allAttempts.iterator();
        while (attemptIterator.hasNext()){
            attemptList.add(attemptIterator.next());
        }
        for (User user:usersList) {

            List<ChallengeAttempt> attemptListWithSameAlias = attemptList.stream().filter((singleAttempt) -> singleAttempt.getUser().getAlias().equalsIgnoreCase(user.getAlias())).toList();
            int totalAttempts=attemptListWithSameAlias.size();
            int correctAttempts= (int)attemptListWithSameAlias.stream().filter(ChallengeAttempt::isCorrect).count();
            int rank=0;
            double success= (double) correctAttempts /totalAttempts;
            userRankList.add(new UserRank(user.getAlias(),totalAttempts,correctAttempts,rank,success));
        }
        List<UserRank> orderedList = new ArrayList<>(userRankList.stream().sorted(Comparator.comparingDouble(UserRank::getSuccessPercentage).reversed()).toList());
        for (int i = 0; i < orderedList.size(); i++) {
            orderedList.get(i).setRank(i + 1);
        }
        log.info("User rank list not ordered & random ranks: "+userRankList);
        log.info("User rank list ordered with correct ranks: "+orderedList);
        return orderedList.stream().limit(5).toList();
    }

}