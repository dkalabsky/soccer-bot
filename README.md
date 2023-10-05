# telegram-soccer-info


Сборка в докере и деплой в докерхаб.
Для сборки нужно:
1. зайти в директорию, где докер файл
2. выполнить команду:
   sudo docker build -t sc-football-bot .
   Деплой в докерхаб
3. тегнуть имедж
   sudo docker tag sc-football-bot groomok/sc-football-bot
4. запушить в хаб
   sudo docker push groomok/sc-football-bot


Посмотреть логи
1. посмотреть все контейнеры
   sudo docker container ls -a
2. посмотреть логи
   sudo docker logs [container_id]