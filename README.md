# Simple Movie DB

Using API provided by [The Movie DB][TmdbL], the app retrieves movie genres, movies by genre, basic info & video trailer of a movie as well as movie reviews.

Using **Kotlin** language and **MVVM** design architecture, here are some components that also used in the app..

### Jetpack Components
- [Hilt Dependency Injection (v2.31.2-alpha)][HiltL]
- [Room DB (v2.2.6)][RoomL]
- [Data Binding][DataBindingL]
- [Paging 3 Library (v3.0.0-beta01)][Paging3L]

### Third Party Components
- Network Request: [Retrofit 2][RetrofitL], [OkHttp][OkHttpL], [Gson Converter][GsonL]
- Image Loader: [Fresco][FrescoL]
- Video Player: [Android YT Player][AndroidYTL]

### Folder structure & documentation
```
app
├── **base**: contains abstract classes that will frequently reused/implemented by another app components (i.e: activity)
├── **data**: contains code concerns about data, its modelling, its access
|   ├── **local**: contains code concerns about accessing to local data
|   ├── **model**: contains data entity model classes
|   └── **di**: contains webservice/api interface & api response wrapper class
├── **di**: contains dependency injection code, defining modules where another code will frequently dependent on it
├── **presentation**: contains code concerns about how the app will be presented. Categorized/foldered by single UI screen, one folder will define the activity and the viewmodel attached to the activity
├── **repository**: contains code concerns about how the presentation/view will interact with data
└── **utils**: contains helper classes and objects
```

### Negative usecase covered
- Handle error occured while doing API request (i.e: HTTP error, no connection)  on initial load and next page (listview)

### Screenshot
![Screenshot](https://github.com/widiarifki/movie-db/blob/main/_extras/screenshot2.gif?raw=true)

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