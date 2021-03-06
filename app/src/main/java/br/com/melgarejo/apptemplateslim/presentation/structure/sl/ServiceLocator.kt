@file:Suppress("IMPLICIT_CAST_TO_ANY")

package br.com.melgarejo.apptemplateslim.presentation.structure.sl

import android.content.Context
import br.com.melgarejo.apptemplateslim.data.remote.repository.DefaultUserRepository
import br.com.melgarejo.apptemplateslim.data.storage.PreferencesCache
import br.com.melgarejo.apptemplateslim.domain.boundary.UserRepository
import br.com.melgarejo.apptemplateslim.domain.boundary.resources.Cache
import br.com.melgarejo.apptemplateslim.domain.boundary.resources.Logger
import br.com.melgarejo.apptemplateslim.domain.boundary.resources.SchedulerProvider
import br.com.melgarejo.apptemplateslim.domain.boundary.resources.StringsProvider
import br.com.melgarejo.apptemplateslim.domain.interactor.user.GetPersistedUser
import br.com.melgarejo.apptemplateslim.domain.interactor.user.RecoverPassword
import br.com.melgarejo.apptemplateslim.domain.interactor.user.SignIn
import br.com.melgarejo.apptemplateslim.domain.interactor.user.SignUp
import br.com.melgarejo.apptemplateslim.presentation.landing.SplashViewModel
import br.com.melgarejo.apptemplateslim.presentation.login.LoginViewModel
import br.com.melgarejo.apptemplateslim.presentation.password.RecoverPasswordViewModel
import br.com.melgarejo.apptemplateslim.presentation.signup.SignUpViewModel
import br.com.melgarejo.apptemplateslim.presentation.util.ErrorHandler
import br.com.melgarejo.apptemplateslim.presentation.util.resources.AndroidLogger
import br.com.melgarejo.apptemplateslim.presentation.util.resources.AndroidStringProvider
import br.com.melgarejo.apptemplateslim.presentation.util.resources.DefaultSchedulerProvider
import br.com.melgarejo.apptemplateslim.presentation.util.resources.LoginAction

interface ServiceLocator {
    companion object {
        private var INSTANCE: ServiceLocator? = null
        fun getInstance() = INSTANCE
                ?: throw RuntimeException("Holy crap!! Service locator should have an instance already")

        fun getInstance(context: Context): ServiceLocator {
            return INSTANCE ?: DefaultServiceLocator(context).also {
                INSTANCE = it
            }
        }
    }

    val cache: Cache
    val logger: Logger
    val strings: StringsProvider
    val schedulerProvider: SchedulerProvider
    val loginAction: LoginAction
    val context: Context


}

class DefaultServiceLocator(override val context: Context) : ServiceLocator {

    override val cache: Cache
        get() = singletonCache ?: PreferencesCache.init(context).also { singletonCache = it }

    override val logger: Logger by lazy { AndroidLogger(context) }

    override val strings: StringsProvider by lazy { AndroidStringProvider(context) }

    override val schedulerProvider: SchedulerProvider by lazy { DefaultSchedulerProvider() }

    private var singletonCache: Cache? = null
    override val loginAction by lazy { LoginAction(context, cache) }

}