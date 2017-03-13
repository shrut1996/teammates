package teammates.test.cases.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.testng.annotations.Test;

import teammates.common.util.AdminLogQuery;
import teammates.test.cases.BaseTestCase;

public class AdminLogQueryTest extends BaseTestCase {
    @Test
    public void testAdminLogQuery() {
        ______TS("Test constructor with parameters");
        List<String> versionList = new ArrayList<String>();
        versionList.add("5-44");
        Calendar cal = new GregorianCalendar();
        cal.set(1994, Calendar.MAY, 7, 15, 30, 12);
        long startTime = cal.getTimeInMillis();
        long endTime = startTime + 22 * 365 * 24 * 60 * 60 * 1000; // about 22 years later
        AdminLogQuery query = new AdminLogQuery(versionList, startTime, endTime);
        assertEquals(startTime, query.getStartTime());
        assertEquals(endTime, query.getEndTime());
        assertNotNull(query.getQuery());

        ______TS("Test setTimePeriod");
        query = new AdminLogQuery(versionList, null, null);
        assertEquals(0, query.getStartTime());
        assertTrue(endTime != query.getStartTime());

        query.setTimePeriod(startTime, endTime);
        assertEquals(startTime, query.getStartTime());
        assertEquals(endTime, query.getEndTime());
        assertNotNull(query.getQuery());
    }

    @Test
    public void testSetQueryWindowBackward() {
        List<String> versionList = new ArrayList<String>();
        versionList.add("5-44");
        Calendar cal = new GregorianCalendar();
        cal.set(2016, 4, 7, 15, 30, 12);
        Long startTime = cal.getTimeInMillis();
        Long endTime = startTime + 3 * 24 * 60 * 60 * 1000; // 3 days later
        AdminLogQuery query = new AdminLogQuery(versionList, startTime, endTime);
        Long fourHours = Long.valueOf(4 * 60 * 60 * 1000);
        query.moveTimePeriodBackward(fourHours); // 4 hours before endTime
        long expectedEndTime = startTime - 1;
        long expectedStartTime = expectedEndTime - fourHours;
        assertEquals(expectedStartTime, query.getStartTime());
        assertEquals(expectedEndTime, query.getEndTime());

        assertEquals(expectedStartTime, query.getQuery().getStartTimeMillis().longValue());
        assertEquals(expectedEndTime, query.getQuery().getEndTimeMillis().longValue());
    }
}
