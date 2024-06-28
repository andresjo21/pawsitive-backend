package edu.backend.pawsitive

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface PostService {
    /**
     * Find all Post
     *
     * @return a list of Users
     */
    fun findAll(): List<PostResult>?

    /**
     * Get one Post by id
     *
     * @param id of the Post
     * @return the Post found
     */
    fun findById(id: Long): PostResult?

    /**
     * Save and flush a Post entity in the database
     * @param postInput dto to create
     * @return the saved Post
     */
    fun create(postInput: PostInput): PostResult?

    /**
     * Delte a Post entity in the database
     * @param id to delete Post
     * @return void
     */
    fun delete(id: Long)

}

@Service
class AbstractPostService(
    @Autowired
    val postRepository: PostRepository,
    @Autowired
    val postMapper: PostMapper,
    @Autowired
    val petService: PetService,
    @Autowired
    val userRepository: UserRepository,
    @Autowired
    val userMapper: UserMapper,
) : PostService {
    /**
     * Find all Post
     *
     * @return a list of Users
     */
    override fun findAll(): List<PostResult>? {
        //get logged user
        val user = userRepository.findByEmail(LoggedUser.get())
        var postList= postMapper.postListToPostListResult(
            postRepository.findAll()
        )
        if (user.isPresent) {
            postList.forEach {
                it.isLiked = user.get().favoritePostList?.contains(postMapper.postResultToPost(it)) ?: false
            }
        }
        return postList
    }

    /**
     * Get one Post by id
     *
     * @param id of the Post
     * @return the Post found
     */
    @Throws(NoSuchElementException::class)
    override fun findById(id: Long): PostResult? {
        val post: Optional<Post> = postRepository.findById(id)
        if (post.isEmpty) {
            throw NoSuchElementException("Post with id $id not found")
        }
        return postMapper.postToPostResult(
            post.get(),
        )
    }

    /**
     * Save and flush a Post entity in the database
     * @param postInput dto to create
     * @return the saved Post
     */
    override fun create(postInput: PostInput): PostResult? {
        val post: Post = postMapper.postInputToPost(postInput)
        val pet: Pet? = petService.create(postInput.pet)

        if (post.user == null) {
            val user = userRepository.findByEmail(LoggedUser.get()).orElse(null)
            post.user = user
        }

        if (pet != null) {
            post.pet = pet
        }

        return postMapper.postToPostResult(
            postRepository.save(post)
        )
    }

    /**
     * Delte a Post entity in the database
     * @param id to delete Post
     * @return void
     */
    override fun delete(id: Long) {
        postRepository.deleteById(id)
    }
}

interface PetService {
    /**
     * Find all Pet
     *
     * @return a list of Pet
     */
    fun findAll(): List<PetPostResult>?

    /**
     * Get one Pet by id
     *
     * @param id of the Pet
     * @return the Pet found
     */
    fun findById(id: Long): PetPostResult?

    /**
     * Save and flush a Pet entity in the database
     * @param petInput dto to create
     * @return the saved Pet
     */
    fun create(petInput: PetPostInput): Pet?

    /**
     * Send Prompt to AI about pet to response
     * @param petInput dto to create
     * @return the response from AI (Json String)
     * */
    fun sendPetPrompt(petInput: PetInputAI): String?
}

