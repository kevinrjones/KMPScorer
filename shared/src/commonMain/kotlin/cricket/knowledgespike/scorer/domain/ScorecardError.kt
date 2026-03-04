package cricket.knowledgespike.scorer.domain

sealed interface ScorecardError {

    sealed interface RemoteError : ScorecardError {

    }
    sealed interface Local: ScorecardError {
        data class UnableToFindScorecard(val id: Int) : Local

        data object UnableToInsertScorecard : Local
        data object UnableToUpdateScorecard : Local
        data class UnableToDeleteScorecard(val id: Int?) : Local

        data object DiskFull : Local
    }
}