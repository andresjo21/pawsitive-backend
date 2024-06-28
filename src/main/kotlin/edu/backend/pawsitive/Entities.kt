package edu.backend.pawsitive

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var password: String? = null,
    var email: String? = null,
    var createDate: Date? = null,
    var enabled: Boolean,
    var tokenExpired: Boolean? = null,
    // Entity Relationship
    @ManyToMany
    @JoinTable(
        name = "user_role",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")]
    )
    var roleList: MutableSet<Role>? = null,
    // post relationship
    @OneToMany(mappedBy = "user")
    var postList: List<Post>? = null,
    // favorite posts relationship
    @ManyToMany
    @JoinTable(
        name = "user_favorite_post",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "post_id", referencedColumnName = "id")]
    )
    var favoritePostList: MutableSet<Post>? = null,

    // image relationship
    @ManyToMany(mappedBy = "userList")
    var imageList: MutableSet<Image>? = null,

    ) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is User) return false

        if (id != other.id) return false
        if (email != other.email) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + email.hashCode()
        return result
    }

    override fun toString(): String {
        return "User(id=$id, firstName='$firstName', lastName='$lastName', email='$email', createDate=$createDate, enabled=$enabled, tokenExpired=$tokenExpired"
    }

}

@Entity
@Table(name = "pet")
data class Pet(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    @Column(name = "name")
    var name: String,
    @Column(name = "address")
    var address: String,
    @Column(name = "about")
    var about: String,
    @Column(name = "age")
    var age: Date,
    // Entity Relationship
    // animal type relationship
    @ManyToOne
    @JoinColumn(name = "breed_id", nullable = false, referencedColumnName = "id")
    var breed: Breed,
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Pet) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "Pets(id=$id, name='$name', address='$address', about='$about', age=$age, breed=$breed)"
    }
}

@Entity
@Table(name = "privilege")
data class Privilege(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id:Long? = null,
    var name: String,
    // Entity Relationship
    @ManyToMany(fetch = FetchType.LAZY)
    var userList: MutableSet<User>,
    @ManyToMany(fetch = FetchType.LAZY)
    var roleList: MutableSet<Role>,
    ) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Privilege) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "Privilege(id=$id, name='$name')"
    }
}

@Entity
@Table(name = "role")
data class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id:Long? = null,
    @Column(name = "name")
    var name: String,
    // Entity Relationship
    @ManyToMany
    @JoinTable(
        name = "role_privilege",
        joinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "privilege_id", referencedColumnName = "id")]
    )
    var privilegeList: MutableSet<Privilege>,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Role) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "Role(id=$id, name='$name')"
    }
}

@Entity
@Table(name = "breed")
data class Breed(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id:Long? = null,
    @Column(name = "name")
    var name: String,
    // Entity Relationship

    // pet relationship
    @OneToMany(mappedBy = "breed")
    var petList: List<Pet>? = null,

    // animal type relationship
    @ManyToOne
    @JoinColumn(name = "animal_type_id", nullable = false, referencedColumnName = "id")
    var animalType: AnimalType,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Breed) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "Breed(id=$id, name='$name', animalType=$animalType)"
    }
}

@Entity
@Table(name = "post")
data class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    @Column(name = "show")
    var show: Boolean,
    // Entity Relationship

    // user relationships

    // user posts
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    var user: User? = null,

    // user favorite posts
    @ManyToMany(mappedBy = "favoritePostList")
    var favoriteUserList: MutableSet<User>? = null,

    // pet relationship
    @OneToOne
    @JoinColumn(name = "pet_id", nullable = false, referencedColumnName = "id")
    var pet: Pet,

    // image relationship
    @ManyToMany(mappedBy = "postList")
    var imageList: MutableSet<Image>? = null,

    // post type relationship
    @ManyToOne
    @JoinColumn(name = "post_type_id", nullable = false, referencedColumnName = "id")
    var postType: PostType,

){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Post) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "Posts(id=$id, show=$show, user=$user, pet=$pet, postType=$postType)"
    }
}

@Entity
@Table(name = "animal_types")
data class AnimalType(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id:Long? = null,
    @Column(name = "name")
    var name: String? = null,
    // Entity Relationship

    // pet relationship
    //@OneToMany(mappedBy = "animalType")
    //var petList: List<Pet>? = null,

    // breed relationship
    @OneToMany(mappedBy = "animalType")
    var breedList: List<Breed>?= null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AnimalType) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "AnimalType(id=$id, name='$name')"
    }
}

@Entity
@Table(name = "image")
data class Image(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    @Column(name = "file_name")
    var fileName : String,
    // Entity Relationship

    // post relationship
    //post can have multiple images
    @ManyToMany
    @JoinTable(
        name = "post_image",
        joinColumns = [JoinColumn(name = "image_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "post_id", referencedColumnName = "id")]
    )
    var postList: MutableSet<Post>? = null,

    // user relationship
    // user can have multiple images
    @ManyToMany
    @JoinTable(
        name = "user_image",
        joinColumns = [JoinColumn(name = "image_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")]
    )
    var userList: MutableSet<User>? = null,
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Image) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "Image(id=$id, fileName='$fileName')"
    }
}

@Entity
@Table(name = "post_type")
data class PostType(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    @Column(name = "name")
    var name: String,
    // Entity Relationship

    // post relationship
    @OneToMany(mappedBy = "postType")
    var postList: List<Post>? = null,
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PostType) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "PostType(id=$id, name='$name')"
    }
}