@Service
class AbstractPetService(
    @Autowired
    val petRepository: PetRepository,
    @Autowired
    val petMapper: PetMapper,

    // strategy interface

    val aiRequestStrategy: AIRequestStrategy,
) : PetService {
    /**
     * Find all Pet
     *
     * @return a list of Pet
     */
    override fun findAll(): List<PetPostResult>? {
        return petMapper.petListToPetListResult(
            petRepository.findAll()
        )
    }

    /**
     * Get one Pet by id
     *
     * @param id of the Pet
     * @return the Pet found
     */
    @Throws(NoSuchElementException::class)
    override fun findById(id: Long): PetPostResult? {
        val pet: Optional<Pet> = petRepository.findById(id)
        if (pet.isEmpty) {
            throw NoSuchElementException("Pet with id $id not found")
        }
        return petMapper.petToPetPostResult(
            pet.get(),
        )
    }

    /**
     * Save and flush a Pet entity in the database
     * @param petInput dto to create
     * @return the saved Pet
     */
    override fun create(petInput: PetPostInput): Pet? {
        val pet: Pet = petMapper.petPostInputToPet(
            petInput,
        )
        return petRepository.save(pet)
    }

    /**
     * Send Prompt to AI about pet to response
     * @param petInput dto to create
     * @return the response from AI (Json String)
     * */
    override fun sendPetPrompt(petInput: PetInputAI): String? {
        /* Build prompt*/
        val prompt = """
        For this pet

        "pet": {,
            "name": "${petInput.name}",
            "address": "${petInput.address}",
            "about": "${petInput.about}",
            "age": ${petInput.age},
            "animalType": {
                "name": "${petInput.animalType.name}",
                "breed": {
                    "name": "${petInput.animalType.breed.name}"
                }
            }
        }

        Based on the animalType and age, return a json with the keys "costs", "cares" and "details" with specific information that i will need if I want to adopt this pet.

        for the "costs" value, return a string with all the information needed, do the same for "cares" and "details", in details add some extra info that I may need

        RETURN ONLY THE JSON PLEASE, NO MORE TEXT IS NEEDED
        
        The response i expect must be like this:
        
        {
            "costs": "STRING COSTS INFO",
            "cares": "STRING CARES INFO",
            "details": "STRING DETAILS INFO"
        }
        
        For each string, please add the information needed to adopt this pet, avoid the age format
        and limit the string to a maximum of 100 words

        AVOID THE MARKDOWN
    """.trimIndent().replace("\n", "").replace("\"", "\\\"")


        //get response from AI
        return aiRequestStrategy.executeAIRequest(prompt)
    }
}

interface UserService {
    /**
     * Get one User by id
     *
     * @param id of the User
     * @return the User found
     */
    fun findById(id: Long): UserProfileResult?

    /**
     * Get the favoritePostList of a User
     * @return favoritePostList of the User
     */
    fun findFavourites(id: Long): List<PostResult>?

    /**
     * Add a post to the list of favourites of a user
     * @param PostResult to add to the list of favourites
     * @return FavouriteResult
     */
    fun addFavourite(post: PostResult): List<PostResult>?

    /**
     * Delete a post from the list of favourites of a user
     * @param id of the post to remove from the list of favourites
     * @return void
     */
    fun removeFavourite(postId: Long)

    /**
     * Register a new user
     * @param user the user to register
     * @return the registered user
     */
    fun signUp(user: UserSignUpInput): Any

    /**
     * Get favorites of the logged user
     * @return A list of elements of FavouriteResult
     */
    fun findMyFavourites(): List<PostResult>?

    /**
     * Get the logged user
     * @return the logged user
     */
    fun getLoggedUser(): UserProfileResult?
    /**
     *  WS to add a post to the list of favourites of the logged user by post id
     *  @param id of the post to add to the list of favourites path parameter
     *  @return FavouriteResult
     * */
    fun addFavouriteById(id: Long): List<PostResult>?

}

