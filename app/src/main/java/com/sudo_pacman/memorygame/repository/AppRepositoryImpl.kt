package com.sudo_pacman.memorygame.repository


import com.sudo_pacman.memorygame.R
import com.sudo_pacman.memorygame.data.model.CardData
import com.sudo_pacman.memorygame.data.model.LevelEnum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AppRepositoryImpl private constructor() : AppRepository {
    companion object {
        private lateinit var instance: AppRepositoryImpl

        fun getInstance(): AppRepositoryImpl {
            if (!(Companion::instance.isInitialized))
                instance = AppRepositoryImpl()
            return instance
        }
    }

    private val images: ArrayList<CardData> = ArrayList()
    private val result = ArrayList<CardData>()

    init {
        initImages()
    }

    override fun getImagesByLevel(level: LevelEnum): Flow<List<CardData>> = flow {

        val count = level.rowCount * level.columnCount / 2

        result.addAll(images.subList(0, count))
        result.addAll(images.subList(0, count))
//        result.shuffle()

        emit(result)
    }.flowOn(Dispatchers.IO)


    override fun checkOpens(openedImages: ArrayList<Int>): Boolean {
        return result[openedImages[0]] == result[openedImages[1]]
    }


    private fun initImages() {
        images.add(CardData(1, R.drawable.monkey))
        images.add(CardData(1, R.drawable.monkey))
        images.add(CardData(1, R.drawable.monkey))
        images.add(CardData(1, R.drawable.monkey))
        images.add(CardData(1, R.drawable.monkey))
        images.add(CardData(1, R.drawable.monkey))
        images.add(CardData(1, R.drawable.monkey))
        images.add(CardData(1, R.drawable.monkey))
        images.add(CardData(1, R.drawable.monkey))
        images.add(CardData(1, R.drawable.monkey))
        images.add(CardData(1, R.drawable.monkey))
        images.add(CardData(1, R.drawable.monkey))
        images.add(CardData(1, R.drawable.monkey))
        images.add(CardData(1, R.drawable.monkey))
        images.add(CardData(1, R.drawable.monkey))
        images.add(CardData(1, R.drawable.monkey))
        images.add(CardData(1, R.drawable.monkey))
        images.add(CardData(1, R.drawable.monkey))
        images.add(CardData(1, R.drawable.monkey))
        images.add(CardData(1, R.drawable.monkey))
        images.add(CardData(1, R.drawable.monkey))
        images.add(CardData(1, R.drawable.monkey))
        images.add(CardData(1, R.drawable.monkey))
        images.add(CardData(1, R.drawable.monkey))
        images.add(CardData(1, R.drawable.monkey))
        images.add(CardData(1, R.drawable.monkey))
        images.add(CardData(1, R.drawable.monkey))
        images.add(CardData(1, R.drawable.monkey))
        images.add(CardData(1, R.drawable.monkey))
        images.add(CardData(1, R.drawable.monkey))
        images.add(CardData(1, R.drawable.monkey))
//        images.add(CardData(2, R.drawable.image_2))
//        images.add(CardData(3, R.drawable.image_3))
//        images.add(CardData(4, R.drawable.image_4))
//        images.add(CardData(5, R.drawable.image_5))
//        images.add(CardData(6, R.drawable.image_6))
//        images.add(CardData(7, R.drawable.image_7))
//        images.add(CardData(8, R.drawable.image_8))
//        images.add(CardData(9, R.drawable.image_9))
//        images.add(CardData(10, R.drawable.image_10))
//        images.add(CardData(11, R.drawable.image_11))
//        images.add(CardData(12, R.drawable.image_12))
//        images.add(CardData(13, R.drawable.image_13))
//        images.add(CardData(14, R.drawable.image_14))
//        images.add(CardData(15, R.drawable.image_15))
//        images.add(CardData(16, R.drawable.image_16))
//        images.add(CardData(17, R.drawable.image_17))
//        images.add(CardData(18, R.drawable.image_18))
//        images.add(CardData(19, R.drawable.image_19))
//        images.add(CardData(20, R.drawable.image_20))
//        images.add(CardData(21, R.drawable.image_21))
//        images.add(CardData(22, R.drawable.image_22))
//        images.add(CardData(23, R.drawable.image_23))
//        images.add(CardData(24, R.drawable.image_24))
//        images.add(CardData(25, R.drawable.image_25))
//        images.add(CardData(26, R.drawable.image_26))
//        images.add(CardData(27, R.drawable.image_27))
    }
}