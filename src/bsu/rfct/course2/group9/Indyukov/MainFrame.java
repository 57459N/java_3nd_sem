package bsu.rfct.course2.group9.Indyukov;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import javax.swing.*;

public class MainFrame extends JFrame {
    private static final String FRAME_TITLE = "Клиент мгновенных сообщений";
    private static final int FRAME_MINIMUM_WIDTH = 500;
    private static final int FRAME_MINIMUM_HEIGHT = 500;
    private static final int FROM_FIELD_DEFAULT_COLUMNS = 10;
    private static final int TO_FIELD_DEFAULT_COLUMNS = 20;
    private static final int INCOMING_AREA_DEFAULT_ROWS = 10;
    private static final int OUTGOING_AREA_DEFAULT_ROWS = 5;
    private static final int SMALL_GAP = 5;
    private static final int MEDIUM_GAP = 10;
    private static final int LARGE_GAP = 15;
    private static final int SERVER_PORT = 4567;
    private static final String UDP_IP = "224.0.0.3";
    private static final Integer UDP_PORT = 8888;
    private final JTextField textFieldFrom;
    private final JTextField textFieldTo;
    private final JTextArea textAreaIncoming;
    private final JTextArea textAreaOutgoing;
    private final JMenu usersMenu = new JMenu("Users");

    final JButton loginButton = new JButton("Login");

    private final String formatUsername = "username";
    private final String formatMessage = "message";

    ArrayList<User> users = new ArrayList<>();
    User me = new User("", "127.0.0.1", get_port());
    User currentChatUser;

    public MainFrame() {
        super(FRAME_TITLE);

        setMinimumSize(new Dimension(FRAME_MINIMUM_WIDTH, FRAME_MINIMUM_HEIGHT));
// Центрирование окна
        final Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - getWidth()) / 2, (kit.getScreenSize().height - getHeight()) / 2);

        // Верхняя панель
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        menuBar.add(usersMenu);

// Текстовая область для отображения полученных сообщений
        textAreaIncoming = new JTextArea(INCOMING_AREA_DEFAULT_ROWS, 0);
        textAreaIncoming.setEditable(false);
// Контейнер, обеспечивающий прокрутку текстовой области
        final JScrollPane scrollPaneIncoming = new JScrollPane(textAreaIncoming);
// Подписи полей
        final JLabel labelFrom = new JLabel("Подпись");
        final JLabel labelTo = new JLabel("Получатель");
// Поля ввода имени пользователя и адреса получателя
        textFieldFrom = new JTextField(FROM_FIELD_DEFAULT_COLUMNS);
        textFieldTo = new JTextField(TO_FIELD_DEFAULT_COLUMNS);
// Текстовая область для ввода сообщения
        textAreaOutgoing = new JTextArea(OUTGOING_AREA_DEFAULT_ROWS, 0);
// Контейнер, обеспечивающий прокрутку текстовой области
        final JScrollPane scrollPaneOutgoing = new JScrollPane(textAreaOutgoing);
// Панель ввода сообщения
        final JPanel messagePanel = new JPanel();
        messagePanel.setBorder(BorderFactory.createTitledBorder("Сообщение"));
// Кнопка отправки сообщения
        final JButton sendButton = new JButton("Отправить");
        sendButton.addActionListener(e -> {
            // Получаем необходимые параметры
            String destinationPort = textFieldTo.getText();
            String message = textAreaOutgoing.getText() + ":" + me.getPort();

            sendMessage(destinationPort, message, formatMessage);
        });

        loginButton.addActionListener(e -> {
            try {
                login();
            } catch (Exception E) {
                E.printStackTrace();
            }
        });

// Компоновка элементов панели "Сообщение"
        final GroupLayout layout2 = new GroupLayout(messagePanel);
        messagePanel.setLayout(layout2);
        layout2.setHorizontalGroup(
                layout2.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout2.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addGroup(layout2.createSequentialGroup()
                                        .addComponent(labelFrom).addGap(SMALL_GAP)
                                        .addComponent(textFieldFrom).addGap(LARGE_GAP)
                                        .addComponent(labelTo)
                                        .addGap(SMALL_GAP)
                                        .addComponent(textFieldTo))
                                .addComponent(scrollPaneOutgoing)
                                .addComponent(sendButton)
                                .addComponent(loginButton))
                        .addContainerGap());

        layout2.setVerticalGroup(
                layout2.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout2
                                .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(labelFrom).addComponent(textFieldFrom)
                                .addComponent(labelTo).addComponent(textFieldTo))
                        .addGap(MEDIUM_GAP)
                        .addComponent(scrollPaneOutgoing)
                        .addGap(MEDIUM_GAP)
                        .addComponent(sendButton)
                        .addComponent(loginButton)
                        .addContainerGap());
// Компоновка элементов фрейма

        final GroupLayout layout1 = new GroupLayout(getContentPane());
        setLayout(layout1);

        layout1.setHorizontalGroup(layout1.createSequentialGroup().addContainerGap().addGroup(layout1.createParallelGroup().addComponent(scrollPaneIncoming).addComponent(messagePanel)).addContainerGap());
        layout1.setVerticalGroup(layout1.createSequentialGroup().addContainerGap().addComponent(scrollPaneIncoming).addGap(MEDIUM_GAP).addComponent(messagePanel).addContainerGap());


