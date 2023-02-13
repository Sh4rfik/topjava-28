package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(1, meal));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        Map<Integer, Meal> mealMap = repository.computeIfAbsent(userId, key -> new ConcurrentHashMap<>());
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            mealMap.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        repository.computeIfPresent(userId, (id, oldMeal) -> mealMap);
        return meal;
    }

    @Override
    public boolean delete(int userId, int mealId) {
        return repository.get(userId).remove(mealId) != null;
    }

    @Override
    public Meal get(int userId, int mealId) {
        return repository.get(userId).get(mealId);
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return filterByPredicate(userId, meal -> true);
    }

    private List<Meal> filterByPredicate(int userId, Predicate<Meal> filter) {
        Map<Integer, Meal> meals = repository.get(userId);
        return CollectionUtils.isEmpty(meals) ? Collections.emptyList() :
                meals.values().stream()
                        .filter(filter)
                        .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                        .collect(Collectors.toList());
    }

    public List<Meal> getFilteredByDate(int userId, LocalDate startDate, LocalDate endDate) {
        return filterByPredicate(userId, meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDate(), startDate, endDate));
    }
}

