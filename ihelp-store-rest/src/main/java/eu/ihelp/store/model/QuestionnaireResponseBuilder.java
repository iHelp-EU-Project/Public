package eu.ihelp.store.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class QuestionnaireResponseBuilder {
    private final ResultSet resultSet;
    protected String questionnaireResponseID;
    protected String providerID;
    protected String status;
    protected Date authoredDate;
    protected String questionnaireID;
    protected String questionnaireName;
    protected String questionnaireTitle;
    protected String linkedID;
    protected String questionID;
    protected String question;
    protected String questionnaireResponseSystem;
    protected String questionnaireResponse;
    protected Double questionnaireScore;
    protected String patientID;
    
    private QuestionnaireResponseBuilder(ResultSet resultSet) {
        this.resultSet = resultSet;
    }
    
    public static QuestionnaireResponseBuilder createIstance(ResultSet resultSet) {
        return new QuestionnaireResponseBuilder(resultSet);
        
    }
    public List<QuestionnaireResponse> buildAll() throws SQLException {
        List<QuestionnaireResponse> questionnaireResponses = new ArrayList<>();
        QuestionnaireResponse questionnaireResponse = null;
        while((questionnaireResponse = buildNext())!=null) {
            questionnaireResponses.add(questionnaireResponse);
        }
        
        return questionnaireResponses;
    }
    
    public QuestionnaireResponse buildNext() throws SQLException {
        if(resultSet.next()) {
            _restoreValues();
            _setValues();
            return _buildQuestionnaireResponse();
        } else {
            return null;
        }
    }
    
    private void _setValues() throws SQLException {
        this.questionnaireResponseID = resultSet.getString("ANSWERID");
        this.providerID = resultSet.getString("PROVIDERID");
        this.status = resultSet.getString("STATUS");
        this.questionnaireID = resultSet.getString("QUESTIONNAIREID");
        this.questionnaireName = resultSet.getString("QUESTIONNAIRENAME");
        this.questionnaireTitle = resultSet.getString("QUESTIONNAIRETITLE");
        this.linkedID = resultSet.getString("LINKID");
        this.questionID = resultSet.getString("QUESTIONID");
        this.question = resultSet.getString("QUESTION");
        this.questionnaireResponseSystem = resultSet.getString("ANSWERSYSTEM");
        this.questionnaireResponse = resultSet.getString("ANSWER");
        this.patientID = resultSet.getString("PATIENTID");
        
        Timestamp timestamp = resultSet.getTimestamp("AUTHORED");
        if(!resultSet.wasNull()) {
            this.authoredDate = new Date(timestamp.getTime());
        }
        
        Double value = resultSet.getDouble("SCORE");
        if(!resultSet.wasNull()) {
            this.questionnaireScore = value;
        }
    }
    
    private void _restoreValues() {
        this.questionnaireResponseID = null;
        this.providerID = null;
        this.status = null;
        this.authoredDate = null;
        this.questionnaireID = null;
        this.questionnaireName = null;
        this.questionnaireTitle = null;
        this.linkedID = null;
        this.questionID = null;
        this.question = null;
        this.questionnaireResponseSystem = null;
        this.questionnaireResponse = null;
        this.questionnaireScore = null;
        this.patientID = null;
    }
    
    private QuestionnaireResponse _buildQuestionnaireResponse() {
        return new QuestionnaireResponse(questionnaireResponseID, providerID, status, authoredDate, questionnaireID, questionnaireName, questionnaireTitle, linkedID, questionID, question, questionnaireResponseSystem, questionnaireResponse, questionnaireScore, patientID);
    }
}
