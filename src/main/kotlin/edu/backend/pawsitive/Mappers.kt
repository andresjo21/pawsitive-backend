package edu.backend.pawsitive

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = [ImageMapper::class])
interface PostMapper {
    fun postToPostResult(
        post: Post
    ) : PostResult

    fun postListToPostListResult(
        postList: List<Post>
    ) : List<PostResult>

    // post input

    fun postInputToPost(
        postInput: PostInput
    ) : Post

    fun postResultToPost(
        postResult: PostResult
    ) : Post
}

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface UserMapper {
    fun userToUserProfileResult(
        user: User
    ) : UserProfileResult

    fun postListToPostListResult(
        postList: MutableSet<Post>?
    ) : List<PostResult>

    //Crear mapper para UserPostResult to User que ponga en vacio los campos que no se necesitan
    fun userPostResultToUser(
        userPostResult: UserPostResult
    ) : User

    fun userToUserPostJWT(
        user: User
    ) : UserPostJWT

    @Mapping(target = "createDate", defaultExpression = "java(new java.util.Date())")
    fun userSignUpInputToUser(user: UserSignUpInput) : User
}

//animal type mapper
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface AnimalTypeMapper {
    fun animalTypeToAnimalTypeResult(
        animalType: AnimalType
    ) : AnimalTypeResult

    fun animalTypeListToAnimalTypeListResult(
        animalTypeList: List<AnimalType>
    ) : List<AnimalTypeResult>

    fun animalTypeInputToAnimalType(
        animalTypeInput: AnimalTypeInput
    ) : AnimalType

    fun animalTypeAItoAnimalType(
        animalTypeAI: AnimalTypeAI
    ) : AnimalType

}

//breed mapper
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface BreedMapper {
    fun breedToBreedResult(
        breed: Breed
    ) : BreedResult

    fun breedListToBreedListResult(
        breedList: List<Breed>
    ) : List<BreedResult>

    fun breedInputToBreed(
        breedInput: BreedInput
    ) : Breed

    fun BreedAItoBreed(
        breedAI: BreedAI
    ) : Breed
}

// post type mapper
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface PostTypeMapper {
    fun postTypeToPostTypeResult(
        postType: PostType
    ) : PostTypeResult

    fun postTypeListToPostTypeListResult(
        postTypeList: List<PostType>
    ) : List<PostTypeResult>

    fun postTypeInputToPostType(
        postTypeInput: PostTypeInput
    ) : PostType
}

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface ImageMapper {
    fun imageToImageResult(image: Image): ImageResult
    fun imageListToImageResultList(imageList: List<Image>): List<ImageResult>
}

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface PetMapper {
    fun petToPetPostResult(pet: Pet): PetPostResult
    fun petListToPetListResult(petList: List<Pet>): List<PetPostResult>
    fun petPostInputToPet(petInput: PetPostInput): Pet
    fun PetInputAItoPet(petAI: PetInputAI): Pet
}