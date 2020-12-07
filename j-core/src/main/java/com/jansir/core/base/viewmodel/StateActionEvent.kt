package com.jansir.core.base.viewmodel

/**
 * author: jansir
 * e-mail: xxx
 * date: 2020/5/6.
 */
sealed  class StateActionEvent

object LoadState : StateActionEvent()

object SuccessState : StateActionEvent()

class NetErrorState(val message: String?="") : StateActionEvent()

object DataErrorState : StateActionEvent()