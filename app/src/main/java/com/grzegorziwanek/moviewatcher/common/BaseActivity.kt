package com.grzegorziwanek.moviewatcher.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity : AppCompatActivity() {

  protected val composite = CompositeDisposable()

  abstract fun specifyLayoutResId(): Int

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(specifyLayoutResId())
  }

  override fun onDestroy() {
    composite.clear()
    super.onDestroy()
  }
}