package com.aiden3630.data.repository

import android.content.Context
import com.aiden3630.domain.model.Product
import com.aiden3630.domain.repository.ShopRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import javax.inject.Inject

class ShopRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context // Нам нужен контекст для доступа к файлам
) : ShopRepository {

    private val jsonParser = Json {
        ignoreUnknownKeys = true
        prettyPrint = true // Чтобы файл был красивым в блокноте
    }

    private val fileName = "shop_products.json"

    // Дефолтный список, который запишется при первом запуске
    private fun getDefaultProducts(): List<Product> {
        return listOf(
            Product(
                id = 1,
                title = "Рубашка Воскресенье",
                price = 300,
                category = "Мужчинам",
                description = "Мой выбор для этих шапок – кардные составы, которые раскрываются деликатным пушком. Кашемиры, мериносы, смесовки с ними отлично подойдут на шапку.\n" +
                        "Кардные составы берите в большое количество сложений, вязать будем резинку 1х1, плотненько.\n" +
                        "Примерный расход на шапку с подгибом 70-90г."
            ),
            Product(
                id = 2,
                title = "Шорты Вторник",
                price = 400,
                category = "Мужчинам",
                description = "Легкие и удобные шорты для дома и отдыха. \n" +
                        "Рекомендуемая пряжа: хлопок с акрилом (50/50) или чистый хлопок мерсеризованный.\n" +
                        "Спицы: №3 для резинки, №4 для основного полотна.\n" +
                        "Плотность вязания: 20 петель х 28 рядов = 10х10 см.\n" +
                        "Особенности: вяжутся снизу вверх, без швов, пояс на кулиске."
            ),
            Product(
                id = 3,
                title = "Платье Среда",
                price = 800,
                category = "Женщинам",
                description = "Элегантное платье силуэта трапеция.\n" +
                        "Идеально подходит для летних прогулок. Используйте натуральный лен или шелк.\n" +
                        "Вам понадобится:\n" +
                        "- Ткань: 2.5 метра при ширине 140 см.\n" +
                        "- Потайная молния 50 см.\n" +
                        "- Нитки в тон.\n" +
                        "Сложность: Средняя (требует навыков втачивания молнии)."
            ),
            Product(
                id = 4,
                title = "Футболка Четверг",
                price = 450,
                category = "Детям",
                description = "Базовая футболка оверсайз для ребенка.\n" +
                        "Материал: Кулирная гладь (100% хлопок) или футер 2-х нитка.\n" +
                        "Расход ткани: 0.6 - 0.8 метра в зависимости от роста.\n" +
                        "В выкройке учтены припуски на швы 0.7 см.\n" +
                        "Отлично сочетается с джинсами или спортивными штанами."
            ),
            Product(
                id = 5,
                title = "Кепка Пятница",
                price = 150,
                category = "Аксессуары",
                description = "Стильный аксессуар для завершения образа.\n" +
                        "Вязание крючком №3.5.\n" +
                        "Пряжа: Рафия (натуральное волокно из листьев пальмы).\n" +
                        "Расход: 1.5 мотка (около 150 метров).\n" +
                        "Кепка держит форму, не боится влаги и отлично защищает от солнца."
            )
        )
    }

    override fun getProducts(): Flow<List<Product>> = flow {
        try {
            val file = File(context.filesDir, fileName)

            // Если файла нет — создаем его и пишем туда дефолтные данные
            if (!file.exists()) {
                file.createNewFile()
                val defaultData = getDefaultProducts()
                val jsonString = jsonParser.encodeToString(defaultData)
                file.writeText(jsonString)
            }

            // Читаем из файла
            val content = file.readText()
            val products = jsonParser.decodeFromString<List<Product>>(content)
            emit(products)

        } catch (e: Exception) {
            e.printStackTrace()
            emit(emptyList())
        }
    }
}