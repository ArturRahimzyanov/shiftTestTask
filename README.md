# shiftTestTask

Реализовать Android-приложение со следующими функциями:
1. Пользователь вводит BIN банковской карты и видит всю доступную информацию о нём,
загруженную с https://binlist.net/
2. История предыдущих запросов выводится списком
3. История предыдущих запросов не теряется при перезапуске приложения
4. Нажатие на URL банка, телефон банка, координаты страны отправляет пользователя в
приложение, которое может обработать эти данные (браузер, звонилка, карты)

Для работы с сетью использовал retrofit, для кешеривания запросов sharedPreferences,
Сохранение состояния при изменении конфигурации устройства реализовал через ViewModel


![ScreenShots](https://github.com/ArturRahimzyanov/shiftTestTask/blob/main/readmione.jpg)
![ScreenShots](https://github.com/ArturRahimzyanov/shiftTestTask/blob/main/readmisecond.jpg)
