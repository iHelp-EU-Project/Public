package eu.ihelp.store.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class QuestionnaireResponse implements Serializable {
    protected static final long serialVersionUID = 1L;
    
    protected final String questionnaireResponseID;
    protected final String providerID;
    protected final String status;
    protected final Date authoredDate;
    protected final String questionnaireID;
    protected final String questionnaireName;
    protected final String questionnaireTitle;
    protected final String linkedID;
    protected final String questionID;
    protected final String question;
    protected final String questionnaireResponseSystem;
    protected final String questionnaireResponse;
    protected final Double questionnaireScore;
    protected final String patientID;

    protected QuestionnaireResponse(String questionnaireResponseID, String providerID, String status, Date authoredDate, String questionnaireID, String questionnaireName, String questionnaireTitle, String linkedID, String questionID, String question, String questionnaireResponseSystem, String questionnaireResponse, Double questionnaireScore, String patientID) {
        this.questionnaireResponseID = questionnaireResponseID;
        this.providerID = providerID;
        this.status = status;
        this.authoredDate = authoredDate;
        this.questionnaireID = questionnaireID;
        this.questionnaireName = questionnaireName;
        this.questionnaireTitle = questionnaireTitle;
        this.linkedID = linkedID;
        this.questionID = questionID;
        this.question = question;
        this.questionnaireResponseSystem = questionnaireResponseSystem;
        this.questionnaireResponse = questionnaireResponse;
        this.questionnaireScore = questionnaireScore;
        this.patientID = patientID;
    }

    public String getQuestionnaireResponseID() {
        return questionnaireResponseID;
    }

    public String getProviderID() {
        return providerID;
    }

    public String getStatus() {
        return status;
    }

    public Date getAuthoredDate() {
        return authoredDate;
    }

    public String getQuestionnaireID() {
        return questionnaireID;
    }

    public String getQuestionnaireName() {
        return questionnaireName;
    }

    public String getQuestionnaireTitle() {
        return questionnaireTitle;
    }

    public String getLinkedID() {
        return linkedID;
    }

    public String getQuestionID() {
        return questionID;
    }

    public String getQuestion() {
        return question;
    }
    
    public String getQuestionnaireResponseSystem() {
        return questionnaireResponseSystem;
    }

    public String getQuestionnaireResponse() {
        return questionnaireResponse;
    }

    public Double getQuestionnaireScore() {
        return questionnaireScore;
    }

    public String getPatientID() {
        return patientID;
    }

    @Override
    public String toString() {
        return "QuestionnaireResponse{" + "questionnaireResponseID=" + questionnaireResponseID + ", providerID=" + providerID + ", status=" + status + ", authoredDate=" + authoredDate + ", questionnaireID=" + questionnaireID + ", questionnaireName=" + questionnaireName + ", questionnaireTitle=" + questionnaireTitle + ", linkedID=" + linkedID + ", questionID=" + questionID + ", questionnaireResponseSystem=" + questionnaireResponseSystem + ", questionnaireResponse=" + questionnaireResponse + ", questionnaireScore=" + questionnaireScore + ", patientID=" + patientID + '}';
    }
}
