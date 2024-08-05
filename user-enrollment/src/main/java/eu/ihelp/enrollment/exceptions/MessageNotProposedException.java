package eu.ihelp.enrollment.exceptions;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class MessageNotProposedException extends Exception {
    final String studyID; 
    final String subjectId;
    final String dialogueId; 
    final String triggeredId;

    public MessageNotProposedException(String studyID, String subjectId, String dialogueId, String triggeredId) {
        super("Message with studyID=" + studyID + ", subjectId=" + subjectId + ", dialogueId=" + dialogueId + ", triggeredId=" + triggeredId + " was not in a status proposed");
        this.studyID = studyID;
        this.subjectId = subjectId;
        this.dialogueId = dialogueId;
        this.triggeredId = triggeredId;
    }

    public String getStudyID() {
        return studyID;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public String getDialogueId() {
        return dialogueId;
    }

    public String getTriggeredId() {
        return triggeredId;
    }
    
    @Override
    public String toString() {
        return "MessageNotProposedException{" + "studyID=" + studyID + ", subjectId=" + subjectId + ", dialogueId=" + dialogueId + ", triggeredId=" + triggeredId + '}';
    }
    
    
}
