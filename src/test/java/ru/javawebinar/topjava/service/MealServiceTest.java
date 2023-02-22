package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.assertMatch;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService mealService;

    @Test
    public void get() {
        Meal meal = mealService.get(adminMeal1.getId(), ADMIN_ID);
        assertMatch(meal, adminMeal1);
    }

    @Test
    public void delete() {
        mealService.delete(adminMeal2.getId(), ADMIN_ID);
        assertThrows(NotFoundException.class, () -> mealService.get(adminMeal2.getId(), ADMIN_ID));
    }

    @Test
    public void getAll() {
        List<Meal> allAdminMeals = mealService.getAll(ADMIN_ID);
        assertMatch(allAdminMeals, adminMeals);
        List<Meal> allUserMeals = mealService.getAll(USER_ID);
        assertMatch(allUserMeals, userMeals);
    }

    @Test
    public void update() {
        Meal updated = getUpdatedMeal();
        mealService.update(updated, USER_ID);
        assertMatch(mealService.get(updated.getId(), USER_ID), getUpdatedMeal());
    }

    @Test
    public void create() {
        Meal created = mealService.create(getNewMeal(), USER_ID);
        Integer newId = created.getId();
        Meal newMeal = getNewMeal();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(mealService.get(newId, USER_ID), newMeal);
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () ->
                mealService.create(new Meal(userMeal2.getDateTime(), "Lunch", 500), USER_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> mealService.delete(MEAL_ID_NOT_FOUND, USER_ID));
    }

    @Test
    public void deletedNotOwn() {
        assertThrows(NotFoundException.class, () -> mealService.delete(adminMeal1.getId(), USER_ID));
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> mealService.get(NOT_FOUND, USER_ID));
    }

    @Test
    public void getNotOwn() {
        assertThrows(NotFoundException.class, () -> mealService.get(adminMeal1.getId(), USER_ID));
    }

    @Test
    public void updateNotOwn() {
        assertThrows(NotFoundException.class, () -> mealService.update(getUpdatedMeal(), ADMIN_ID));
    }

    @Test
    public void getBetweenInclusive() {
        assertMatch(mealService.getBetweenInclusive(
                        LocalDate.of(2023, Month.FEBRUARY, 20),
                        LocalDate.of(2023, Month.FEBRUARY, 20), USER_ID),
                userMeal1, userMeal2);
    }

    @Test
    public void getBetweenWithNullDates() {
        assertMatch(mealService.getBetweenInclusive(null, null, USER_ID), userMeals);
    }
}