package com.teampatch.core.domain.fake

import com.teampatch.core.domain.model.question.Question

class FakeQuestions : FakeModel<List<Question>>() {

    override fun build(): List<Question> = listOf(
        Question("q001", 1, "What is your favorite color?"),
        Question("q002", 2, "What is your dream job?"),
        Question("q003", 3, "Where would you like to travel?"),
        Question("q004", 4, "What is your favorite book?"),
        Question("q005", 5, "Who is your role model?"),
        Question("q006", 6, "What is your favorite food?"),
        Question("q007", 7, "What are your hobbies?"),
        Question("q008", 8, "What is your favorite movie?"),
        Question("q009", 9, "What is your biggest fear?"),
        Question("q010", 10, "What is your proudest achievement?")
    )
}