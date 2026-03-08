package cricket.knowledgespike.scorer.domain

sealed interface ScorecardError {

    sealed interface RemoteError : ScorecardError {

    }
    sealed interface Local: ScorecardError {
        data class UnableToFindScorecard(val id: Int, val message: String? = null) : Local

        data class UnableToInsertScorecard(val message: String?) : Local
        data object UnableToUpdateScorecard : Local
        data class UnableToDeleteScorecard(val id: Int?) : Local

        data object DiskFull : Local
    }
}