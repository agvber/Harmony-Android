package com.teampatch.core.domain.fake

import com.teampatch.core.domain.model.question.QuestionComment

class FakeQuestionComments : FakeModel<List<QuestionComment>>() {

    override fun build(): List<QuestionComment> = listOf(
        QuestionComment(
            commentId = "cmt001",
            writerUid = "uid001",
            writerName = "Alice Johnson",
            content = "Great question!"
        ),
        QuestionComment(
            commentId = "cmt002",
            writerUid = "uid002",
            writerName = "Bob Smith",
            content = "I’ve been wondering the same."
        ),
        QuestionComment(
            commentId = "cmt003",
            writerUid = "uid003",
            writerName = "Charlie Brown",
            content = "Here’s my take on it."
        ),
        QuestionComment(
            commentId = "cmt004",
            writerUid = "uid004",
            writerName = "Dana White",
            content = "Very insightful."
        ),
        QuestionComment(
            commentId = "cmt005",
            writerUid = "uid005",
            writerName = "Eve Black",
            content = "Thanks for asking!"
        ),
        QuestionComment(
            commentId = "cmt006",
            writerUid = "uid006",
            writerName = "Frank Green",
            content = "Interesting perspective."
        ),
        QuestionComment(
            commentId = "cmt007",
            writerUid = "uid007",
            writerName = "Grace Lee",
            content = "I completely agree."
        ),
        QuestionComment(
            commentId = "cmt008",
            writerUid = "uid008",
            writerName = "Hank Miller",
            content = "I’ve never thought about that."
        ),
        QuestionComment(
            commentId = "cmt009",
            writerUid = "uid009",
            writerName = "Ivy Wilson",
            content = "That’s a good point."
        ),
        QuestionComment(
            commentId = "cmt010",
            writerUid = "uid010",
            writerName = "Jack King",
            content = "Here are my thoughts on this."
        )
    )
}