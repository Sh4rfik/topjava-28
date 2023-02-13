package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import java.util.Collection;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

public abstract class AbstractMealController {

    @Autowired
    private MealService service;

    public Collection<Meal> getAll(int userId) {
        return service.getAll(userId);
    }

    public Meal get(int userId, int mealId) {
        return service.get(userId, mealId);
    }

    public Meal create(int userId, Meal meal) {
        checkNew(meal);
        return service.create(userId, meal);
    }

    public void delete(int userId, int mealId) {
        service.delete(userId, mealId);
    }

    public void update(int userId, Meal meal) {
        assureIdConsistent(meal, meal.getId());
        service.update(userId, meal);
    }
}