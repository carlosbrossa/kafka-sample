package br.com.sample;

public interface ServiceFactory<T> {
    ConsumerService<T> create();
}
