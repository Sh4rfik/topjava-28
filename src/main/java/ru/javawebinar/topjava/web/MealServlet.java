package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealCrud;
import ru.javawebinar.topjava.repository.MealInMemoryRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.slf4j.LoggerFactory.getLogger;


public class MealServlet extends HttpServlet {

    private static final Logger log = getLogger(MealServlet.class);

    private final MealCrud repository = new MealInMemoryRepository();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        final Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        repository.save(meal);
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String action = request.getParameter("action");
        final int caloriesPerDay = 2000;

        if (action == null) {
            log.info("get all Meal");
            request.setAttribute("mealList", MealsUtil.filteredByStreams(new ArrayList<>(repository.getAll()),
                    LocalTime.MIN,
                    LocalTime.MAX,
                    caloriesPerDay));
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        } else if (action.equals("delete")) {
            log.info("delete meal with id = " + getId(request));
            repository.delete(getId(request));
            response.sendRedirect("meals");
        } else if (action.equals("create")) {
            Meal meal = new Meal(LocalDateTime.now().withNano(0), "", 100);
            request.setAttribute("meal", meal);
            request.getRequestDispatcher("editMealForm.jsp").forward(request, response);
            log.info("create new meal");
        } else if (action.equals("update")) {
            Meal meal = repository.get(getId(request));
            request.setAttribute("meal", meal);
            request.getRequestDispatcher("editMealForm.jsp").forward(request, response);
            log.info("update meal with id = " + getId(request));
        } else {
            response.sendRedirect("meals");
        }
    }

    private int getId(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter("id"));
    }
}