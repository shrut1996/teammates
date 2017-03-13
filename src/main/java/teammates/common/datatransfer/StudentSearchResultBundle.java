package teammates.common.datatransfer;

import teammates.common.datatransfer.attributes.InstructorAttributes;
import teammates.common.datatransfer.attributes.StudentAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The search result bundle for {@link StudentAttributes}.
 */
public class StudentSearchResultBundle extends SearchResultBundle {

    public List<StudentAttributes> studentList = new ArrayList<StudentAttributes>();
    public Map<String, InstructorAttributes> courseIdInstructorMap = new HashMap<String, InstructorAttributes>();

}
