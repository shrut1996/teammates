package teammates.ui.template;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import teammates.common.datatransfer.attributes.StudentAttributes;
import teammates.common.datatransfer.TeamDetailsBundle;

public class StudentListTeamData {

    private String teamName;
    private List<StudentListStudentData> students;

    public StudentListTeamData(TeamDetailsBundle team, Map<String, String> emailPhotoUrlMapping, String googleId) {
        this.teamName = team.name;
        List<StudentListStudentData> studentsDetails =
                                        new ArrayList<StudentListStudentData>();
        for (StudentAttributes student : team.students) {
            studentsDetails.add(new StudentListStudentData(googleId, student.name, student.email, student.course,
                                                           student.getStudentStatus(),
                                                           emailPhotoUrlMapping.get(student.email)));
        }
        this.students = studentsDetails;
    }

    public String getTeamName() {
        return teamName;
    }

    public List<StudentListStudentData> getStudents() {
        return students;
    }

}
