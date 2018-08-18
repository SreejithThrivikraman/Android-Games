package com.madt.sree.rockpaperscissors;

import java.util.Random;

public class NameGenerator
{

    private static final String[] first_name =
            new String[] {
                    "Red",
                    "Orange",
                    "Yellow",
                    "Green",
                    "Blue",
                    "Indigo",
                    "Violet",
                    "Purple",
                    "Lavender",
                    "Fuchsia",
                    "Plum",
                    "Orchid",
                    "Magenta",
                         };




    private static final String[] second_name =
            new String[] {
                    "Apple",
                    "Banana",
                    "Chocolate",
                    "Door",
                    "Eduardo",
                    "Flower",
                    "Games",
                    "Hopper",
                    "Iris",
                    "Jacob",
                    "Kelly",
                    "Lucy",
                    "Michelle",
                    "Nunavut",
            };



    private NameGenerator() {}    // constructor

    // class to generate the random number. Class : Random
    // nextInt(int n )  : generates a number between 0 and given number 'n'.
    private static final Random random_name = new Random();


    /** Generate a random host name */
    public static String generate()
    {
        String color = first_name[random_name.nextInt(first_name.length)];

        System.out.println("Length 1 = " + random_name.nextInt(first_name.length));

        String treat = second_name[random_name.nextInt(second_name.length)];

        System.out.println("Length 2 = " + random_name.nextInt(second_name.length) + treat);

        return color + " " + treat ;
    }

}