package com.teampatch.core.network

import com.teampatch.core.network.annotation.AuthorizedRequest
import com.teampatch.core.network.model.question.request.CommentRequestBody
import com.teampatch.core.network.model.question.request.QuestionCardAnswerRequestBody
import com.teampatch.core.network.model.question.request.QuestionCardCommentRequestBody
import com.teampatch.core.network.model.question.response.CommentCreateResponse
import com.teampatch.core.network.model.question.response.CommentEditResponse
import com.teampatch.core.network.model.question.response.QuestionCardAnswerResponse
import com.teampatch.core.network.model.question.response.QuestionCardCommentResponse
import com.teampatch.core.network.model.question.response.QuestionCommonResponse
import com.teampatch.core.network.model.question.response.QuestionEmptyResponse
import com.teampatch.core.network.model.question.response.QuestionProvideResponse
import com.teampatch.core.network.model.question.response.TodayQuestionResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface QuestionRemoteDataSource {

    @AuthorizedRequest
    @GET("/qc/providequestion")
    suspend fun getQuestion(): QuestionCommonResponse<QuestionProvideResponse>

    @AuthorizedRequest
    @GET("/qc/currentquestion/{groupId}")
    suspend fun getTodayQuestion(
        @Path(value = "groupId") groupId: Int,
    ): QuestionCommonResponse<TodayQuestionResponse>

    @AuthorizedRequest
    @GET("/qc/questions/{groupId}")
    suspend fun getRecentThreeQuestions(
        @Path(value = "groupId") groupId: Int,
    ): QuestionCommonResponse<List<TodayQuestionResponse>>

    @AuthorizedRequest
    @GET("/qc/allquestions/{groupId}")
    suspend fun getQuestionAll(
        @Path(value = "groupId") groupId: Int,
    ): QuestionCommonResponse<List<TodayQuestionResponse>>

    @AuthorizedRequest
    @GET("/qc/question/{questionId}")
    suspend fun getQuestionDetail(
        @Path(value = "questionId") questionId: Int,
    ): QuestionCommonResponse<TodayQuestionResponse>

    @AuthorizedRequest
    @GET("/qc/comments/{questionId}")
    suspend fun getQuestionCardComments(
        @Path(value = "questionId") questionId: Int,
    ): QuestionCommonResponse<List<QuestionCardCommentResponse>>

    @AuthorizedRequest
    @POST("/qc/answer/{questionId}")
    suspend fun postQuestionCardAnswer(
        @Path(value = "questionId") questionId: Int,
        @Body questionCardAnswerRequestBody: QuestionCardAnswerRequestBody,
    ): QuestionCommonResponse<QuestionCardAnswerResponse>

    @AuthorizedRequest
    @POST("/qc/updateanswer/{questionId}")
    suspend fun putQuestionCardAnswer(
        @Path(value = "questionId") questionId: Int,
        @Body questionCardAnswerRequestBody: QuestionCardAnswerRequestBody,
    ): QuestionCommonResponse<QuestionCardAnswerResponse>

    @AuthorizedRequest
    @POST("/qc/comment")
    suspend fun postQuestionCardComment(
        @Body questionCardCommentRequestBody: QuestionCardCommentRequestBody,
    ): QuestionCommonResponse<CommentCreateResponse>

    @AuthorizedRequest
    @PUT("/qc/comment/{commentId}")
    suspend fun putComment(
        @Path(value = "commentId") commentId: Int,
        @Body commentRequestBody: CommentRequestBody,
    ): QuestionCommonResponse<CommentEditResponse>

    @AuthorizedRequest
    @DELETE("/qc/comment/{commentId}")
    suspend fun deleteComment(
        @Path(value = "commentId") commentId: Int,
    ): QuestionEmptyResponse
}