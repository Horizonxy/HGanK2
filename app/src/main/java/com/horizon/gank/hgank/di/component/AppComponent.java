package com.horizon.gank.hgank.di.component;

import com.horizon.gank.hgank.Application;
import com.horizon.gank.hgank.di.module.ApiModule;
import com.horizon.gank.hgank.di.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        AppModule.class,
        ApiModule.class
})
public interface AppComponent {
    Application inject(Application application);
}
