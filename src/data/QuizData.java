package data;

import model.Question;

import java.util.ArrayList;
import java.util.List;

public class QuizData {

    private QuizData() {
    }

    public static List<Question> getQuestions() {

        List<Question> questions = new ArrayList<>();

        questions.add(new Question(
                "What does JVM stand for?",
                new String[]{
                        "Java Virtual Machine",
                        "Java Variable Method",
                        "Java Version Manager",
                        "Java Visual Model"
                },
                0
        ));

        questions.add(new Question(
                "Which keyword is used to create an object?",
                new String[]{
                        "class",
                        "new",
                        "this",
                        "static"
                },
                1
        ));

        questions.add(new Question(
                "Which collection allows dynamic size?",
                new String[]{
                        "Array",
                        "ArrayList",
                        "String",
                        "Scanner"
                },
                1
        ));

        questions.add(new Question(
                "Which method starts a Java program?",
                new String[]{
                        "start()",
                        "run()",
                        "main()",
                        "init()"
                },
                2
        ));

        questions.add(new Question(
                "Which keyword is used for inheritance?",
                new String[]{
                        "implements",
                        "extends",
                        "inherits",
                        "super"
                },
                1
        ));

        questions.add(new Question(
                "Which access modifier is most restrictive?",
                new String[]{
                        "public",
                        "protected",
                        "default",
                        "private"
                },
                3
        ));

        questions.add(new Question(
                "Which loop always executes at least once?",
                new String[]{
                        "for",
                        "while",
                        "do-while",
                        "foreach"
                },
                2
        ));

        questions.add(new Question(
                "Which collection stores unique elements?",
                new String[]{
                        "ArrayList",
                        "LinkedList",
                        "HashSet",
                        "Vector"
                },
                2
        ));

        questions.add(new Question(
                "Which keyword handles exceptions?",
                new String[]{
                        "throw",
                        "try",
                        "throws",
                        "catch"
                },
                1
        ));

        questions.add(new Question(
                "Which principle hides data from outside?",
                new String[]{
                        "Inheritance",
                        "Abstraction",
                        "Encapsulation",
                        "Polymorphism"
                },
                2
        ));

        return questions;
    }
}