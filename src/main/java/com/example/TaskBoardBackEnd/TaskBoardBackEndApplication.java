package com.example.TaskBoardBackEnd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskBoardBackEndApplication {
    /**
     * revisar el problema de que no se pueden crear mas usarios porque hay un problema de secuencia de codigo (se crea antes
     * primero el token, y despues el usario o algo asi, por eso me dice que user not found al crear un nuevo usuario)
     * crear endpoint editTaks
     */
    public static void main(String[] args) {
        SpringApplication.run(TaskBoardBackEndApplication.class, args);
    }
}