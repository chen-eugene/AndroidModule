package com.eugene.aop.bindview.apt;


import com.eugene.aop.bindview.apt.provider.Provider;

public interface Binder<T> {

    void bind(T host, Object source, Provider provider);

}
