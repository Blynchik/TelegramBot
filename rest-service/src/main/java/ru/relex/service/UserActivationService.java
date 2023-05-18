package ru.relex.service;

public interface UserActivationService {
    //принимает в зашифрованном виде
    boolean activation(String cryptoUserId);
}
