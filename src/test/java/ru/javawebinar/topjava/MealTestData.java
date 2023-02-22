package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    public static final int USER_MEAL_ID = START_SEQ + 7;

    public static final int ADMIN_MEAL_ID = START_SEQ + 3;

    public static final Meal adminMeal1 = new Meal(ADMIN_MEAL_ID + 1, LocalDateTime.of(2023, Month.FEBRUARY, 20, 18, 30), "Dinner", 600);

    public static final Meal adminMeal2 = new Meal
            (ADMIN_MEAL_ID, LocalDateTime.of(2023, Month.FEBRUARY, 20, 12, 45), "Lunch", 500);

    public static final Meal adminMeal3 = new Meal
            (ADMIN_MEAL_ID + 2, LocalDateTime.of(2023, Month.FEBRUARY, 20, 8, 35), "Breakfast", 900);

    public static final Meal adminMeal4 = new Meal
            (ADMIN_MEAL_ID + 3, LocalDateTime.of(2023, Month.FEBRUARY, 22, 8, 0), "Breakfast", 600);

    public static final Meal userMeal1 = new Meal
            (USER_MEAL_ID, LocalDateTime.of(2023, Month.FEBRUARY, 20, 19, 54), "Dinner", 300);

    public static final Meal userMeal2 = new Meal
            (USER_MEAL_ID + 1, LocalDateTime.of(2023, Month.FEBRUARY, 20, 12, 0), "Lunch", 550);

    public static final Meal userMeal3 = new Meal
            (USER_MEAL_ID + 2, LocalDateTime.of(2023, Month.FEBRUARY, 22, 19, 0), "Dinner", 650);

    public static final List<Meal> adminMeals = Arrays.asList(adminMeal4, adminMeal1, adminMeal2, adminMeal3);

    public static final List<Meal> userMeals = Arrays.asList(userMeal3, userMeal1, userMeal2);

    public static final int MEAL_ID_NOT_FOUND = 10;

    public static Meal getNewMeal() {
        return new Meal(null,
                LocalDateTime.of(2023, Month.FEBRUARY, 21, 13, 46),
                "some new meal",
                1000);
    }

    public static Meal getUpdatedMeal() {
        Meal updated = getNewMeal();
        updated.setId(USER_MEAL_ID);
        updated.setDateTime(LocalDateTime.of(2023, Month.FEBRUARY, 20, 23, 0));
        updated.setDescription("update new meal");
        updated.setCalories(500);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().ignoringFields().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparatorIgnoringFields().isEqualTo(expected);
    }
}
