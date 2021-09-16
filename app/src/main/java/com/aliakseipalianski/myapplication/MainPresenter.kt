package com.aliakseipalianski.myapplication

class MainPresenter(private val mainModel: MainModel = MainModel()) {

    private var mainView: IMainView? = null

    fun bind(mainView: IMainView) {
        this.mainView = mainView
    }

    fun unbind() {
        this.mainView = null
    }

    fun onTextButtonClicked() {
        val observable = mainModel.doSomethingObservable()

        observable.addObserver { o, arg ->
            //handle UI
            //DO something
            // TODO working with data and write some data related logic

            //TODO Handle to main thread

            when (arg) {
                null -> mainView?.showError()
                is List<*> -> mainView?.showList(arg)
                is SomeModel -> mainView?.showView(arg)
                else -> {
                    //else if debug - crash app or print log
                }
            }
        }

        observable.addObserver { o, arg ->
            //load another model
        }
    }

    fun onTextTwoButtonClicked() {
        mainModel.doSomething {
            //DO something
            //TODO Handle to main thread
            // TODO working with data and write some data related logic

            when (it) {
                null -> mainView?.showError()
                is List<*> -> mainView?.showList(it)
                is SomeModel -> mainView?.showView(it)
                else -> {
                    //else if debug - crash app or print log
                }
            }
        }
    }
}

class SomeModel {

}
