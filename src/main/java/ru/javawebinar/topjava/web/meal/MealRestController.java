package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.getAuthUserId;

@Controller
public class MealRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal create(Meal meal) {
        log.info("create new meal {} for UserId={}", meal, getAuthUserId());
        checkNew(meal);
        return service.create(getAuthUserId(), meal);
    }

    public void update(Meal meal, int mealId) {
        log.info("update meal with id = {}", mealId);
        assureIdConsistent(meal, mealId);
        service.update(getAuthUserId(), meal);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(getAuthUserId(), id);
    }

    public Meal get(int mealId) {
        log.info("get {}", mealId);
        return service.get(getAuthUserId(), mealId);
    }

    public List<MealTo> getAll() {
        log.info("getAll for UserId={}", getAuthUserId());
        return MealsUtil.getTos(service.getAll(getAuthUserId()), authUserCaloriesPerDay());
    }

    public List<MealTo> getFilteredByDateAndTime(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        log.info("get all filtered for UserId = {}", getAuthUserId());
        LocalDate dateStart = startDate == null ? LocalDate.MIN : startDate;
        LocalDate dateEnd = endDate == null ? LocalDate.MAX : endDate;
        LocalTime timeStart = startTime == null ? LocalTime.MIN : startTime;
        LocalTime timeEnd = endTime == null ? LocalTime.MAX : endTime;
        return MealsUtil.getFilteredTos(service.getFilteredByDate(getAuthUserId(), dateStart, dateEnd), authUserCaloriesPerDay(), timeStart, timeEnd);
    }
}