package edu.backend.pawsitive

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.NoSuchElementException

// home
@RestController
@RequestMapping("/")
class HomeController {
    @GetMapping
    fun home() = "Welcome to Pawsitive API"
}
@RestController
@RequestMapping("\${url.breeds}")
class BreedController(private val breedService: BreedService) {
    /**
     * WS to find all elements of type Breed
     * @return A list of elements of type Breed
     */
    @GetMapping
    @ResponseBody
    fun findAll() = breedService.findAll()

    /**
     * WS to find one Breed by the id
     * @param id to find Breed
     * @return the Breed found
     */
    @Throws(NoSuchElementException::class)
    @GetMapping("{id}")
    @ResponseBody
    fun findById(@PathVariable id:Long) = breedService.findById(id)

    /**
     * WS to create a new Breed
     * @param breedInput to create
     * @return the created Breed
     */
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun create(@RequestBody breedInput: BreedInput) : BreedResult? {
        return breedService.create(breedInput)
    }
}

@RestController
@RequestMapping("\${url.animalTypes}")
class AnimalTypeController(private val animalTypeService: AnimalTypeService) {
    /**
     * WS to find all elements of type AnimalType
     * @return A list of elements of type AnimalType
     */
    @GetMapping
    @ResponseBody
    fun findAll() = animalTypeService.findAll()

    /**
     * WS to find one AnimalType by the id
     * @param id to find AnimalType
     * @return the AnimalType found
     */
    @Throws(NoSuchElementException::class)
    @GetMapping("{id}")
    @ResponseBody
    fun findById(@PathVariable id:Long) = animalTypeService.findById(id)

    /**
     * WS to create a new AnimalType
     * @param animalTypeInput to create
     * @return the created AnimalType
     */
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun create(@RequestBody animalTypeInput: AnimalTypeInput) : AnimalTypeResult? {
        return animalTypeService.create(animalTypeInput)
    }
}

// post type web service
@RestController
@RequestMapping("\${url.postTypes}")
class PostTypeController(private val postTypeService: PostTypeService) {
    /**
     * WS to find all elements of type PostType
     * @return A list of elements of type PostType
     */
    @GetMapping
    @ResponseBody
    fun findAll() = postTypeService.findAll()

    /**
     * WS to find one PostType by the id
     * @param id to find PostType
     * @return the PostType found
     */
    @Throws(NoSuchElementException::class)
    @GetMapping("{id}")
    @ResponseBody
    fun findById(@PathVariable id:Long) = postTypeService.findById(id)

    /**
     * WS to create a new PostType
     * @param postTypeInput to create
     * @return the created PostType
     */
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun create(@RequestBody postTypeInput: PostTypeInput) : PostTypeResult? {
        return postTypeService.create(postTypeInput)
    }
}

// user web service
@RestController
@RequestMapping("\${url.user}")
class UserController(private val userService: UserService) {
    /**
     * WS to find one User by the id
     * @param id to find User
     * @return the User found
     */
    @Throws(NoSuchElementException::class)
    @GetMapping("{id}")
    @ResponseBody
    fun findById(@PathVariable id:Long) = userService.findById(id)

    /**
     * WS to logout user
     * endpoint: /${url.user}/logout
     * @return void
     * */
    @PostMapping("/logout")
    @ResponseBody
    fun logout() = LoggedUser.logOut()

    /**
     * WS to register a new user
     * endpoint: /${url.user}/signup
     * @param user the user to register
     * @return the registered user
     */
    @PostMapping("/signup")
    @ResponseBody
    fun signUp(@RequestBody user: UserSignUpInput) = userService.signUp(user)

    /**
     * WS to get the logged user
     * @return the logged user
     */
    @Throws(NoSuchElementException::class)
    @GetMapping("/me")
    @ResponseBody
    fun getLoggedUser() = userService.getLoggedUser()
}

// post web service
@RestController
@RequestMapping("\${url.posts}")
class PostController(private val postService: PostService) {
    /**
     * WS to find all elements of type Post
     * @return A list of elements of type Post
     */
    @GetMapping
    @ResponseBody
    fun findAll() = postService.findAll()

    /**
     * WS to find one Post by the id
     * @param id to find Post
     * @return the Post found
     */
    @Throws(NoSuchElementException::class)
    @GetMapping("{id}")
    @ResponseBody
    fun findById(@PathVariable id:Long) = postService.findById(id)

    /**
     * WS to create a new Post
     * @param postInput to create
     * @return the created Post
     */
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun create(@RequestBody postInput: PostInput) : PostResult? {
        return postService.create(postInput)
    }

    /**
     * WS to delete a Post by the id
     * @param id to delete Post
     * @return void
     */
    @Throws(NoSuchElementException::class)
    @DeleteMapping("{id}")
    @ResponseBody
    fun delete(@PathVariable id:Long) = postService.delete(id)
}

@RestController
@RequestMapping("\${url.favourites}")
class FavouriteController(private val userService: UserService) {
    /**
     * WS to find the list of favourites of a user
     * @return A list of elements of FavouriteResult
     */
    //TODO: Implement with user in JWT
    @Throws(NoSuchElementException::class)
    @GetMapping("{id}")
    @ResponseBody
    fun findFavourites(@PathVariable id:Long) = userService.findFavourites(id)

    /**
     * WS to add a post to the list of favourites of a user
     * @param PostResult to add to the list of favourites
     * @return FavouriteResult
     */
    @PutMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun addFavourite(@RequestBody post: PostResult) = userService.addFavourite(post)

    /**
     * WS to remove a post from the list of favourites of a user
     * @param id of the post to remove from the list of favourites
     * @return void
     */
    @Throws(NoSuchElementException::class)
    @DeleteMapping("{id}")
    @ResponseBody
    fun removeFavourite(@PathVariable id:Long) = userService.removeFavourite(id)

    /**
     * WS to get /me favorites of the logged user
     * @return A list of elements of FavouriteResult
     */
    @Throws(NoSuchElementException::class)
    @GetMapping("/me")
    @ResponseBody
    fun findMyFavourites() = userService.findMyFavourites()

    /**
     *  WS to add a post to the list of favourites of the logged user by post id
     *  @param id of the post to add to the list of favourites path parameter
     *  @return FavouriteResult
     * */
    @PutMapping("/me/{id}")
    @ResponseBody
    fun addMyFavourite(@PathVariable id:Long) = userService.addFavouriteById(id)
}

// pets ai web service
@RestController
@RequestMapping("\${url.petsAI}")
class PetsAIController(private val PetService:PetService){
    /**
     * WS to send AI request
     * @param PetInput to send
     * @return the response from the AI
     * */
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun getAIJson(@RequestBody petInput: PetInputAI) : String? {
        return PetService.sendPetPrompt(petInput)
    }
}