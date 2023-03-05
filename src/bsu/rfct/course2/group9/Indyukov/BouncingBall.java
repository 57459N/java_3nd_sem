package bsu.rfct.course2.group9.Indyukov;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class BouncingBall implements Runnable {
    // Максимальный радиус, который может иметь мяч
    private static final int MAX_RADIUS = 40;
    // Минимальный радиус, который может иметь мяч
    private static final int MIN_RADIUS = 3;
    // Максимальная скорость, с которой может летать мяч
    private static final int MAX_SPEED = 15;
    private Field field;
    private int radius;
    private Color color;
    // Текущие координаты мяча
    private double x;
    private double y;
    // Вертикальная и горизонтальная компонента скорости
    private int speed;
    private double speedX;
    private double speedY;

    // Конструктор класса BouncingBall
    public BouncingBall(Field field) {
        // Необходимо иметь ссылку на поле, по которому прыгает мяч,
        // чтобы отслеживать выход за его пределы

        // через getWidth(), getHeight()
        this.field = field;
        // Радиус мяча случайного размера
        radius = Double.valueOf(Math.random() * (MAX_RADIUS -
                MIN_RADIUS) + MIN_RADIUS).intValue();
        // Абсолютное значение скорости зависит от диаметра мяча,
        // чем он больше, тем медленнее
        speed = Double.valueOf(Math.round(5 * MAX_SPEED / radius)).intValue();
        if (speed > MAX_SPEED) {
            speed = MAX_SPEED;
        }
        // Начальное направление скорости тоже случайно,
        // угол в пределах от 0 до 2PI
        double angle = Math.random() * 2 * Math.PI;
        // Вычисляются горизонтальная и вертикальная компоненты скорости
        speedX = 3 * Math.cos(angle);
        speedY = 3 * Math.sin(angle);
        // Цвет мяча выбирается случайно
        color = new Color((float) Math.random(), (float) Math.random(),
                (float) Math.random());
        // Начальное положение мяча случайно
        x = Math.random() * (field.getSize().getWidth() - 2 * radius) + radius;
        y = Math.random() * (field.getSize().getHeight() - 2 * radius) + radius;
        // Создаём новый экземпляр потока, передавая аргументом
        // ссылку на класс, реализующий Runnable (т.е. на себя)
        Thread thisThread = new Thread(this);
        // Запускаем поток
        thisThread.start();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getRadius() {
        return radius;
    }

    public double getSpeedX() {
        return speedX;
    }

    public double getSpeedY() {
        return speedY;
    }

    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }

    // Метод run() исполняется внутри потока. Когда он завершает работу,
    // то завершается и поток
    public void run() {
        try {
            // Крутим бесконечный цикл, т.е. пока нас не прервут,
            // мы не намерены завершаться
            while (true) {

                // Синхронизация потоков на самом объекте поля
                // Если движение разрешено - управление будет
                // возвращено в метод
                // В противном случае - активный поток заснет
                field.canMove(this);

                ArrayList<BouncingBall> balls = field.getBallsCoords();


                for (BouncingBall ball : balls) {
                    if (ball.equals(this)) {
                        continue;
                    }
                    BouncingBall ball1 = this;
                    BouncingBall ball2 = ball;

                    // Компоненты вектора C (вектор, соединяющий центры шаров).

                    double cx = ball2.getX() - ball1.getX();
                    double cy = ball2.getY() - ball1.getY();

                    // Вектор C (вектор, соединяющий центры шаров).

                    double cSqr = cx * cx + cy * cy;

                    if (cSqr <= (Math.pow(ball1.getRadius() + ball2.getRadius(), 2))) {
                        // Скалярное произведение векторов.
                        double ball1CScalar = ball1.getSpeedX() * cx + ball1.getSpeedY() * cy;

                        double ball2CScalar = ball2.getSpeedX() * cx + ball2.getSpeedY() * cy;

                        // Разложение скорости шара № 1 на нормальную и тагенсальную.

                        double ball1Nvx = (cx * ball1CScalar) / cSqr;
                        double ball1Nvy = (cy * ball1CScalar) / cSqr;
                        double ball1Tvx = ball1.getSpeedX() - ball1Nvx;
                        double ball1Tvy = ball1.getSpeedY() - ball1Nvy;

                        // Разложение скорости шара № 2 на нормальную и тагенсальную.

                        double ball2Nvx = (cx * ball2CScalar) / cSqr;
                        double ball2Nvy = (cy * ball2CScalar) / cSqr;
                        double ball2Tvx = ball2.getSpeedX() - ball2Nvx;
                        double ball2Tvy = ball2.getSpeedY() - ball2Nvy;

                        // Реализация обмена нормальными скоростями (тагенсальные остаются неизменными).

                        ball1.setSpeedX(ball2Nvx + ball1Tvx);
                        ball1.setSpeedY(ball2Nvy + ball1Tvy);
                        ball2.setSpeedX(ball1Nvx + ball2Tvx);
                        ball2.setSpeedY(ball1Nvy + ball2Tvy);
                    }

                }

                if (x + speedX <= radius) {
                    // Достигли левой стенки, отскакиваем право
                    speedX = -speedX;
                    x = radius;
                } else if (x + speedX >= field.getWidth() - radius) {
                    // Достигли правой стенки, отскок влево
                    speedX = -speedX;
                    x = Double.valueOf(field.getWidth() - radius).intValue();
                } else if (y + speedY <= radius) {
                    // Достигли верхней стенки
                    speedY = -speedY;
                    y = radius;
                } else if (y + speedY >= field.getHeight() - radius) {
                    // Достигли нижней стенки
                    speedY = -speedY;
                    y = Double.valueOf(field.getHeight() - radius).intValue();
                } else {
                    // Просто смещаемся
                    x += speedX;

                    y += speedY;
                }
                // Засыпаем на X миллисекунд, где X определяется
                // исходя из скорости
                // Скорость = 1 (медленно), засыпаем на 15 мс.
                // Скорость = 15 (быстро), засыпаем на 1 мс.
                Thread.sleep(16 - speed);
            }
        } catch (InterruptedException ex) {
            // Если нас прервали, то ничего не делаем
            // и просто выходим (завершаемся)
        }
    }

    // Метод прорисовки самого себя
    public void paint(Graphics2D canvas) {
        canvas.setColor(color);
        canvas.setPaint(color);
        Ellipse2D.Double ball = new Ellipse2D.Double(x - radius, y - radius,
                2 * radius, 2 * radius);
        canvas.draw(ball);
        canvas.fill(ball);
    }
}