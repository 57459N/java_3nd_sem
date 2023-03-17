package bsu.rfct.course2.group9.indyukov.java_3rd_sem;

import java.io.IOException;
import java.util.Calendar;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class NewMessageServlet extends ChatServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse
            response) throws ServletException, IOException {
// По умолчанию используется кодировка ISO-8859. Так как мы
// передаѐм данные в кодировке UTF-8
// то необходимо установить соответствующую кодировку HTTP-запроса
        request.setCharacterEncoding("UTF-8");
// Извлечь из HTTP-запроса параметр 'message'
        String message = (String) request.getParameter("message");
// Если сообщение не пустое, то
        if (message != null && !"".equals(message)) {
// По имени из сессии получить ссылку на объект ChatUser
            ChatUser author = activeUsers.get((String)
                    request.getSession().getAttribute("name"));
            synchronized (messages) {
// Добавить в список сообщений новое
                messages.add(new ChatMessage(message, author,
                        Calendar.getInstance().getTimeInMillis()));
            }
        }
// Перенаправить пользователя на страницу с формой сообщения
        response.sendRedirect("/chat/compose_message.htm");
    }
}