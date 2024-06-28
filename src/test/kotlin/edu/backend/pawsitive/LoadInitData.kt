package edu.backend.pawsitive

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql

@SpringBootTest
@Sql("/import-roles.sql",
    "/import-postType.sql",
    "/import-privilege.sql",
    "/import-users.sql",
    "/import-animalType.sql" ,
    "/import-breed.sql",
    "/import-pet.sql",
    "/import-post.sql",
    "/import-image.sql")

class LoadInitData(
    @Autowired
    val postRepository: PostRepository,
){
    @Test
    fun testPostFindAll(){
        val postList: List<Post> = postRepository.findAll()
        postList.forEach { println(it) }
        Assertions.assertTrue(postList.size == 2)
    }

}