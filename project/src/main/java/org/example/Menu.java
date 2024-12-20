package org.example;

public class Menu {

    // meny för huvudmenyn
    // Menu, Exit, Cities, Schools, Courses,Students,Exams
    public void showMainMenu() {
        System.out.println(
                """  
                Menu
                ========
                0. Exit
                1. Cities
                2. Schools
                3. Courses
                4. Student
                5. Exam
                6. Statistic
                """);
    }
}

