[![API](https://img.shields.io/badge/API-19%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=19)
# MovieWatcher
*Android Movie DB browser*

## Completed features from interview task
* Movie list screen
* Movie details screen
* Persistent favourites save
* Autocomlpete text input
* Use of endpoints from <a href="https://www.themoviedb.org/documentation/api?language=en-US" title="Movie DB">Movie DB</a>

## What was used to build app
* Kotlin 1.3.50
* Android Studio 3.5.1

**Pattern**
* MVVM

**Libraries**
* <a href="https://github.com/ReactiveX/RxKotlin" title="RxKotlin">RxKotlin</a>
* <a href="https://github.com/JakeWharton/RxBinding" title="RxBinding">RxBinding</a>
* <a href="https://insert-koin.io" title="Koin">Koin</a>
* <a href="https://github.com/square/okhttp" title="okHttp">okHttp</a>
* <a href="https://github.com/square/retrofit" title="Retrofit">Retrofit</a>
* <a href="https://github.com/bumptech/glide" title="Glide">Glide</a>
* <a href="https://github.com/flavioarfaria/KenBurnsView" title="KenBurnsView">KenBurnsView</a>

**Tests**
* JUnit, Mockito

**Tested**
* ViewModels
* UseCases

**Tested on**
* Samsung Galaxy A6
* API version 28

## Potential bugs
On Android Emulator API 19 run on laptop with AMD CPU architecture, there was present a bug SSLHandshakeException.
According to <a href="https://developer.android.com/training/articles/security-ssl#UnknownCa" title="documentation">documentation</a>, it's related to unknown CA during network connection. Reasons may vary and it was decided not to pursuit this bug due to limited time.
