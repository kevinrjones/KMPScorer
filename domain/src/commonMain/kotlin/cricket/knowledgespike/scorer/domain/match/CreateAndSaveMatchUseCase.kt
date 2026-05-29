package cricket.knowledgespike.scorer.domain.match

import arrow.core.Either
import cricket.knowledgespike.scorer.domain.matchsetup.MatchSetup
import cricket.knowledgespike.scorer.domain.model.StoredMatch
import cricket.knowledgespike.scorer.domain.repository.MatchPersistenceError
import cricket.knowledgespike.scorer.domain.repository.MatchRepository

class CreateAndSaveMatchUseCase(
    private val matchRepository: MatchRepository,
) {
    suspend operator fun invoke(matchSetup: MatchSetup): Either<MatchPersistenceError, StoredMatch> {
        return matchRepository.createMatchFromSetup(matchSetup)
    }
}
