#Проект: Vape Shop
![](https://sun9-88.userapi.com/impg/cPALhr_ZU7ose-i_n8l2N1QHUNn8ZJ57Z-6q8A/9BnJDfl_B2w.jpg?size=2487x1272&quality=96&sign=3748bc063e3fe73db453e99fedcb0883&type=album)
![](https://sun9-60.userapi.com/impg/49I_S5R7pTFUhgIZ-YzlOsjpyr4Aw_jZOzrPuQ/vvhsYnuIbeg.jpg?size=2491x1270&quality=96&sign=aa7e0241470714ae3e1234358c99735b&type=album)
Проект создавался с целью облегчить покупку и обмен вейпов между людьми. Существует множество групп в социальных сетях, которые занимаются обеспечением обмена и продажи вейпов среди пользователей, например, я вдохновлялся [группой](https://vk.com/novosib54vp) вконтакте. Однако у всех подобных групп есть свои весомые недостатки:
1. Нет рейтинга пользователей (часто человек продает не то, что выставляет на продажу).
2. Нет сортировки по разным атрибутам товаров, что усложняет поиск нужного товара.
3. Даже когда товар уже продан, нельзя его удалить из поста и, следовательно, люди продолжают писать по поводу объявления еще долгое время после продажи товара.

Проект создавался с целью избавиться от этих недостатков и предоставить пользователям удобный интерфейс, с помощью которого можно быстро и легко выставлять товары на продажу, продавать их, оценивать и получать оценки от пользователей, оставлять комментарии под товарами, оставлять отзывы и получать их от других пользователей, а так же легко и быстро находить нужный вам товар.

---

##Описание проекта
Человек, заходя на сайт, попадает на главную страницу, изображения которой приводились выше. Незарегистрированный пользователь может смотреть товары, производить поиск и сортировку по ним. Так же в верхнем правом углу сайта будет предложено зайти в аккаунт или зарегистрироваться.

___Страница логина:___
![](https://sun9-75.userapi.com/impg/DuxOEMXcvwakZuBrj0lBxk6bh53_R-oX-DKE3g/tgk_cFOVNbI.jpg?size=2493x1270&quality=96&sign=286fc5f4750434687d2e9baae8ef85b6&type=album)

___Страница регистрации:___
![](https://sun9-73.userapi.com/impg/sEo9A5gfts-pzr7tJp9L7Zl_ouvC_CUPLMtcnQ/81hSpjX4ngg.jpg?size=2489x1275&quality=96&sign=80036f76ab9392e4163c08db6a086b09&type=album)

Следует отметить, что возраст должен быть более 18ти лет, иначе система не зарегистрирует вас на сайте. И так же нужно обязательно указать существующую почту, так как впоследствии вам будет высланно сообщение с подтверждением регистрации аккаунта.
После успешной регистрации и авторизации вам будет открыта страница пользователя, откуда можно будет изменять данные учетной записи, поставить фотографию и так же создавать свои товары. На страницах чужих товаров, вам будет разрешено оставлять комментарии.

___Страница профиля:___
![](https://sun9-86.userapi.com/impg/JqcftM1g68kIsuRiOYMNv1Yo1E4tRAs8W5FKeA/kEAgXrLlzoU.jpg?size=2489x1276&quality=96&sign=ca80f0f0899913abe5da5ad331a25528&type=album)

Здесь можно будет добавлять новые товары(после создания товар будет отправлен на рассмотрение админам, которые впоследствии решат, соответствует ли товар нормам или нет), смотреть товары, которые вы приобрели у других людей, а так же смотреть товары, которые у вас приобрели другие люди. В самом нижу страницы, будут отображены отзывы от ваших покупателей с их оценкой вас как продавца.

___Страница чужого товара:___
![](https://sun9-87.userapi.com/impg/_QLEaiTN9OkjF6vHjteDIUCUZszBi3Q3qtMqXg/qlVEK_5wrno.jpg?size=1321x1158&quality=96&sign=3471b6b2a95acacf0d01457bd141a747&type=album)

На этой странице будет возможность оставлять комментарии к товару и получать на них ответы от продавца. Можно забронировать товар и отписаться продавцу о том, что вы хотите приобрести этот товар. Потом вы договариваетесь о встрече и затем у продавца будет возможность подтвердить продажу товара какому-то пользователю и впоследствии покупатель сможет оставить отзыв продавцу (Возможность оценивания будет открыта на странице профиля пользователя, после покупки у него товара).

___Страница своего товара:___
![](https://sun9-34.userapi.com/impg/olU_t5AlkMIKO1GXwZx9gi33L229keir7NdSGQ/YUJ8bFEfz3k.jpg?size=1325x1133&quality=96&sign=5d94eb35a06e4dfeab3de0db4f245777&type=album)

На данной странице есть возможности редактировать и удалить товар, а также подтвердить его покупку.

---

##Технологии в проекте
Было реализованно MVC приложение с помощью Spring Framework.
К проекту подключенно множество библиотек и подфреймворков Spring. Из которых можно выделить следущие:
* Spring Boot
* Spring Security
* Шаблонизатор Thymeleaf
* Spring Data JPA
* Spring Web
* Hibernate Validation

Так же было реализовано модульное Mock тестирование и настройка логгирования по уровням с записью логов в файлы.

---

##Установка проекта
1. После клонирования репозитория необходимо создать базу данных и выполнить скрипт, который находится в корне проекта.
2. Необходимо присвоить значения ключам в файле application.properties, который находится по пути: _src/main/resources/application.properties_. 
___Пример заполнения файла:___
![](https://sun9-80.userapi.com/impg/ZQckjEjrbhEjr9dI2u85DfIOvZmp9ljdgL1Nig/Ffd4Lu7piHw.jpg?size=973x597&quality=96&sign=61b580afc33031e6fd94e9b4eb835064&type=album)
Раздел mail config заполняется индивидуально, а свои данные можно посмотреть, руководствуясь, в моем случае [документацией](https://help.mail.ru/mail/security/protection/external).
3. Для того, чтобы сделать пользователя админом нужно:
    3.1 Зарегистрироваться на сайте
    3.2 Подтвердить свою почту, путем перехода по ссылке.
    3.3 Вручную изменить в базе роль пользователя с ROLE_USER на        
    ROLE_ADMIN, при этом на странице профиля появится кнопка, для   
    перехода на страницу админа. 