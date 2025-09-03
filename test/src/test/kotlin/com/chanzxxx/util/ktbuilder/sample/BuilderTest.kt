package com.chanzxxx.util.ktbuilder.sample

import com.chazxxx.util.ktbuilder.Company
import com.chazxxx.util.ktbuilder.Person
import com.chazxxx.util.ktbuilder.PersonBuilder
import com.chazxxx.util.ktbuilder.ktBuilderFor
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class BuilderTest {

    @Test
    fun `builder should correctly set fields and build object`() {
        val person = PersonBuilder()
            .name("Alice")
            .age(30)
            .build()

        assertEquals("Alice", person.name)
        assertEquals(30, person.age)
    }

    @Test
    fun `builder extension test`() {
        val person = ktBuilderFor(Person::class)
            .name("Alice")
            .age(30)
            .build()

        assertEquals("Alice", person.name)
        assertEquals(30, person.age)
    }

    @Test
    fun `builder extension test 2`() {
        val company = ktBuilderFor(Company::class)
            .name("wow")
            .build()

        assertEquals("wow", company.name)
    }


    @Test
    fun `builder should allow multiple build calls with different values`() {
        val builder = PersonBuilder()
            .name("Alice")
            .age(30)

        val person1 = builder.build()
        assertEquals("Alice", person1.name)
        assertEquals(30, person1.age)

        builder.name("Bob").age(25)
        val person2 = builder.build()
        assertEquals("Bob", person2.name)
        assertEquals(25, person2.age)
    }
}
