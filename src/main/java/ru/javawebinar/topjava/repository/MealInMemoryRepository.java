package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealInMemoryRepository implements MealInMemoryCrud {

    private final Map<Integer, Meal> meals = new ConcurrentHashMap<>();

    private final AtomicInteger counter = new AtomicInteger();

    {
        MealsUtil.meals.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.getId() == null) {
            meal.setId(counter.incrementAndGet());
            meals.put(meal.getId(), new Meal(meal.getId(), meal.getDateTime(),
                    meal.getDescription(),
                    meal.getCalories()));
            return meal;
        }
        return meals.computeIfPresent(meal.getId(), (id, presentMeal) -> meal);
    }

    @Override
    public void delete(int id) {
        meals.remove(id);
    }

    @Override
    public Meal get(int id) {
        return meals.get(id);
    }

    @Override
    public Collection<Meal> getAll() {
        return meals.values();
    }
}