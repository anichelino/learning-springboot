package microservices.book.multiplication.challenge;

import jakarta.validation.constraints.NotNull;

import java.util.Optional;

public enum ChallengeType {
    Addition,
    Subtraction,
    Multiplication,
    Division;

    public static ChallengeType fromString(@NotNull String input) {

            for (ChallengeType type : ChallengeType.values()) {
                if (type.name().equalsIgnoreCase(input)) {
                    return type;
                }
            }
        throw new IllegalArgumentException("No enum constant for input: " + input);
    }
}