@Service
class AbstractUserService(
    @Autowired
    val userRepository: UserRepository,
    @Autowired
    val userMapper: UserMapper,
    @Autowired
    val postMapper: PostMapper,
    @Autowired
    val passwordEncoder: BCryptPasswordEncoder,
    @Autowired
    private val roleRepository: RoleRepository,
    @Autowired
    private val postRepository: PostRepository,
) : UserService {
    /**
     * Get one User by id
     *
     * @param id of the User
     * @return the User found
     */
    @Throws(NoSuchElementException::class)
    override fun findById(id: Long): UserProfileResult? {
        val user: Optional<User> = userRepository.findById(id)
        if (user.isEmpty) {
            throw NoSuchElementException("User with id $id not found")
        }
        return userMapper.userToUserProfileResult(
            user.get(),
        )
    }

    /**
     * Get the favoritePostList of a User
     * @return favoritePostList of the User
     */
    override fun findFavourites(id: Long): List<PostResult>? {
        val user: Optional<User> = userRepository.findById(id)
        if (user.isEmpty) {
            throw NoSuchElementException("User with id $id not found")
        }
        return userMapper.postListToPostListResult(
            user.get().favoritePostList
        )
    }

    /**
     * WS to add a post to the list of favourites of a user
     * @param PostResult to add to the list of favourites
     * @return FavouriteResult
     */
    override fun addFavourite(post: PostResult): List<PostResult>? {
        val email: String = LoggedUser.get()

        val user: Optional<User> = userRepository.findByEmail(email)
        if (user.isEmpty) {
            throw NoSuchElementException("User with email $email not found")
        }

        val favourite = postMapper.postResultToPost(post)

        user.get().favoritePostList?.add(favourite)
        userRepository.save(user.get())

        return userMapper.postListToPostListResult(
            user.get().favoritePostList
        )
    }

    /**
     * Delete a post from the list of favourites of a user
     * @param id of the post to remove from the list of favourites
     * @return void
     */
    override fun removeFavourite(postId: Long) {
        val email: String = LoggedUser.get()

        val user: Optional<User> = userRepository.findByEmail(email)
        if (user.isEmpty) {
            throw NoSuchElementException("User with email $email not found")
        }

        user.get().favoritePostList?.removeIf { it.id == postId }
        userRepository.save(user.get())
    }

    /**
     * Register a new user
     * @param user the user to register
     * @return the registered user
     */
    override fun signUp(user: UserSignUpInput): Any {
        user.email?.let {
            if (userRepository.findByEmail(it).isPresent) {
                throw IllegalArgumentException("User already exists")
            }
        } ?: throw IllegalArgumentException("Email must not be null")

        val encodedPassword = passwordEncoder.encode(user.password)
        val newUser = user.copy(password = encodedPassword)
        val userEntity = userMapper.userSignUpInputToUser(newUser)

        // Obtener el rol existente por su ID (supongamos que el ID del rol es 2 en este ejemplo)
        val roleId = 2L // ID del rol que quieres asignar al usuario
        val existingRole = roleRepository.findById(roleId)
            .orElseThrow { IllegalArgumentException("Role with id $roleId not found") }

        // Agregar el rol existente a la lista de roles del usuario
        userEntity.roleList = mutableSetOf(existingRole)

        if (userRepository.save(userEntity) != null) {
            return UserSignUpResponse(
                success = true
            )
        }

        return UserSignUpResponse(
            success = false
        )
    }

    /**
     * Get favorites of the logged user
     * @return A list of elements of FavouriteResult
     */
    override fun findMyFavourites(): List<PostResult>? {
        val email: String = LoggedUser.get()

        val user: Optional<User> = userRepository.findByEmail(email)
        if (user.isEmpty) {
            throw NoSuchElementException("User with email $email not found")
        }

        return userMapper.postListToPostListResult(
            user.get().favoritePostList
        )
    }

    /**
     * Get the logged user
     * @return the logged user
     */
    override fun getLoggedUser(): UserProfileResult? {
        val email: String = LoggedUser.get()

        val user: Optional<User> = userRepository.findByEmail(email)
        if (user.isEmpty) {
            throw NoSuchElementException("User with email $email not found")
        }

        return userMapper.userToUserProfileResult(user.get())
    }

    /**
     *  WS to add a post to the list of favourites of the logged user by post id
     *  @param id of the post to add to the list of favourites path parameter
     *  @return FavouriteResult
     * */
    override fun addFavouriteById(id: Long): List<PostResult>? {
        val email: String = LoggedUser.get()

        val user: Optional<User> = userRepository.findByEmail(email)
        if (user.isEmpty) {
            throw NoSuchElementException("User with email $email not found")
        }

        val post: Optional<Post> = postRepository.findById(id)
        if (post.isEmpty) {
            throw NoSuchElementException("Post with id $id not found")
        }

        user.get().favoritePostList?.add(post.get())
        userRepository.save(user.get())

        return userMapper.postListToPostListResult(
            user.get().favoritePostList
        )
    }
}

