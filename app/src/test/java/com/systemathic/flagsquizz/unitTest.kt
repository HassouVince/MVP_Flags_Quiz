package com.systemathic.flagsquizz

import com.nhaarman.mockito_kotlin.doNothing
import com.nhaarman.mockito_kotlin.doReturn
import com.systemathic.flagsquizz.di.applicationModule
import com.systemathic.flagsquizz.model.question.Question
import com.systemathic.flagsquizz.model.question_pack.QuestionsPack
import com.systemathic.flagsquizz.model.User
import com.systemathic.flagsquizz.model.question.QuestionManagerImp
import com.systemathic.flagsquizz.model.question_pack.QuestionPackManagerImp
import com.systemathic.flagsquizz.ui.activity.main.MainActivity
import com.systemathic.flagsquizz.ui.activity.main.MainPresenter
import com.systemathic.flagsquizz.ui.activity.quizz.QuizzActivity
import com.systemathic.flagsquizz.ui.activity.quizz.QuizzPresenter
import com.systemathic.flagsquizz.utils.MAX_QUESTIONS_IN_ONE_PACK
import com.systemathic.flagsquizz.utils.levels
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.inject
import org.koin.test.KoinTest
import org.mockito.Mockito
import java.lang.Exception

@RunWith(JUnit4::class)
class unitTest : KoinTest{

    val packMgr : QuestionPackManagerImp by inject()
    val questionMgr : QuestionManagerImp by inject()

    lateinit var user : User
    lateinit var question : Question
    lateinit var questionsPacks : List<QuestionsPack>

    lateinit var quizzActivity: MainActivity
    lateinit var  quizzPresenter: MainPresenter

    @Before
    fun setUp(){
        try {startKoin(listOf(applicationModule))
        }catch (e : Exception){}

        user = User()
        question = Question()
        questionsPacks = packMgr.getQuestionsPacks()

        quizzActivity = Mockito.mock(MainActivity::class.java)
        quizzPresenter = Mockito.spy(MainPresenter(quizzActivity))
        doNothing().`when`(quizzActivity).initView()
        doNothing().`when`(quizzPresenter).save()
        doReturn(user).`when`(quizzPresenter).getUserFromRepository(null)
        doReturn(user).`when`(quizzPresenter).getUser()
        quizzActivity.setPresenter(quizzPresenter)
        quizzPresenter.onViewCreated()
    }

    @Test
    fun getTabOfQuestions() {
        val list : List<Question> = questionMgr.getTabOfQuestions()
        assertEquals(4,list.get(3).id)
        assertEquals(MAX_QUESTIONS_IN_ONE_PACK*levels,list.size)
    }

    @Test
    fun getPacksQuestionsArray() {
        assertEquals(levels,questionsPacks.size)
        assertEquals(questionsPacks.get(0).questions.get(0),questionMgr.getTabOfQuestions().get(0))
    }

    @Test
    fun isAllPlayed() {
        assertFalse(user.isAllPlayed(packMgr,questionMgr))
        for(i in questionsPacks.indices){
            user.idsQPUsed.add("i")
        }
        assertEquals(user.idsQPUsed.size, questionsPacks.size)
        assertTrue(user.isAllPlayed(packMgr,questionMgr))
    }


    @Test
    fun getPackWithId() {
        var pack = packMgr.getPackWithId(10000)
        assertTrue(pack==null)
        pack = packMgr.getPackWithId(1)
        assertTrue(pack!=null)
        assertEquals(pack?.questions!![0],questionsPacks[0].questions[0])

    }

    @Test
    fun getAvailableQuestionInPack() {
        assertTrue(question.id == 0)
        var question = questionsPacks[0].getAvailableQuestionInPack(user)
        assertTrue(question.id!=0 && question.id!=-1)
        for(question1 in questionsPacks[0].questions){
            user.tempQuestionsChecked.add("0" + question1.id)
        }
        question = questionsPacks[0].getAvailableQuestionInPack(user)
        assertTrue(question.id==-1)
    }

    @Test
    fun getIdPositionInIdsArray() {
        var pos = questionsPacks[0].getIdPositionInIdsArray(user.idsQPUsed)
        assertEquals(-1,pos)
        user.idsQPUsed.add("0|" + questionsPacks[0].id)
        user.idsQPUsed.add("0|" + questionsPacks[1].id)
        pos = questionsPacks[1].getIdPositionInIdsArray(user.idsQPUsed)
        assertEquals(1,pos)
    }

    @Test
    fun isQuestionPackCompleted(){
        var completed = questionsPacks[0].isQuestionPackCompleted(user)
        assertFalse(completed)
        user.idsQPUsed.add("2|" + questionsPacks[0].id)
        completed = questionsPacks[0].isQuestionPackCompleted(user)
        assertTrue(completed)
    }

}
