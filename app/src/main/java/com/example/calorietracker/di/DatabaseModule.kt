@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    // ... diğer provide metodları ...

    @Provides
    fun provideFoodDao(database: AppDatabase): FoodDao {
        return database.foodDao()
    }

    @Provides
    fun provideFoodRepository(foodDao: FoodDao): FoodRepository {
        return FoodRepository(foodDao)
    }
} 