package teammates.test.cases.action;

import org.testng.annotations.Test;

import teammates.common.datatransfer.attributes.FeedbackSessionAttributes;
import teammates.common.datatransfer.attributes.InstructorAttributes;
import teammates.common.exception.UnauthorizedAccessException;
import teammates.common.util.Const;
import teammates.ui.controller.AjaxResult;
import teammates.ui.controller.FeedbackSessionStatsPageAction;
import teammates.ui.pagedata.FeedbackSessionStatsPageData;

public class FeedbackSessionStatsPageActionTest extends BaseActionTest {

    @Override
    protected String getActionUri() {
        return Const.ActionURIs.INSTRUCTOR_FEEDBACK_STATS_PAGE;
    }

    @Override
    @Test
    public void testExecuteAndPostProcess() {
        InstructorAttributes instructor1OfCourse1 = dataBundle.instructors.get("instructor1OfCourse1");
        String instructorId = instructor1OfCourse1.googleId;
        String[] submissionParams;

        gaeSimulation.loginAsInstructor(instructorId);

        ______TS("typical: instructor accesses feedback stats of his/her course");

        FeedbackSessionAttributes accessableFeedbackSession = dataBundle.feedbackSessions.get("session1InCourse1");
        submissionParams = new String[]{
                Const.ParamsNames.FEEDBACK_SESSION_NAME, accessableFeedbackSession.getFeedbackSessionName(),
                Const.ParamsNames.COURSE_ID, instructor1OfCourse1.courseId
        };

        FeedbackSessionStatsPageAction a = getAction(addUserIdToParams(instructorId, submissionParams));
        AjaxResult r = getAjaxResult(a);
        FeedbackSessionStatsPageData data = (FeedbackSessionStatsPageData) r.data;

        assertEquals("?error=false&user=idOfInstructor1OfCourse1", r.getDestinationWithParams());
        assertEquals(10, data.sessionDetails.stats.expectedTotal);
        assertEquals(4, data.sessionDetails.stats.submittedTotal);
        assertEquals("", r.getStatusMessage());

        ______TS("fail: instructor accesses stats of non-existent feedback session");

        String nonexistentFeedbackSession = "nonexistentFeedbackSession";
        submissionParams = new String[] {
                Const.ParamsNames.FEEDBACK_SESSION_NAME, nonexistentFeedbackSession,
                Const.ParamsNames.COURSE_ID, instructor1OfCourse1.courseId
        };

        boolean doesThrowUnauthorizedAccessException = false;
        String exceptionMessage = "";

        a = getAction(addUserIdToParams(instructorId, submissionParams));

        try {
            r = getAjaxResult(a);
        } catch (UnauthorizedAccessException e) {
            doesThrowUnauthorizedAccessException = true;
            exceptionMessage = e.getMessage();
        }

        assertTrue(doesThrowUnauthorizedAccessException);
        assertEquals("Trying to access system using a non-existent feedback session entity", exceptionMessage);
        assertEquals("", r.getStatusMessage());
    }

    @Override
    protected FeedbackSessionStatsPageAction getAction(String... params) {
        return (FeedbackSessionStatsPageAction) gaeSimulation.getActionObject(getActionUri(), params);
    }
}
