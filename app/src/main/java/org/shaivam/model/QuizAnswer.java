package org.shaivam.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuizAnswer implements Serializable
{
    @SerializedName("selected")
    @Expose
    private boolean selected;
    @SerializedName("answer")
    @Expose
    private String answer;
    /**
     * No args constructor for use in serialization
     *
     */
    public QuizAnswer() {
    }

    /**
     * @param selected
     * @param answer
     */
    public QuizAnswer(boolean selected, String answer) {
        super();
        this.selected = selected;
        this.answer = answer;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}