package com.example.lifechanger.data

import com.example.lifechanger.R
import com.example.lifechanger.data.model.Category

class CategoryRepository() {

    fun loadEnvironmentCategory(): MutableList<Category> {
        return mutableListOf(
            Category(
                R.string.categoryTitleClimate,
                R.drawable.climate
            ),
            Category(
                R.string.categoryTitleWater,
                R.drawable.water
            ),
            Category(
                R.string.categoryTitleGarbage,
                R.drawable.garbage
            ),
            Category(
                R.string.categoryTitleDisaster,
                R.drawable.disaster
            )
        )
    }

    fun loadAnimalCategory(): MutableList<Category> {
        return mutableListOf(
            Category(
                R.string.categoryTitleAnimalRights,
                R.drawable.animalrights
            ),
            Category(
                R.string.categoryTitleSpeciesProtection,
                R.drawable.speciesprotection
            ),
            Category(
                R.string.categoryTitleCats,
                R.drawable.cat
            ),
            Category(
                R.string.categoryTitleDogs,
                R.drawable.dog
            ),
            Category(
                R.string.categoryTitleHorses,
                R.drawable.horse
            )
        )
    }

    fun loadPeopleCategory(): MutableList<Category> {
        return mutableListOf(
            Category(
                R.string.categoryTitleKids,
                R.drawable.kids
            ),
            Category(
                R.string.categoryTitleSeniors,
                R.drawable.seniors
            ), Category(
                R.string.categoryTitleHomeless,
                R.drawable.homeless
            ),
            Category(
                R.string.categoryTitleRefugees,
                R.drawable.refugees
            ),
            Category(
                R.string.categoryTitleInclusion,
                R.drawable.inclusion
            )
        )
    }

    fun loadDevelopmentAidCategory(): MutableList<Category> {
        return mutableListOf(
            Category(
                R.string.categoryTitleEducation,
                R.drawable.education
            ),
            Category(
                R.string.categoryTitleInfrastructure,
                R.drawable.infrastructure
            )
        )
    }
}