// animal type service
interface AnimalTypeService {
    /**
     * Find all AnimalType
     *
     * @return a list of AnimalType
     */
    fun findAll(): List<AnimalTypeResult>?

    /**
     * Get one AnimalType by id
     *
     * @param id of the AnimalType
     * @return the AnimalType found
     */
    fun findById(id: Long): AnimalTypeResult?

    /**
     * Save and flush a AnimalType entity in the database
     * @param animalTypeInput dto to create
     * @return the saved AnimalType
     */
    fun create(animalTypeInput: AnimalTypeInput): AnimalTypeResult?
}

@Service
class AbstractAnimalTypeService(
    @Autowired
    val animalTypeRepository: AnimalTypeRepository,
    @Autowired
    val animalTypeMapper: AnimalTypeMapper,
) : AnimalTypeService {
    /**
     * Find all AnimalType
     *
     * @return a list of AnimalType
     */
    override fun findAll(): List<AnimalTypeResult>? {
        return animalTypeMapper.animalTypeListToAnimalTypeListResult(
            animalTypeRepository.findAll()
        )
    }

    /**
     * Get one AnimalType by id
     *
     * @param id of the AnimalType
     * @return the AnimalType found
     */
    @Throws(NoSuchElementException::class)
    override fun findById(id: Long): AnimalTypeResult? {
        val animalType: Optional<AnimalType> = animalTypeRepository.findById(id)
        if (animalType.isEmpty) {
            throw NoSuchElementException("AnimalType with id $id not found")
        }
        return animalTypeMapper.animalTypeToAnimalTypeResult(
            animalType.get(),
        )
    }

    /**
     * Save and flush a AnimalType entity in the database
     * @param animalTypeInput dto to create
     * @return the saved AnimalType
     */
    override fun create(animalTypeInput: AnimalTypeInput): AnimalTypeResult? {
        val animalType: AnimalType = animalTypeMapper.animalTypeInputToAnimalType(
            animalTypeInput,
        )
        return animalTypeMapper.animalTypeToAnimalTypeResult(
            animalTypeRepository.save(animalType),
        )
    }
}

// breed service
interface BreedService {
    /**
     * Find all Breed
     *
     * @return a list of Breed
     */
    fun findAll(): List<BreedResult>?

    /**
     * Get one Breed by id
     *
     * @param id of the Breed
     * @return the Breed found
     */
    fun findById(id: Long): BreedResult?

    /**
     * Save and flush a Breed entity in the database
     * @param breedInput dto to create
     * @return the saved Breed
     */
    fun create(breedInput: BreedInput): BreedResult?
}

