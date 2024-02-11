package com.work.TestTask.classes;

import lombok.Data;

import java.util.UUID;

// данные http запросов
@Data
public class GameRequest {
    // для запроса /new
    @Data
    public static class NewGame {
        private int width;
        private int height;
        private int mines_count;

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public int getMines_count() {
            return mines_count;
        }
    }

    // для запроса /turn
    @Data
    public static class Turn {
        private UUID game_id;
        private int col;
        private int row;

        public UUID getGame_id() {
            return game_id;
        }

        public int getCol() {
            return col;
        }

        public int getRow() {
            return row;
        }
    }
}
