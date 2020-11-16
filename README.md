# Описание задачи
Реализовать систему ввода и отображения информации о рецептах поликлиники, включающую следующие сущности и их атрибуты:
* Пациент 
	* Имя
	* Фамилия 
	* Отчество 
	* Телефон
* Врач
	* Имя
	* Фамилия 
	* Отчество 
	* Специализация
* Рецепт
	* Описание
	* Пациент
	* Врач
	* Дата создания 
	* Срок действия 
	* Приоритет

Рецепт может иметь один из приоритетов: Нормальный, Cito (Срочный), Statim (Немедленный).

# Используемые технологии:
- [x] Java SE 8
- [x] Пользовательский интерфейс на [Vaadin 14](https://vaadin.com)
- [x] Доступ к данным через JDBC
- [x] Сервер баз данных: HSQLDB в [in-process режиме](http://hsqldb.org/doc/2.0/guide/running-chapt.html#rgc_inprocess)

Требования
-------------

* [Java Development Kit (JDK) 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Maven 3](https://maven.apache.org/download.cgi)

Сборка и запуск
-------------

1. Запустите следующие команды:
	```
	mvn package
	mvn jetty:run
	```

2. Перейдите по ссылке: `http://localhost:8080`в браузере.

