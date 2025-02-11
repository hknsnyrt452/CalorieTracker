enum class MealType {
    BREAKFAST,
    LUNCH,
    DINNER,
    SNACK;

    override fun toString(): String {
        return when (this) {
            BREAKFAST -> "Kahvaltı"
            LUNCH -> "Öğle Yemeği"
            DINNER -> "Akşam Yemeği"
            SNACK -> "Ara Öğün"
        }
    }
} 