package com.work.TestTask.services;

import com.work.TestTask.classes.Game;
import com.work.TestTask.classes.GameRequest;
import com.work.TestTask.classes.Values;
import com.work.TestTask.exceptions.ForbiddenMoveException;
import org.springframework.stereotype.Service;
import java.util.*;

// сервис, создающий игру и изменяющий ее
@Service
public class GameService {
    private final HashMap<UUID, Game> games = new HashMap<>(); // все созданные игры

    // создание новой игры
    public Game createGame(GameRequest.NewGame req) {
        Game game = new Game(req);
        UUID key = game.getGame_id();
        // генерация уникального game_id
        while (games.containsKey(key)) {
            key = UUID.randomUUID();
        }
        game.setGame_id(key);
        games.put(key, game);
        return game;
    }

    // меняет игру в соответствии с ходом пользователя
    public Game turn(UUID game_id, int i, int j) throws Exception {
        if (games.containsKey(game_id)) {
            Game game = games.get(game_id);
            if (game.isCompleted()) {
                throw new ForbiddenMoveException("Игра завершена");
            }
            if (!(i >= 0 && i < game.getHeight() && j >= 0 && j < game.getWidth())) {
                throw new ForbiddenMoveException("Невозможно сделать такой ход");
            }
            // если это первый ход в этой игре, то генерируем поле.
            // генерируем здесь, чтобы в первом же ходу не проиграть
            if (game.getOpened_elements() == 0) {
                game.setFull_field(createField(game, game.getWidth(), game.getHeight(), game.getFull_field(), i, j));
            }
            if (game.isClosedElement(i, j)) {
                String[][] fullField = game.getFull_field();
                String selectedElement = fullField[i][j];
                // попали на мину - поражение
                if (Values.isMine(selectedElement)) {
                    gameOver(game, fullField);
                }
                // попали на безопасную ячейку
                else if (Values.isNumber(selectedElement)) {
                    // попали на 0 - открываем нужные ячейки рядом
                    if (selectedElement.equals(Values.ZERO.getValue())) {
                        openElements(game, i, j);
                    }
                    // попали на ненулевое число - открываем эту ячейку
                    else {
                        game.makeElementOpened(i, j, selectedElement);
                    }
                } else {
                    throw new Exception("Произошла непредвиденная ошибка");
                }
                // если в ходе открытия ячеек открылись все безопасные ячейки - победа
                if (game.isCompleted()) {
                    game.openField();
                }
            } else {
                throw new ForbiddenMoveException("Уже открытая ячейка");
            }
            return game;
        } else {
            throw new ForbiddenMoveException("Игра с идентификатором " + game_id + " не была создана или устарела (неактуальна)");
        }
    }

    // генерируем поле field: генерируем ячейки с минами, считаем числовые ячейки
    public String[][] createField(Game game, int width, int height, String[][] field, int startI, int startJ) {
        for (int i = 0; i < game.getMines_count(); i++) {
            // генерация случайных ячеек с минами
            int randomRow, randomColumn;
            do {
                randomRow = (int) (Math.random() * height);
                randomColumn = (int) (Math.random() * width);
            } while (field[randomRow][randomColumn].equals(Values.MINE.getValue()) || (randomRow == startI && randomColumn == startJ));
            field[randomRow][randomColumn] = Values.MINE.getValue();

            // увеличиваем числовые клетки рядом с миной на 1
            for (int row = randomRow - 1; row <= randomRow + 1; row++) {
                if (row >= 0 && row < height) {
                    for (int column = randomColumn - 1; column <= randomColumn + 1; column++) {
                        if (column >= 0 && column < width && !(field[row][column].equals(Values.MINE.getValue()))) {
                            field[row][column] = Integer.toString(Integer.parseInt(field[row][column]) + 1);
                        }
                    }
                }
            }
        }
        return field;
    }

    // рекурсивно открываем все смежные ячейки, рядом с которыми также нет ни одной мины, а также все смежные с ними
    // "числовые" ячейки, рядом с которыми мины есть, с указанием их количества
    private void openElements(Game game, int i, int j) {
        if (game.isCompleted() || !(i >= 0 && i < game.getHeight() && j >= 0 && j < game.getWidth())) {
            return;
        }
        String element = game.getFull_field()[i][j];
        if (Values.isMine(element) || !game.isClosedElement(i, j)) {
            return;
        }
        if (Values.isNumber(element) && !element.equals(Values.ZERO.getValue())) {
            game.makeElementOpened(i, j, element);
            return;
        }
        game.makeElementOpened(i, j, element);
        if (game.isCompleted()) {
            return;
        }
        openElements(game, i - 1, j - 1);
        openElements(game, i - 1, j);
        openElements(game, i - 1, j + 1);
        openElements(game, i, j - 1);
        openElements(game, i, j + 1);
        openElements(game, i + 1, j - 1);
        openElements(game, i + 1, j);
        openElements(game, i + 1, j + 1);
    }

    // открываем поле field и заменяем "M" на "X" в случае поражения
    private void gameOver(Game game, String[][] fullField) {
        game.setCompleted(true);
        int mines_count = game.getMines_count();
        int mines_counter = 0;
        for (int i = 0; i < game.getHeight(); i++) {
            for (int j = 0; j < game.getWidth(); j++) {
                if (Values.isMine(fullField[i][j])) {
                    fullField[i][j] = Values.GAME_OVER.getValue();
                    mines_counter++;
                    if (mines_counter == mines_count) {
                        i = game.getHeight();
                        break;
                    }
                }
            }
        }
        game.setField(fullField);
    }
}
