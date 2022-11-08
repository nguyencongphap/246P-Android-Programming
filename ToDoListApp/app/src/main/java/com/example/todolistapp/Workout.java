package com.example.todolistapp;

public class Workout {
    private String name;
    private String description;

    public static final Workout[] workouts = {
            new Workout("The Limb Loosener",
                    "1 Handstand push-ups\n10 1-legged squats\n15 Pull-ups"),
            new Workout("Core Agony",
                    "2 Handstand push-ups\n10 1-legged squats\n15 Pull-ups"),
            new Workout("The Wimp Special",
                    "3 Handstand push-ups\n10 1-legged squats\n15 Pull-ups"),
            new Workout("Strength and Length",
                    "4 Handstand push-ups\n10 1-legged squats\n15 Pull-ups"),
    };

    // Each Workout has a name and description
    private Workout(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}



