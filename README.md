# Simple Movie DB

Using API provided by [The MovieDB][TmdbL], the app retrieves list of genre movies, list of movies by genre, basic info & video trailer of a movie as well as user reviews of the movie.

Using Kotlin language and MVVM design architecture, here are some components that also used in the app..

### Jetpack Components
- [Hilt Dependency Injection (v2.31.2-alpha)][HiltL]
- [Room DB (v2.2.6)][RoomL]
- [Data Binding][DataBindingL]
- [Paging 3 Library (3.0.0-beta01)][Paging3L]

### Third Party Component
- Network Request: [Retrofit 2][RetrofitL], [OkHttp][OkHttpL], [Gson Converter][GsonL]
- Image Loader: [Fresco][FrescoL]
- Video Player: [Android YT Player][AndroidYTL]

### Negative Usecase covered
- Handle error coming while doing API request (initial load & next page)

### Screenshot
![Screenshot](https://github.com/widiarifki/movie-db/blob/main/_extras/screenshot.gif?raw=true)

[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)


   [RetrofitL]: <https://github.com/square/retrofit>
   [OkHTTPL]: <https://github.com/square/okhttp>
   [GsonL]: <https://github.com/google/gson>
   [FrescoL]: <https://github.com/facebook/fresco>
   [AndroidYTL]: <https://github.com/PierfrancescoSoffritti/android-youtube-player>
   [TmdbL]: <https://www.themoviedb.org/documentation/api>
   [HiltL]: <https://dagger.dev/hilt/>
   [Paging3L]: <https://developer.android.com/topic/libraries/architecture/paging/v3-overview>
   [DataBindingL]: <https://developer.android.com/topic/libraries/data-binding>
   [RoomL]: <https://developer.android.com/reference/android/arch/persistence/room/RoomDatabase>