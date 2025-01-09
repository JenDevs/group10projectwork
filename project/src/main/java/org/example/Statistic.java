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
                        3. Show courses by school
                        4. Show students and exam by course and school
                        5. Show all records
                        
                        """);
                System.out.println("Make a choice");
                int choice = scanner.nextInt();
                scanner.nextLine();

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
                        showCoursesBySchool();
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
                    case 5: showAllRecords();
                        break;
                    default:
                        System.out.println("Invalid choice, please try again");
                        break;
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid input, only integers are valid choices");
                scanner.nextLine();
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

    private void showCoursesBySchool() {
        EntityManager em = JPAUtil.getEntityManager();

        System.out.println("Enter the name of the school");
        String nameOfSchool = scanner.nextLine();

        String sql = """
                SELECT c.courseName
                FROM Course c
                JOIN c.courseSchoolId s
                WHERE s.schoolName = :schoolName
                ORDER BY c.courseName
                """;

        TypedQuery<String> query = em.createQuery(sql, String.class);
        query.setParameter("schoolName", nameOfSchool);

        List<String> courses = query.getResultList();

        System.out.println("Courses for " + nameOfSchool + ":");
        if (courses.isEmpty()) {
            System.out.println("No courses found");
        } else {
            courses.forEach(System.out::println);
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

    public void showAllRecords() {
        EntityManager em = JPAUtil.getEntityManager();

        String sql = """
            SELECT ci, sch, cr, st, e
            FROM City ci
            JOIN ci.schools sch
            JOIN sch.courses cr
            JOIN cr.students st
            JOIN st.exams e
            ORDER BY ci.cityName
            """;

        TypedQuery<Object[]> query = em.createQuery(sql, Object[].class);

        List<Object[]> result = query.getResultList();

        String format = "%-15s %-25s %-20s %-20s %-20s%n";
        System.out.printf(format, "City", "School", "Course", "Student", "Exam Rating");
        System.out.println("===============================================================================================");

        for (Object[] row : result) {
            City ci = (City) row[0];
            School sch = (School) row[1];
            Course cr = (Course) row[2];
            Student st = (Student) row[3];
            Exam exam = (Exam) row[4];

            System.out.printf(format,
                    ci.getCityName(),
                    sch.getSchoolName(),
                    cr.getCourseName(),
                    st.getStudentName(),
                    exam.getExamRating());
        }

        em.close();
    }


}