// Создание и запуск потока-обработчика запросов
        get_port();
        new Thread(() -> {
            try {
                ServerSocket serverSocket;
                try {
                    serverSocket = new ServerSocket(SERVER_PORT);
                } catch (BindException e) {
                    try {
                        serverSocket = new ServerSocket(SERVER_PORT + 1);
                    } catch (BindException e2) {
                        serverSocket = new ServerSocket(SERVER_PORT + 2);
                    }
                }
                while (!Thread.interrupted()) {
                    final Socket socket = serverSocket.accept();
                    final DataInputStream in = new DataInputStream(socket.getInputStream());
// Читаем формат
                    final String format = in.readUTF();
                    switch (format) {
                        case (formatUsername) -> processUsername(in);
                        case (formatMessage) -> processMessage(in);
                    }
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(MainFrame.this, "Ошибка в работе сервера", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }).start();
    }

    private void processUsername(DataInputStream in) throws IOException {
        String[] msg = in.readUTF().split(":");
        String username = msg[0];
        int port = Integer.parseInt(msg[1]);
        System.out.println("Processing port: " + port);
        users.add(new User(username, "127.0.0.1", port));
        updateUsersList();
    }

    private void processMessage(DataInputStream in) throws IOException {
        String[] data = in.readUTF().split(":");
        String msg = data[0];
        int port = Integer.parseInt(data[1]);

        for (User user : users) {
            if (user.getPort() == port) {
                user.appendDialog(user.getName() + " : " + msg + "\n");
                break;
            }
        }
        if (currentChatUser != null)
            textAreaIncoming.setText(currentChatUser.getDialog());
    }


    private void updateUsersList() {
        usersMenu.removeAll();
        for (User user : users) {
            usersMenu.add(new AbstractAction(user.getName()) {
                @Override
                public void actionPerformed(ActionEvent e) {
                    chooseUser(user);
                }
            });
        }
    }

    private void chooseUser(User user) {
        currentChatUser = user;
        updateIncomingText();
    }

    private void updateIncomingText() {
        textAreaIncoming.setText(currentChatUser.getDialog());
        textFieldTo.setText(currentChatUser.getPort().toString());
    }

    private int get_port() {
        int i = 0;
        while (i < 100) {
            try {
                ServerSocket serverSocket = new ServerSocket(SERVER_PORT + i);
                serverSocket.close();
                System.out.println("MY PORT: " + (SERVER_PORT + i));
                return SERVER_PORT + i;
            } catch (Exception e) {
                i++;
            }
        }
        return 0;
    }

    private void login() throws UnknownHostException {
        new Thread(() -> {
            // Get the address that we are going to connect to.
            InetAddress address;
            try {
                address = InetAddress.getByName(UDP_IP);
            } catch (UnknownHostException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            System.out.println("started udp client");
            // Create a buffer of bytes, which will be used to store
            // the incoming bytes containing the information from the server.
            // Since the message is small here, 256 bytes should be enough.
            byte[] buf = new byte[256];

            // Create a new Multicast socket that will allow other sockets/programs
            // to join it as well.

            try (MulticastSocket clientSocket = new MulticastSocket(UDP_PORT)) {
                //Joint the Multicast group.
                clientSocket.joinGroup(address);
                System.out.println("joined group: " + address);
                while (true) {
                    // Receive the information and print it.
                    DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
                    System.out.println("ready to receive new members");

                    clientSocket.receive(msgPacket);

                    System.out.println("got message udp from " + msgPacket.getSocketAddress() + " : with len: " + msgPacket.getLength());

                    String msg = new String(buf, 0, msgPacket.getLength());
                    String[] msgParts = msg.split(":");

                    int newUsersPort = Integer.parseInt(msgParts[1]);
                    if (newUsersPort != me.getPort()) {
                        User newUser = new User(msgParts[0], me.getIP(), newUsersPort);
                        users.add(newUser);
                        System.out.println(me.getPort() + " received msg: " + msg);
                        updateUsersList();
                        giveResponseToNewMember(newUsersPort);
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(MainFrame.this, "Ошибка в приеме UDP", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }).start();

        InetAddress addr = InetAddress.getByName(UDP_IP);

        // Open a new DatagramSocket, which will be used to send the data.
        try {
            DatagramSocket serverSocket = new DatagramSocket();

            String name = textFieldFrom.getText();
            textFieldFrom.setEnabled(false);
            loginButton.setEnabled(false);

            String msg = name + ":" + me.getPort();

            // Create a packet that will contain the data
            // (in the form of bytes) and send it.
            DatagramPacket msgPacket = new DatagramPacket(msg.getBytes(), msg.getBytes().length, addr, UDP_PORT);
            serverSocket.send(msgPacket);
            serverSocket.close();

            me.setName(name);
            System.out.println("Server sent packet with msg: " + msg);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void giveResponseToNewMember(int newUsersPort) {
        try {
            final Socket socket = new Socket("127.0.0.1", newUsersPort);
            final DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            out.writeUTF(formatUsername);
            out.writeUTF(me.getName() + ":" + me.getPort());
            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainFrame.this, "Не удалось отправить сообщение: узел-адресат не найден", "Ошибка", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainFrame.this, "Не удалось отправить сообщение", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sendMessage(String destinationPort, String message, String format) {
        try {

// Убеждаемся, что поля не пустые
            if (destinationPort.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Введите адрес узла-получателя", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (message.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Введите текст сообщения", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
// Создаем сокет для соединения
            final Socket socket = new Socket("127.0.0.1", Integer.parseInt(destinationPort));
// Открываем поток вывода данных
            final DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            out.writeUTF(format);
// Записываем сообщение в поток
            out.writeUTF(message);
// Закрываем сокет
            socket.close();
// Помещаем сообщения в текстовую область вывода
            currentChatUser.appendDialog("Я : " + message.split(":")[0] + "\n");
            updateIncomingText();
// Очищаем текстовую область ввода сообщения
            textAreaOutgoing.setText("");
        } catch (UnknownHostException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainFrame.this, "Не удалось отправить сообщение: узел-адресат не найден", "Ошибка", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainFrame.this, "Не удалось отправить сообщение", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            final MainFrame frame = new MainFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}

