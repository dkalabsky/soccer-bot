# Определение базового образа с Java
FROM openjdk:19-jdk

# Создание директории приложения в контейнере
WORKDIR /app

# Копирование JAR файла в контейнер
COPY soccer-bot.jar /app/soccer-bot.jar

# Команда запуска приложения
CMD ["java", "-jar", "soccer-bot.jar"]