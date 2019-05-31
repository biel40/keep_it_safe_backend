package com.esliceu.keep_it_safe;

import com.esliceu.keep_it_safe.entities.Invoice;
import com.esliceu.keep_it_safe.entities.Luggage;
import com.esliceu.keep_it_safe.entities.User;
import com.esliceu.keep_it_safe.repository.InvoiceRepository;
import com.esliceu.keep_it_safe.repository.LuggageRepository;
import com.esliceu.keep_it_safe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.Instant;


@Component
public class MockDatabase {

    private UserRepository userRepository;
    private LuggageRepository luggageRepository;
    private InvoiceRepository invoiceRepository;
    private ApplicationContext context;

    @Autowired
    public MockDatabase(UserRepository userRepository, LuggageRepository luggageRepository, InvoiceRepository invoiceRepository, ApplicationContext context) {
        this.userRepository = userRepository;
        this.luggageRepository = luggageRepository;
        this.invoiceRepository = invoiceRepository;
        this.context = context;
    }

    public void createDatabaseMock(){

        User user_1 = context.getBean(User.class);
        user_1.setName("AAAAA");
        user_1.setSurnames("BBBBBB CCCCC");
        user_1.setPassword("*******");
        user_1.setEmail("usuario1@gmail.com");
        user_1.setRol_user(RolUser.EMPLOYEE);
        userRepository.save(user_1);

        User user_2 = context.getBean(User.class);
        user_2.setName("ZZZZZ");
        user_2.setSurnames("XXXXXX WWWWWW");
        user_2.setRol_user(RolUser.CLIENT);
        user_2.setPassword("*******");
        user_2.setEmail("usuario2@gmail.com");
        userRepository.save(user_2);

        User biel = context.getBean(User.class);
        biel.setName("Biel");
        biel.setSurnames("Borras Serra");
        biel.setRol_user(RolUser.CLIENT);
        biel.setPassword("biel");
        biel.setEmail("bielet40@gmail.com");
        userRepository.save(biel);


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

        Invoice invoice_1 = context.getBean(Invoice.class);
        invoice_1.setTotal_price(50);
        invoice_1.setStart_date(Instant.now());
        invoice_1.setEnd_date(Instant.now());
        invoiceRepository.save(invoice_1);

        Invoice invoice_2 = context.getBean(Invoice.class);
        invoice_2.setTotal_price(60);
        invoice_2.setStart_date(Instant.now());
        invoice_2.setEnd_date(Instant.now());
        invoiceRepository.save(invoice_2);
        invoice_2.setUser(biel);
        invoiceRepository.save(invoice_2);

    }
}
