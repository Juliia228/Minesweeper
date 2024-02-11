package com.work.TestTask.classes;

// enum для хранения возможных значений ячеек
public enum Values {
    CLOSED(" "),
    ZERO("0"),
    NUMBERS("012345678"),
    MINE("M"),
    GAME_OVER("X");
    private final String value;

    Values(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static boolean isNumber(String value) {
        return NUMBERS.getValue().contains(value);
    }

    public static boolean isMine(String value) {
        return value.equals(MINE.getValue());
    }
}
