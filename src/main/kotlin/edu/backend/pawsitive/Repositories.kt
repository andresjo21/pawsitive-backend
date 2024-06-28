package edu.backend.pawsitive

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PostRepository : JpaRepository<Post, Long>

@Repository
interface UserRepository : JpaRepository<User, Long>{
    fun findByEmail(@Param("email") email: String): Optional<User>
}

@Repository
interface BreedRepository : JpaRepository<Breed, Long>

@Repository
interface AnimalTypeRepository : JpaRepository<AnimalType, Long>

@Repository
interface PostTypeRepository : JpaRepository<PostType, Long>

@Repository
interface PetRepository : JpaRepository<Pet, Long>

@Repository
interface RoleRepository : JpaRepository<Role, Long>{
    fun findByName(@Param("name") name: String): Optional<Role>
}