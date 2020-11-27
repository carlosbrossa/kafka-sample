package br.com.sample.consumer;

public interface ServiceFactory<T> {
    ConsumerService<T> create();
}
