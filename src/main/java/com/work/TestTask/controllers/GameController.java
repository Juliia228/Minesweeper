package com.work.TestTask.controllers;

import com.work.TestTask.classes.Game;
import com.work.TestTask.classes.GameRequest;
import com.work.TestTask.exceptions.InvalidRequestException;
import com.work.TestTask.services.GameService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

// контроллер, отвечающий за обработку post запросов (для /new и /turn)
@RestController
@CrossOrigin
@AllArgsConstructor
public class GameController {
    private final GameService service;

    // создает новую игру
    @PostMapping("/new")
    public Game newGame(@RequestBody GameRequest.NewGame request) throws InvalidRequestException {
        if (!(request.getWidth() >= 2 && request.getWidth() <= 30 &&
                request.getHeight() >= 2 && request.getHeight() <= 30)) {
            throw new InvalidRequestException("Ширина поля должна быть не менее 2 и не более 30");
        }
        if (!(request.getMines_count() >= 1 && request.getMines_count() <= request.getWidth() * request.getHeight() - 1)) {
            throw new InvalidRequestException("Количество мин должно быть не менее 1 и не более " +
                    (request.getWidth() * request.getHeight() - 1));
        }
        return service.createGame(request);
    }

    // меняет созданную ранее игру в соотвествии с ходом пользователя
    @PostMapping("/turn")
    public Game gameTurn(@RequestBody GameRequest.Turn request) throws Exception {
        return service.turn(request.getGame_id(), request.getRow(), request.getCol());
    }
}