@Service
class AbstractBreedService(
    @Autowired
    val breedRepository: BreedRepository,
    @Autowired
    val breedMapper: BreedMapper,
    @Autowired
    val animalTypeRepository: AnimalTypeRepository,
) : BreedService {
    /**
     * Find all Breed
     *
     * @return a list of BreedResult
     */
    override fun findAll(): List<BreedResult>? {
        return breedMapper.breedListToBreedListResult(
            breedRepository.findAll()
        )
    }

    /**
     * Get one Breed by id
     *
     * @param id of the Breed
     * @return the Breed found
     */
    @Throws(NoSuchElementException::class)
    override fun findById(id: Long): BreedResult? {
        val breed: Optional<Breed> = breedRepository.findById(id)
        if (breed.isEmpty) {
            throw NoSuchElementException("Breed with id $id not found")
        }
        return breedMapper.breedToBreedResult(
            breed.get(),
        )
    }

    /**
     * Save and flush a Breed entity in the database
     * @param breedInput dto to create
     * @return the saved Breed
     */
    override fun create(breedInput: BreedInput): BreedResult? {
        val breed: Breed = breedMapper.breedInputToBreed(
            breedInput,
        )

        // find animal type by id
        val id = breed.animalType.id
        val animalType = id?.let { animalTypeRepository.findById(it) }
        println(animalType)
        if (animalType != null) {
            breed.animalType = animalType.get()
        }


        return breedMapper.breedToBreedResult(
            breedRepository.save(breed),
        )
    }
}

// post type service
interface PostTypeService {
    /**
     * Find all PostType
     *
     * @return a list of PostType
     */
    fun findAll(): List<PostTypeResult>?

    /**
     * Get one PostType by id
     *
     * @param id of the PostType
     * @return the PostType found
     */
    fun findById(id: Long): PostTypeResult?

    /**
     * Save and flush a PostType entity in the database
     * @param postTypeInput dto to create
     * @return the saved PostType
     */
    fun create(postTypeInput: PostTypeInput): PostTypeResult?
}

@Service
class AbstractPostTypeService(
    @Autowired
    val postTypeRepository: PostTypeRepository,
    @Autowired
    val postTypeMapper: PostTypeMapper,
) : PostTypeService {
    /**
     * Find all PostType
     *
     * @return a list of PostType
     */
    override fun findAll(): List<PostTypeResult>? {
        return postTypeMapper.postTypeListToPostTypeListResult(
            postTypeRepository.findAll()
        )
    }

    /**
     * Get one PostType by id
     *
     * @param id of the PostType
     * @return the PostType found
     */
    @Throws(NoSuchElementException::class)
    override fun findById(id: Long): PostTypeResult? {
        val postType: Optional<PostType> = postTypeRepository.findById(id)
        if (postType.isEmpty) {
            throw NoSuchElementException("PostType with id $id not found")
        }
        return postTypeMapper.postTypeToPostTypeResult(
            postType.get(),
        )
    }

    /**
     * Save and flush a PostType entity in the database
     * @param postTypeInput dto to create
     * @return the saved PostType
     */
    override fun create(postTypeInput: PostTypeInput): PostTypeResult? {
        val postType: PostType = postTypeMapper.postTypeInputToPostType(
            postTypeInput,
        )
        return postTypeMapper.postTypeToPostTypeResult(
            postTypeRepository.save(postType),
        )
    }
}

@Service
@Transactional
class AppUserDetailsService(
    @Autowired
    val userRepository: UserRepository,
    @Autowired
    val roleRepository: RoleRepository,
) : UserDetailsService {

    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may be case sensitive, or case insensitive depending on how the
     * implementation instance is configured. In this case, the `UserDetails`
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never `null`)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     * GrantedAuthority
     */
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val userAuth: org.springframework.security.core.userdetails.User
        val user: User = userRepository.findByEmail(username).orElse(null)
            ?: return org.springframework.security.core.userdetails.User(
                "", "", true, true, true, true,
                getAuthorities(
                    listOf(
                        roleRepository.findByName("ROLE_USER").get()
                    )
                )
            )

        userAuth = org.springframework.security.core.userdetails.User(
            user.email, user.password, user.enabled, true, true,
            true, getAuthorities(user.roleList!!.toMutableList())
        )

        return userAuth
    }

    private fun getAuthorities(roles: Collection<Role>): Collection<GrantedAuthority> {
        return roles.flatMap { role ->
            sequenceOf(SimpleGrantedAuthority(role.name)) +
                    role.privilegeList.map { privilege -> SimpleGrantedAuthority(privilege.name) }
        }.toList()
    }

}