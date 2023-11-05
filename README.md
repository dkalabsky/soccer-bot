# telegram-soccer-info


Сборка в докере и деплой в докерхаб.
Для сборки нужно:
0. удалить
   sudo docker image rm sc-soccer-bot
   sudo docker image rm groomok/sc-soccer-bot
1. зайти в директорию, где докер файл
2. выполнить команду:
   sudo docker build -t sc-soccer-bot .
   Деплой в докерхаб
3. тегнуть имедж
   sudo docker tag sc-soccer-bot groomok/sc-soccer-bot
4. запушить в хаб
   sudo docker push groomok/sc-soccer-bot


Посмотреть логи
1. посмотреть все контейнеры
   sudo docker container ls -a
2. посмотреть логи
   sudo docker logs [container_id]