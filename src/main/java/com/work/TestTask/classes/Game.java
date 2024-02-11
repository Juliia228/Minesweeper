package com.work.TestTask.classes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import java.util.Arrays;
import java.util.UUID;

// Класс Game содержит информацию об игре и методы работы с классом
@Data
public class Game {
    private UUID game_id;
    private int width;
    private int height;
    private int mines_count;
    @JsonIgnore
    private int opened_elements; // количество открытых ячеек
    private String[][] field; // поле, хранящее открытые ячейки (числа и "X" при выигрыше) и закрытые (" ") ячейки
    @JsonIgnore
    private String[][] full_field; // поле, хранящее все значения (числа и "M")
    private boolean completed;

    public Game(GameRequest.NewGame req) {
        game_id = UUID.randomUUID();
        width = req.getWidth();
        height = req.getHeight();
        mines_count = req.getMines_count();
        completed = false;
        opened_elements = 0;
        field = new String[height][width];
        hideField();
        full_field = new String[height][width];
        setZeroFullField();
    }

    // заполняет field пробелами - " " (стартовое значение для всех ячеек в field)
    private void hideField() {
        for (int i = 0; i < height; i++) {
            Arrays.fill(field[i], Values.CLOSED.getValue());
        }
    }

    // заполняет full_field нулями - "0" (стартовое значение для всех ячеек в full_field)
    private void setZeroFullField() {
        for (int i = 0; i < height; i++) {
            Arrays.fill(full_field[i], Values.ZERO.getValue());
        }
    }

    // проверка, открытая ли ячейка, т.е. равна " " (закрытая) или нет (открытая)
    public boolean isClosedElement(int i, int j) {
        return field[i][j].equals(Values.CLOSED.getValue());
    }

    // делает ячейку "открытой" и проверяет, выполнено ли условие победы
    public void makeElementOpened(int i, int j, String element) {
        field[i][j] = element;
        if (++opened_elements == width * height - mines_count) {
            setCompleted(true);
        }
    }

    // в случае победы делает поле field "открытым", т.е. без " "
    public void openField() {
        field = full_field;
    }
}
