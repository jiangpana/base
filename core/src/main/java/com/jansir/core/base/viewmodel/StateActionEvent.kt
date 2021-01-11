package com.jansir.core.base.viewmodel

/**
 * author: jansir
 * e-mail: xxx
 * date: 2020/5/6.
 */
sealed class StateActionEvent {

    object LoadingState : StateActionEvent()

    object SuccessState : StateActionEvent()

    object NetErrorState : StateActionEvent()

    object DataErrorState : StateActionEvent()
}

