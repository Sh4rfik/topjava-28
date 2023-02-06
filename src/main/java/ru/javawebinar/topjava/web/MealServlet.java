package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.InMemoryMealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletConfig;
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

    private static final Logger log = getLogger(UserServlet.class);

    private static final int CALORIES_PER_DAY = 2000;

    private MealRepository repository;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        repository = new InMemoryMealRepository();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        if (id.isEmpty()) log.debug("Create new meal");
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
        int id;

        switch (action == null ? "all" : action) {
            case "delete":
                id = getId(request);
                log.debug("Delete meal with id {}", id);
                repository.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                Meal meal = action.equals("create") ?
                        new Meal(LocalDateTime.now().withNano(0), "", 100)
                        : repository.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("editMealForm.jsp").forward(request, response);
                log.debug("Update meal with id {}", getId(request));
                break;
            case "all":
            default:
                request.setAttribute("mealList", MealsUtil.filteredByStreams(new ArrayList<>(repository.getAll()),
                        LocalTime.MIN,
                        LocalTime.MAX,
                        CALORIES_PER_DAY));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                log.debug("Redirect to meals");
        }
    }

    private int getId(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter("id"));
    }
}