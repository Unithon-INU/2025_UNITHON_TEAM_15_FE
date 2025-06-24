package unithon.helpjob.data.model

enum class AppLanguage(val code: String, val displayName: String) {
    KOREAN("ko", "한국어"),
    ENGLISH("en", "English");

    companion object {
        fun fromCode(code: String): AppLanguage {
            return entries.find { it.code == code } ?: ENGLISH
        }

        fun fromDisplayName(displayName: String): AppLanguage {
            return entries.find { it.displayName == displayName } ?: ENGLISH
        }

        fun getAllDisplayNames(): List<String> {
            return entries.map { it.displayName }
        }
    }
}