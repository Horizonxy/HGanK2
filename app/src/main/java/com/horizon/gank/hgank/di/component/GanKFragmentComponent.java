package com.horizon.gank.hgank.di.component;

import com.horizon.gank.hgank.di.FragmentScope;
import com.horizon.gank.hgank.di.module.GanKFragmentModule;
import com.horizon.gank.hgank.ui.fragment.GanKFragment;

import dagger.Component;

@FragmentScope
@Component(modules = GanKFragmentModule.class)
public interface GanKFragmentComponent {

    GanKFragment inject(GanKFragment fragment);
}
