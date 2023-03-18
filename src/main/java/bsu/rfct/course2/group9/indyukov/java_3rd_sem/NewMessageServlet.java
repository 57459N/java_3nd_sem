package bsu.rfct.course2.group9.indyukov.java_3rd_sem;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Calendar;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "newMessageServlet", value = "/chat/send_message")
public class NewMessageServlet extends ChatServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse
            response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        PrintWriter rw = response.getWriter();
        rw.println("Hello");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse
            response) throws ServletException, IOException {
// По умолчанию используется кодировка ISO-8859. Так как мы
// передаём данные в кодировке UTF-8
// то необходимо установить соответствующую кодировку HTTP-запроса
        request.setCharacterEncoding("UTF-8");

// Извлечь из HTTP-запроса параметр 'message' и 'chosen_user'
        String message = (String) request.getParameter("message");
        String chosenUser = request.getParameter("chosen_user");

// Если сообщение не пустое, то
        if (message != null && !"".equals(message)) {
            // Избранный пользователь. Если выбран, берется из активных пользователей по имени, иначе null
            ChatUser onlyFor = activeUsers.get(chosenUser);
// По имени из сессии получить ссылку на объект ChatUser
            ChatUser author = activeUsers.get((String)
                    request.getSession().getAttribute("name"));
            synchronized (messages) {
// Добавить в список сообщений новое
                System.out.println(request.getParameterNames());
                messages.add(new ChatMessage(message, author,
                        Calendar.getInstance().getTimeInMillis(), onlyFor));
            }
        }
// Перенаправить пользователя на страницу с формой сообщения
        response.sendRedirect("messages");
    }
}