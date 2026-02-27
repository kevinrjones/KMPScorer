package cricket.knowledgespike.scorer

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform