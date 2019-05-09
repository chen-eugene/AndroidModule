package com.eugene.aop.apt;

import com.example.aop.apt.provider.Provider;

public interface Binder<T> {

    void bind(T host, Object source, Provider provider);

}
