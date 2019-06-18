package com.esliceu.keep_it_safe;

import com.esliceu.keep_it_safe.entities.*;
import com.esliceu.keep_it_safe.exception.NoStockException;
import com.esliceu.keep_it_safe.managers.entities.StockManager;
import com.esliceu.keep_it_safe.repository.CommentRepository;
import com.esliceu.keep_it_safe.repository.InvoiceRepository;
import com.esliceu.keep_it_safe.repository.LuggageRepository;
import com.esliceu.keep_it_safe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Component
public class MockDatabase {

    private UserRepository userRepository;
    private LuggageRepository luggageRepository;
    private InvoiceRepository invoiceRepository;
    private CommentRepository commentRepository;
    private StockManager stockManager;
    private ApplicationContext context;

    @Autowired
    public MockDatabase(UserRepository userRepository, LuggageRepository luggageRepository,
                        InvoiceRepository invoiceRepository, StockManager stockManager,
                        CommentRepository commentRepository,
                        ApplicationContext context) {
        this.userRepository = userRepository;
        this.luggageRepository = luggageRepository;
        this.invoiceRepository = invoiceRepository;
        this.commentRepository = commentRepository;
        this.stockManager = stockManager;
        this.context = context;
    }

    public void createDatabaseMock() {

        User client_1 = context.getBean(User.class);
        client_1.setName("Client");
        client_1.setSurnames("Client 1");
        client_1.setPassword("client1234");
        client_1.setEmail("client_1@gmail.com");
        client_1.setRol_user(RolUser.CLIENT);
        userRepository.save(client_1);

        User client_2 = context.getBean(User.class);
        client_2.setName("Client");
        client_2.setSurnames("Client_2");
        client_2.setPassword("client1234");
        client_2.setEmail("client_2@gmail.com");
        client_2.setRol_user(RolUser.CLIENT);
        userRepository.save(client_2);

        User employee = context.getBean(User.class);
        employee.setName("Employee");
        employee.setSurnames("Employee");
        employee.setRol_user(RolUser.EMPLOYEE);
        employee.setPassword("employee1234");
        employee.setEmail("employee@gmail.com");
        userRepository.save(employee);

        User admin = context.getBean(User.class);
        admin.setName("admin");
        admin.setSurnames("sudo su");
        admin.setRol_user(RolUser.ADMIN);
        admin.setPassword("admin1234");
        admin.setEmail("admin@gmail.com");
        userRepository.save(admin);

        Luggage smallLuggage = context.getBean(Luggage.class);
        smallLuggage.setLuggage_type(LuggageType.SMALL);
        smallLuggage.setPrice(4.20);
        smallLuggage.setHigh_dimension(35.7);
        smallLuggage.setWidth_dimension(40);
        smallLuggage.setDeep_dimension(20.5);
        luggageRepository.save(smallLuggage);

        Luggage mediumLuggage = context.getBean(Luggage.class);
        mediumLuggage.setLuggage_type(LuggageType.MEDIUM);
        mediumLuggage.setPrice(6.20);
        mediumLuggage.setHigh_dimension(50.2);
        mediumLuggage.setWidth_dimension(60);
        mediumLuggage.setDeep_dimension(30.5);
        luggageRepository.save(mediumLuggage);

        Luggage bigLuggage = context.getBean(Luggage.class);
        bigLuggage.setLuggage_type(LuggageType.BIG);
        bigLuggage.setPrice(8.24);
        bigLuggage.setHigh_dimension(70.32);
        bigLuggage.setWidth_dimension(87);
        bigLuggage.setDeep_dimension(50.9);
        luggageRepository.save(bigLuggage);

        List<Luggage> luggages = new ArrayList<>();


        Invoice invoice_1 = context.getBean(Invoice.class);
        invoice_1.setTotal_price(12.6);
        invoice_1.setStart_date(this.addDay2(Calendar.getInstance(), 0));
        invoice_1.setEnd_date(this.addDay2(Calendar.getInstance(), 10));
        invoice_1.setVerified(true);
        invoiceRepository.save(invoice_1);
        invoice_1.setUser(client_1);
        luggages.add(smallLuggage);
        luggages.add(smallLuggage);
        luggages.add(smallLuggage);
        luggages.add(smallLuggage);
        invoice_1.setLuggages(luggages);
        invoiceRepository.save(invoice_1);

        try {
            this.stockManager.checkStock(invoice_1);
        } catch (NoStockException e) {
            System.out.println(e);
        }


        luggages.clear();

        Invoice invoice_2 = context.getBean(Invoice.class);
        invoice_2.setTotal_price(20.64);
        invoice_2.setStart_date(this.addDay2(Calendar.getInstance(), -15));
        invoice_2.setEnd_date(this.addDay2(Calendar.getInstance(), 0));
        invoice_2.setVerified(false);
        invoiceRepository.save(invoice_2);
        invoice_2.setUser(client_2);
        luggages.add(mediumLuggage);
        luggages.add(mediumLuggage);
        luggages.add(bigLuggage);
        invoice_2.setLuggages(luggages);
        invoiceRepository.save(invoice_2);


        try {
            this.stockManager.checkStock(invoice_2);
        } catch (NoStockException e) {
            System.out.println(e);
        }

        luggages.clear();

        Invoice invoice_3 = context.getBean(Invoice.class);
        invoice_3.setTotal_price(32.96);
        invoice_3.setStart_date(this.addDay2(Calendar.getInstance(), 0));
        invoice_3.setEnd_date(this.addDay2(Calendar.getInstance(), 2));
        invoice_3.setVerified(true);
        invoiceRepository.save(invoice_3);
        invoice_3.setUser(client_2);
        luggages.add(bigLuggage);
        luggages.add(bigLuggage);
        luggages.add(bigLuggage);
        luggages.add(bigLuggage);
        invoice_3.setLuggages(luggages);
        invoiceRepository.save(invoice_3);

        try {
            this.stockManager.checkStock(invoice_3);
        } catch (NoStockException e) {
            System.out.println(e);
        }

        Comment comment1 = context.getBean(Comment.class);
        comment1.setComment_text("HOLA SOY UN PUTO COMENTARIO");
        commentRepository.save(comment1);
        comment1.setUser(client_1);
        commentRepository.save(comment1);


        Comment comment2 = context.getBean(Comment.class);
        comment2.setComment_text("Comentario genérico para testear la aplicación de Keep It Safe");
        commentRepository.save(comment2);
        comment2.setUser(client_2);
        commentRepository.save(comment2);

        Comment comment3 = context.getBean(Comment.class);
        comment3.setComment_text("Hola k ase premoh");
        commentRepository.save(comment3);
        comment3.setUser(client_1);
        commentRepository.save(comment3);

        // Comentario sin usuario para probar.
        Comment comment4 = context.getBean(Comment.class);
        comment4.setComment_text("COMENTARIO SIN USUARIO XD");
        commentRepository.save(comment4);


    }

    private Calendar addDay(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal;
    }


    private Calendar addDay2(Calendar cal, int days) {
        cal.add(Calendar.DATE, days);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(cal.getTime());
        return calendar;
    }
}
