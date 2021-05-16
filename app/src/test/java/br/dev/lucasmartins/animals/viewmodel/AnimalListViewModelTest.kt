package br.dev.lucasmartins.animals.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.dev.lucasmartins.animals.di.*
import br.dev.lucasmartins.animals.model.Animal
import br.dev.lucasmartins.animals.model.ApiKeyResponse
import br.dev.lucasmartins.animals.network.AnimalApiService
import br.dev.lucasmartins.animals.util.SharedPreferencesHelper
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor


class AnimalListViewModelTest {
    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var animalService: AnimalApiService

    @Mock
    lateinit var prefs: SharedPreferencesHelper

    private val application = Mockito.mock(Application::class.java)

    private var subject = AnimalListViewModel(application, true)

    private val key = "Key Mock"

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        DaggerViewModelComponent.builder()
                .appModule(AppModule(application))
                .apiModule(ApiModuleTest(animalService))
                .prefsModule(PrefsModuleTest(prefs))
                .build()
                .inject(subject)
    }

    @Before
    fun setupRxSchedulers() {
        val immediate = object : Scheduler() {
            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() }, true)
            }
        }

        RxJavaPlugins.setInitNewThreadSchedulerHandler { immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { immediate }
    }

    @Test
    fun getKeySuccess() {

        val apiKey = ApiKeyResponse("OK", key)
        val keySingle = Single.just(apiKey)

        val animal = Animal(
            "AnimalMock",
            null,
            null,
            null,
            null,
            null,
            null
        )
        val animalList = listOf(animal)
        val testSingle = Single.just(animalList)

        Mockito.`when`(prefs.getApiKey()).thenReturn(null)
        Mockito.`when`(animalService.getApiKey()).thenReturn(keySingle)
        Mockito.`when`(animalService.getAnimals(key)).thenReturn(testSingle)

        subject.refresh()

        Assert.assertEquals(1, subject.animals.value?.size)
        Assert.assertFalse(subject.loadError.value!!)
        Assert.assertFalse(subject.loading.value!!)
    }

    @Test
    fun getKeyFailure() {
        Mockito.`when`(prefs.getApiKey()).thenReturn(null)

        val keySingle = Single.error<ApiKeyResponse>(Throwable())

        Mockito.`when`(animalService.getApiKey()).thenReturn(keySingle)

        subject.refresh()
        Assert.assertNull(subject.animals.value)
        Assert.assertTrue(subject.loadError.value!!)
        Assert.assertFalse(subject.loading.value!!)
    }

    @Test
    fun getAnimalsSuccess() {
        Mockito.`when`(prefs.getApiKey()).thenReturn(key)
        val animal = Animal(
            "AnimalMock",
            null,
            null,
            null,
            null,
            null,
            null
        )
        val animalList = listOf(animal)
        val testSingle = Single.just(animalList)
        Mockito.`when`(animalService.getAnimals(key)).thenReturn(testSingle)

        subject.refresh()

        Assert.assertEquals(1, subject.animals.value?.size)
        Assert.assertFalse(subject.loadError.value!!)
        Assert.assertFalse(subject.loading.value!!)
    }

    @Test
    fun getAnimalsFailure() {
        Mockito.`when`(prefs.getApiKey()).thenReturn(key)
        val testSingle = Single.error<List<Animal>>(Throwable())
        val keySingle = Single.just(ApiKeyResponse("OK", key))
        Mockito.`when`(animalService.getAnimals(key)).thenReturn(testSingle)
        Mockito.`when`(animalService.getApiKey()).thenReturn(keySingle)

        subject.refresh()

        Assert.assertEquals(null, subject.animals.value)
        Assert.assertFalse(subject.loading.value!!)
        Assert.assertTrue(subject.loadError.value!!)
    }
}