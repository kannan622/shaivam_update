package org.shaivam.model;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Quiz implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("answer")
    @Expose
    private String answer;

    @SerializedName("QuizAnswer")
    @Expose
    private List<QuizAnswer> QuizAnswer;
    
    @SerializedName("datequiz")
    @Expose
    private String datequiz;
    @SerializedName("correctanswer")
    @Expose
    private String correctanswer;
    @SerializedName("status")
    @Expose
    private Integer status;
    private final static long serialVersionUID = 7116304994688848226L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Quiz() {
    }

    /**
     *
     * @param id
     * @param datequiz
     * @param status
     * @param correctanswer
     * @param answer
     * @param question
     */
    public Quiz(Integer id, String question, String answer, String datequiz, String correctanswer, Integer status) {
        super();
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.datequiz = datequiz;
        this.correctanswer = correctanswer;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getDatequiz() {
        return datequiz;
    }

    public void setDatequiz(String datequiz) {
        this.datequiz = datequiz;
    }

    public String getCorrectanswer() {
        return correctanswer;
    }

    public void setCorrectanswer(String correctanswer) {
        this.correctanswer = correctanswer;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<QuizAnswer> getQuizAnswer() {
        return QuizAnswer;
    }

    public void setQuizAnswer(List<QuizAnswer> quizAnswer) {
        QuizAnswer = quizAnswer;
    }
}