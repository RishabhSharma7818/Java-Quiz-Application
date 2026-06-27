package data;

import model.Question;

import java.util.ArrayList;
import java.util.List;

public final class QuizData {

    private QuizData() {}

    public static List<Question> getQuestions() {

        List<Question> questions = new ArrayList<>();

        questions.add(new Question(
                "What does JVM stand for?",
                new String[]{"Java Virtual Machine", "Java Variable Method", "Java Version Manager", "Java Visual Model"},
                0
        ));

        questions.add(new Question(
                "Which keyword is used to create an object in Java?",
                new String[]{"class", "new", "this", "static"},
                1
        ));

        questions.add(new Question(
                "Which collection allows dynamic resizing?",
                new String[]{"Array", "ArrayList", "String", "Scanner"},
                1
        ));

        questions.add(new Question(
                "Which method is the entry point of a Java program?",
                new String[]{"start()", "run()", "main()", "init()"},
                2
        ));

        questions.add(new Question(
                "Which keyword is used for inheritance in Java?",
                new String[]{"implements", "extends", "inherits", "super"},
                1
        ));

        questions.add(new Question(
                "Which access modifier is the most restrictive?",
                new String[]{"public", "protected", "default", "private"},
                3
        ));

        questions.add(new Question(
                "Which loop always executes its body at least once?",
                new String[]{"for", "while", "do-while", "foreach"},
                2
        ));

        questions.add(new Question(
                "Which collection stores only unique elements?",
                new String[]{"ArrayList", "LinkedList", "HashSet", "Vector"},
                2
        ));

        questions.add(new Question(
                "Which keyword begins exception handling in Java?",
                new String[]{"throw", "try", "throws", "catch"},
                1
        ));

        questions.add(new Question(
                "Which OOP principle hides internal data from outside?",
                new String[]{"Inheritance", "Abstraction", "Encapsulation", "Polymorphism"},
                2
        ));

        questions.add(new Question(
                "What is the size of an int in Java?",
                new String[]{"8 bits", "16 bits", "32 bits", "64 bits"},
                2
        ));

        questions.add(new Question(
                "Which of these is NOT a primitive type in Java?",
                new String[]{"int", "boolean", "String", "char"},
                2
        ));

        questions.add(new Question(
                "What does the 'final' keyword do when applied to a variable?",
                new String[]{"Makes it static", "Makes it constant", "Makes it private", "Deletes it after use"},
                1
        ));

        questions.add(new Question(
                "Which interface must be implemented to create a thread?",
                new String[]{"Serializable", "Runnable", "Comparable", "Cloneable"},
                1
        ));

        questions.add(new Question(
                "What is the default value of a boolean in Java?",
                new String[]{"true", "false", "0", "null"},
                1
        ));

        return questions;
    }
}