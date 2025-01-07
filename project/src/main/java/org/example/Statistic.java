package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Statistic {
    public static Scanner scanner = new Scanner(System.in);

    public void showStatisticMenu() {

        boolean inSubMenu = true;
        while (inSubMenu) {
            try {
                System.out.print("""
                        
                        Menu
                        ========
                        0. Go back to main menu
                        1. Show top 3 Students
                        2. Highest and lowest grades
                           for every course
                        3. Number of courses per school
                        4. Show students and exam by course and school
                        
                        """);

                System.out.println("Make a choice");
                String input = scanner.nextLine();
                int choice;

                try {
                    choice = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid choice, only numbers between 0 and 4 are accepted");
                    continue;
                }

                switch (choice) {
                    case 0:
                        inSubMenu = false;
                        break;
                    case 1:
                        topThreeStudents();
                        break;
                    case 2:
                        gradesForCourses();
                        break;
                    case 3:
                        numberOfCoursesPerSchool();
                        break;
                    case 4:
                        Map<String, List<Exam>> gropedExam = showAllExamsGroupedBySchoolAndCourse();
                        gropedExam.forEach((group, exams) -> {
                            System.out.println(group + ":");
                            exams.forEach(exam ->
                                    System.out.println("    - " + exam.getExamStudentId().getStudentName() + " | Rating: " + exam.getExamRating()
                                    ));

                        });
                        break;
                    default:
                        System.out.println("Invalid choice, please try again");
                        break;
                }

            } catch (Exception e) {
                System.out.println("An unexpected error occurred" + e.getMessage());
            }
        }
    }

    private void gradesForCourses() {
        EntityManager em = JPAUtil.getEntityManager();

        String sql = """
                         SELECT c.courseName,
                         MAX(e.examRating) AS highestRating,
                         MIN(e.examRating) AS lowestRating
                         FROM Course c
                         JOIN c.students s
                         JOIN s.exams e
                         GROUP BY c.courseName
                """;


        List<Object[]> results = em.createQuery(sql, Object[].class).getResultList();

        System.out.println("\nHighest and Lowest grades per Course:");
        for (Object[] result : results) {
            String courseName = (String) result[0];
            Integer highestRating = (Integer) result[1];
            Integer lowestRating = (Integer) result[2];

            System.out.println("\nCourse: " + courseName +
                    "\nHighest Grade: " + highestRating +
                    "\nLowest Grade: " + lowestRating);
        }
    }

    private void topThreeStudents() {
        EntityManager em = JPAUtil.getEntityManager();
        String sql = """
                    SELECT s.studentName, AVG(e.examRating) AS avgRating
                        FROM Exam e
                        JOIN e.examStudentId s
                        GROUP BY s.studentName
                        ORDER BY avgRating DESC
                """;

        List<Object[]> results = em.createQuery(sql, Object[].class)
                .setMaxResults(3)
                .getResultList();

        System.out.println("\nTop 3 Students:");
        for (Object[] result : results) {
            String studentName = (String) result[0];
            Double examRating = (Double) result[1];
            System.out.println("\nName: " + studentName + "\nAverage rating: " + examRating);
        }

    }

    private void numberOfCoursesPerSchool() {
        EntityManager em = JPAUtil.getEntityManager();

        String sql = """
                SELECT s.schoolName, COUNT(c)
                FROM Course c
                JOIN c.courseSchoolId s
                GROUP BY s.schoolName
                ORDER BY s.schoolName
                """;

        TypedQuery<Object[]> query = em.createQuery(sql, Object[].class);

        List<Object[]> results = query.getResultList();
        System.out.println("\nNumber of courses per school:");
        if (results.isEmpty()) {
            System.out.println("No school found");
        } else {
            results.forEach(result -> {
                String schoolName = (String) result[0];
                long count = (Long) result[1];
                System.out.println(schoolName + ": " + count + " courses");
                    });
        }

    }

    public static Map<String, List<Exam>> showAllExamsGroupedBySchoolAndCourse() {
        EntityManager em = JPAUtil.getEntityManager();

        String str = """
        SELECT e FROM Exam e
        JOIN FETCH e.examStudentId s
        JOIN FETCH s.studentCourseId c
        JOIN FETCH c.courseSchoolId sch
        ORDER BY sch.schoolName ASC, c.courseName ASC
        """;

        TypedQuery<Exam> examQuery = em.createQuery(str, Exam.class);

        List<Exam> exams = examQuery.getResultList();

        Map<String, List<Exam>> groupedExams = new LinkedHashMap<>();
        exams.forEach(exam -> {
            String key = exam.getExamStudentId().getStudentCourseId().getCourseSchoolId().getSchoolName() + " - " +
                    exam.getExamStudentId().getStudentCourseId().getCourseName();
            groupedExams.computeIfAbsent(key, k -> new ArrayList<>()).add(exam);
        });

        Map<String, List<Exam>> numberedGroupedExams = new LinkedHashMap<>();
        AtomicInteger groupCounter = new AtomicInteger(1);
        groupedExams.forEach((key, value) -> {
            String numberedKey = groupCounter.getAndIncrement() + ". " + key;
            numberedGroupedExams.put(numberedKey, value);
        });

        return numberedGroupedExams;

    }

}
