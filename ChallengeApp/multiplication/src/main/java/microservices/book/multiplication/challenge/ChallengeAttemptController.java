package microservices.book.multiplication.challenge;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservices.book.multiplication.rank.UserRank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

/**
 * This class provides a REST API to POST the attempts from users.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/attempts")
class ChallengeAttemptController {

    private final ChallengeService challengeService;

    @PostMapping
    ResponseEntity<ChallengeAttempt> postResult(
             @Valid @RequestBody ChallengeAttemptDTO challengeAttemptDTO) {
        return ResponseEntity.ok(challengeService.verifyAttempt(challengeAttemptDTO));
    }
    @GetMapping
    ResponseEntity<List<ChallengeAttempt>> getStatistics(
            @RequestParam("alias") String alias) {
        return ResponseEntity.ok(challengeService.getStatsForUser(alias));
    }

    @GetMapping(path = "rankStatistics")
    ResponseEntity<List<UserRank>> getRankStatistics(){

        return ResponseEntity.ok(challengeService.getAllStats());
    }
}