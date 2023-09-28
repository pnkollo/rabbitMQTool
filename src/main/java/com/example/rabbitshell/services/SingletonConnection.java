package com.example.rabbitshell.services;

import com.rabbitmq.client.ConnectionFactory;

public class SingletonConnection {
        static ConnectionFactory factory;

        public static ConnectionFactory getFactory(){
            factory.setHost("${host.name}");
            return factory;
        }
}
