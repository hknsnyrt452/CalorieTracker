enum class MealType {
    KAHVALTI,
    OGLE_YEMEGI,
    AKSAM_YEMEGI,
    ARA_OGUN;

    override fun toString(): String {
        return when (this) {
            KAHVALTI -> "Kahvaltı"
            OGLE_YEMEGI -> "Öğle Yemeği"
            AKSAM_YEMEGI -> "Akşam Yemeği"
            ARA_OGUN -> "Ara Öğün"
        }
    }
} 