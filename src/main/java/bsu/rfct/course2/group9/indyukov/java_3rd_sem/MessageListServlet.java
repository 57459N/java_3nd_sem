package bsu.rfct.course2.group9.indyukov.java_3rd_sem;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "messages", value = "/chat/messages")
public class MessageListServlet extends ChatServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse
            response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("../login.html");
        } else {
            // Установить кодировку HTTP-ответа UTF-8
            response.setCharacterEncoding("utf8");

// Получить доступ к потоку вывода HTTP-ответа
            PrintWriter pw = response.getWriter();
// Записть в поток HTML-разметку страницы
            pw.println("<html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8'/><meta http-equiv='refresh' content='10'></head>");
            pw.println("<body>");

            // Кнопки Отправить и Выйти
            pw.println("<form action=\"send_message\" method=\"post\">\n" +
                    "        Текст сообщения:\n" +
                    "        <input type=\"text\" name=\"message\" style=\"width: 50%\">\n" +
                    "        <input type=\"submit\" value=\"Отправить\">\n" +
                    "        <a href=\"logout\" target=\"_top\">Выйти из чата</a>\n<br>");


            for (String username : activeUsers.keySet()) {
                if (session.getAttribute("username") == username)
                    continue;
                pw.println("<input type=\"radio\" name=\"chosen_user\" value=\"" + username + "\" id=\"" + username + "\">");
                pw.println("<label for=\"" + username + "\">" + username + "</label><br>");
            }


// В обратном порядке записать в поток HTML-разметку для каждого сообщения
            for (int i = messages.size() - 1; i >= 0; i--) {
                ChatMessage aMessage = messages.get(i);
                if (aMessage.getOnlyFor() != null) {
                    String username = (String) session.getAttribute("username");
                    String onlyForName = aMessage.getOnlyFor().getName();
                    String authorName = aMessage.getAuthor().getName();

                    if (onlyForName.equals(username)) {
                        pw.println("<div><strong>" + aMessage.getAuthor().getName() + "</strong> (for you): " + aMessage.getMessage() + "</div>");
                    } else if (authorName.equals(username)) {
                        pw.println(String.format("<div><strong>You</strong> (for %s): " + aMessage.getMessage() + "</div>", onlyForName));
                    }
                } else {
                    pw.println("<div><strong>" + aMessage.getAuthor().getName() + "</strong>: " + aMessage.getMessage() + "</div>");
                }
            }
            pw.println("</body></html>");
        }
    }

    private boolean isAddresseeOrAuthor(ChatMessage message, Object username) {
        return message.getOnlyFor().getName().equals(username) ||
                message.getAuthor().getName().equals(username);
    }
}

