package com.karumi.screenshot.ui.view;

import android.app.Activity;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import com.karumi.screenshot.ScreenshotTest;
import com.karumi.screenshot.SuperHeroesApplication;
import com.karumi.screenshot.di.MainComponent;
import com.karumi.screenshot.di.MainModule;
import com.karumi.screenshot.model.SuperHero;
import com.karumi.screenshot.model.SuperHeroesRepository;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import it.cosenonjaviste.daggermock.DaggerMockRule;

import static org.mockito.Mockito.when;

public class SuperHeroDetailActivityTest extends ScreenshotTest {

    @Mock SuperHeroesRepository repository;

    @Rule public DaggerMockRule<MainComponent> daggerRule =
            new DaggerMockRule<>(MainComponent.class, new MainModule()).set(
                    new DaggerMockRule.ComponentSetter<MainComponent>() {
                        @Override public void setComponent(MainComponent component) {
                            SuperHeroesApplication app =
                                    (SuperHeroesApplication) InstrumentationRegistry.getInstrumentation()
                                            .getTargetContext()
                                            .getApplicationContext();
                            app.setComponent(component);
                        }
                    });

    @Rule public IntentsTestRule<SuperHeroDetailActivity> activityRule =
            new IntentsTestRule<>(SuperHeroDetailActivity.class, true, false);

    @Test public void showsHeroFromIntent() {
        SuperHero superHero = givenSuperHeroForName("AquaMan", false);
        Intent intent = new Intent();
        intent.putExtra(SuperHeroDetailActivity.SUPER_HERO_NAME_KEY, superHero.getName());

        Activity activity = activityRule.launchActivity(intent);

        compareScreenshot(activity);
    }

    @Test public void showsHeroWithoutName() {
        SuperHero superHero = new SuperHero("", "photo", false, "Description");
        givenSuperHero(superHero);
        Intent intent = new Intent();
        intent.putExtra(SuperHeroDetailActivity.SUPER_HERO_NAME_KEY, superHero.getName());

        Activity activity = activityRule.launchActivity(intent);

        compareScreenshot(activity);
    }

    @Test public void showsHeroWithoutDescription() {
        SuperHero superHero = new SuperHero("DescriptionlessMan", "photo", false, "");
        givenSuperHero(superHero);
        Intent intent = new Intent();
        intent.putExtra(SuperHeroDetailActivity.SUPER_HERO_NAME_KEY, superHero.getName());

        Activity activity = activityRule.launchActivity(intent);

        compareScreenshot(activity);
    }

    @Test public void showsAvenger() {
        SuperHero superHero = new SuperHero("AvengerMan", "photo", true, "Me avenger, you no");
        givenSuperHero(superHero);
        Intent intent = new Intent();
        intent.putExtra(SuperHeroDetailActivity.SUPER_HERO_NAME_KEY, superHero.getName());

        Activity activity = activityRule.launchActivity(intent);

        compareScreenshot(activity);
    }

    private SuperHero givenSuperHeroForName(String name, boolean avenger) {
        String superHeroDescription = "Description Super Hero - " + name;
        SuperHero superHero = new SuperHero(name, "photo", avenger, superHeroDescription);
        when(repository.getByName(name)).thenReturn(superHero);

        return superHero;
    }

    private SuperHero givenSuperHero(SuperHero superHero) {
        when(repository.getByName(superHero.getName())).thenReturn(superHero);

        return superHero;
    }
}