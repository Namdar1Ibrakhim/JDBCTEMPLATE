package com.example.jdbctemplate;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PurchaseRepository {
    //Репозиторий класс который взаимодействует с базами данных

    private final JdbcTemplate jdbs;


    public PurchaseRepository(JdbcTemplate jdbs) {
        this.jdbs = jdbs;
    }


    public void storePurchase(Purchase purchase) {
        //В качестве параметра
        //метод принимает данные,
        //которые нужно сохранить
        String sql = "INSERT INTO purchase VALUES (?, ?, ?)";
        //Запрос представляет собой
        //строку, в которой вместо
        //значений параметров стоят
        //вопросительные знаки (?).
        //Вместо ID ставим NULL, так как
        //СУБД сама генерирует значение
        //для этого столбца

        jdbs.update(sql, purchase.getId(), purchase.getProduct(), purchase.getPrice());
        //Метод update() экземпляра JdbcTemplate посылает запрос
        //на сервер баз данных. Первый параметр метода — сам
        //запрос, а остальные — значения параметров запроса. Эти
        //значения в указанной последовательности подставляются
        //в запрос вместо вопросительных знаков
    }

//Как и при создании записи, для этого нужно написать и отправить на сервер запрос — в нашем
//случае это соответственно SELECT. Но потом, чтобы сообщить JdbcTemplate,
//как преобразовать данные в объекты Purchase (наш класс модели), необходим
//RowMapper — объект, выполняющий преобразование строки из ResultSet в заданный объект. Например, чтобы получить данные из базы и представить их
//в виде модели Purchase, необходимо создать RowMapper и описать в нем способ
//преобразования строки таблицы в экземпляр Purchase


    public List<Purchase> findAllPurchases() { //Определяем запрос SELECT для получения всех записей из таблицы покупок

        String sql = "SELECT * FROM purchase";

        //Создаем объект RowMapper,
        //который сообщает JdbcTemplate,
        //как преобразовать строку,
        //полученную из базы данных,
        //в объект Purchase. Параметр r
        //лямбда-выражения соответствует
        //ResultSet (данным, полученным из
        //базы), а параметр i — целое число,
        //показывающее номер строки

        RowMapper<Purchase> purchaseRowMapper = (r, i) -> {
            Purchase rowObject = new Purchase();
            rowObject.setId(r.getInt("id"));
            rowObject.setProduct(r.getString("product"));
            rowObject.setPrice(r.getBigDecimal("price"));
            return rowObject;
        };
        //Заносим данные в экземпляр
        //Purchase. JdbcTemplate
        //будет выполнять эту логику
        //для каждой строки из набора
        //результатов


        return jdbs.query(sql, purchaseRowMapper);
        //Отправляем запрос SELECT,
        //используя метод query(),
        //и передаем объект
        //преобразователя строк,
        //чтобы JdbcTemplate знал,
        //как преобразовать полученные
        //данные в объекты Purchase
    }
}