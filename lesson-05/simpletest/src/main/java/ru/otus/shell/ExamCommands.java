package ru.otus.shell;

import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.service.ConsoleExaminationService;

@ShellComponent
@ShellCommandGroup("Exam Commands")
public class ExamCommands {

    private final ConsoleExaminationService consoleExaminationService;

    public ExamCommands(ConsoleExaminationService consoleExaminationService) {
        this.consoleExaminationService = consoleExaminationService;
    }

    @ShellMethod(value = "run exam command", key = {"run", "runExam"})
    public void runExam() {
        consoleExaminationService.conductTest();
    }
}
