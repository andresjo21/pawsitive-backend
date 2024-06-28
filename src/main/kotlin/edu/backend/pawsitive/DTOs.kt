package edu.backend.pawsitive

import com.fasterxml.jackson.annotation.JsonFormat
import java.util.Date

/*
* * Crear los DTOs con la siguiente estructura:
* * EntityName + Prefijo
* * Ejemplo: UserLoginInput o UserResult
*/

data class PostResult(
    val id: Long,
    val show: Boolean,
    val postType: PostTypeResult,
    //val image: MutableSet<ImageResult>,
    val user: UserPostResult,
    val pet: PetPostResult,
    // flag para manejar si el usuario logueado tiene like en el post
    var isLiked: Boolean = false
)

// newPost input
data class PostInput(
    val show: Boolean,
    val postType: PostTypeInput,
    //val imageList: Set<ImageInput>,
    val pet: PetPostInput,
    var user: UserPostJWT? = null
)

data class UserPostJWT(
    val id: Long
)

data class PostTypeResult(
    val id: Long,
    val name: String
)

// PostType input
data class PostTypeInput(
    val id: Long,
    val name: String
)

data class ImageResult(
    val id: Long,
    val fileName: String
)

// Image input
data class ImageInput(
    val name: String
)

data class UserPostResult(
    val id: Long,
    val email: String
)

data class PetPostResult(
    val id: Long,
    val name: String,
    val address: String,
    val about: String,
    val age: Date,
    val breed: BreedPostResult,
)

//pet post input
data class PetPostInput(
    val name: String,
    val address: String,
    val about: String,
    @JsonFormat(pattern="dd/MM/yyyy")
    val age: Date,
    val breed: BreedPostInput
)


data class AnimalTypePostResult(
    val id: Long,
    val name: String,
    val breed: BreedPostResult
)

//animal type post input
data class AnimalTypePostInput(
    val id: Long? = null,
)

data class AnimalTypeInput(
    val name: String
)

data class AnimalTypeResult(
    val id: Long,
    val name: String,
)

//animal type breed output
data class AnimalTypeBreedResult(
    val id: Long,
    val name: String,
)

data class AnimalTypeBreedInput(
    val id: Long? = null,
    val name: String,
)

data class BreedPostResult(
    val id: Long,
    val name: String,
    val animalType: AnimalTypeBreedResult
)

data class BreedInput(
    val name: String,
    val animalType: AnimalTypeBreedInput
)

//breed post input
data class BreedPostInput(
    val id: Long? = null,
    val name: String,
    val animalType: AnimalTypeBreedInput
)

//breed output
data class BreedResult(
    val id: Long,
    val name: String,
    val animalType: AnimalTypeBreedResult
)

data class UserProfileResult(
    var userId: Long? = null,
    var email: String? = null,
    var enabled: Boolean? = null,
    var firstName: String? = null,
    var lastName: String? = null,
)

data class UserLoginInput(
    var username: String = "",
    var password: String = ""
)

data class UserSignUpInput(
    var email: String? = null,
    var username: String? = null,
    var password: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var createDate: Date? = null,
    var enabled: Boolean = true,
    var tokenExpired: Boolean = false
)

data class UserSignUpResponse(
    var success: Boolean? = null,
)

// users from Maikos's task app project

data class UserInput(
    var id: Long?=null,
    var firstName: String?=null,
    var lastName: String?=null,
    var email: String?=null,
    var password: String?=null,
    var enabled: Boolean?=null,
)

data class UserResult(
    var id: Long,
    var firstName: String,
    var lastName: String,
    var email: String,
    var password: String,
    var enabled: Boolean?,
    var tokenExpired: Boolean?,
    var createDate: Date,
)

//for ai
data class PetInputAI(
    val name: String,
    val address: String,
    val about: String,
    val age: Date,
    val animalType: AnimalTypeAI
)

data class AnimalTypeAI(
    val name: String,
    val breed: BreedAI
)

data class BreedAI(
    val name: String
)