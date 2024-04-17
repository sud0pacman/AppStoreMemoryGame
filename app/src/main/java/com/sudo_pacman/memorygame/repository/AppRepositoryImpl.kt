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
    private var result = ArrayList<CardData>()

    init {
        initImages()
    }

    override fun getImagesByLevel(level: LevelEnum): Flow<List<CardData>> = flow {
        val count = level.rowCount * level.columnCount / 2

        val ls = images.subList(0, count)
        val result = ArrayList<CardData>(ls)
        result.addAll(ls)

        result.shuffle()
        emit(result)

//        val shuffledImages = images
//
//        val count = level.rowCount * level.columnCount / 2
//
//        result.addAll(shuffledImages.subList(0, count))
//        result.addAll(shuffledImages.subList(0, count))
//        result = result.shuffled() as ArrayList<CardData>
//
//        emit(result)
    }.flowOn(Dispatchers.IO)


    override fun checkOpens(openedImages: ArrayList<Int>): Boolean {
        return result[openedImages[0]] == result[openedImages[1]]
    }


    private fun initImages() {
        images.add(CardData(1, R.drawable.img1))
        images.add(CardData(2, R.drawable.img2))
        images.add(CardData(3, R.drawable.img3))
        images.add(CardData(4, R.drawable.img4))
        images.add(CardData(5, R.drawable.img5))
        images.add(CardData(6, R.drawable.img6))
        images.add(CardData(7, R.drawable.img7))
        images.add(CardData(8, R.drawable.img8))
        images.add(CardData(9, R.drawable.img9))
        images.add(CardData(10, R.drawable.img10))
        images.add(CardData(11, R.drawable.img11))
        images.add(CardData(12, R.drawable.img12))
        images.add(CardData(13, R.drawable.img13))
        images.add(CardData(14, R.drawable.img14))
        images.add(CardData(15, R.drawable.img15))
        images.add(CardData(16, R.drawable.img16))
        images.add(CardData(17, R.drawable.img17))
        images.add(CardData(18, R.drawable.img18))
        images.add(CardData(19, R.drawable.img19))
        images.add(CardData(20, R.drawable.img20))
        images.add(CardData(21, R.drawable.img21))
        images.add(CardData(22, R.drawable.img22))
        images.add(CardData(23, R.drawable.img23))
        images.add(CardData(23, R.drawable.img24))
        images.add(CardData(23, R.drawable.img25))
        images.add(CardData(23, R.drawable.img26))
        images.add(CardData(23, R.drawable.img27))
        images.add(CardData(23, R.drawable.img28))
        images.add(CardData(23, R.drawable.img29))
        images.add(CardData(23, R.drawable.img30))
    }
}