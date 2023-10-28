package com.pshs.attendancesystem.security;

import java.util.Random;

public class PasswordGenerator {
    private static final Random RANDOM = new Random();

    public String generate(int passwordLength) {
        String loweralpha = "abcdefghijklmnopqrstuvwxyz";
        String upperalpha = "ABCDEFGHIJKLMNOPARSTUVWXYZ";
        String numeric = "01234567890";
        StringBuilder builder = new StringBuilder(passwordLength);

        for (int length = 0; length < passwordLength; length++) {
            switch (randomInt(1, 4)) {
                case 1:
                    builder.append(loweralpha.charAt(randomInt(0, loweralpha.length() - 1)));
                    break;
                case 2:
                    builder.append(upperalpha.charAt(randomInt(0, upperalpha.length() - 1)));
                    break;
                case 3:
                    builder.append(numeric.charAt(randomInt(0, numeric.length() - 1)));
                    break;
                default:
                    // To create a file, the symbols are not allowed to be first letter at filename or else the Windows will raise an error.
                    // Instead, we will skip symbols at the first position of the filename and then use them later since after the first position was occupied by alphabets we can
                    // create a file without any problems with symbols in the name of the file.
                    if ((builder.length() <= 1)) {
                        switch (randomInt(1, 3)) {
                            case 1:
                                builder.append(loweralpha.charAt(randomInt(0, loweralpha.length() - 1)));
                                break;
                            case 2:
                                builder.append(upperalpha.charAt(randomInt(0, upperalpha.length() - 1)));
                                break;
                            case 3:
                                builder.append(numeric.charAt(randomInt(0, numeric.length() - 1)));
                                break;
                            default:
                                break;
                        }
                    }
            }
        }

        return builder.toString();
    }

    public Integer randomInt(int min, int max) {
        return RANDOM.nextInt((max - min) + 1) + min;
    }
}