package unithon.helpjob.data.model

enum class AppLanguage(val code: String) {
    ENGLISH("en"),
    KOREAN("ko");

    companion object {
        fun fromCode(code: String): AppLanguage {
            return entries.find { it.code == code } ?: ENGLISH
        }
    }
}