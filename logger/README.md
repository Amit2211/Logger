# Logger

* Logger is a Simple And Powerful library for logging in your application in your customizable way.

* It also supports and manages the feature of saving logs into the database so that you can send those logs to the server for improvisation in your app and see how your app is performing.

* Also, you can add a list of classes in Logger for generating logs from a specific class only.

## Setup Installation

Add this line at your project-level Gradle.

```Android
allprojects {
 repositories {
    jcenter()
    maven { url "https://www.jitpack.io" }
 }
}
```


Add this dependency at your app-level Gradle file to install Logger in your project.

```Android
implementation 'com.github.Amit2211:Logger:1.0'
```

## Sample code

* Initialization

```Android
Logger.with(this, new Logger.Builder()
                .setLoggable(true)
                .insertToDb(false)
                .withSelectedClassOnly(false)
                .build());
```


* Use and check sample code

```Android
Logger.e(TAG, "run: " + Logger.getInstance().hashCode());
//hasecode will be same across the application because of its single instance.

Logger.e(TAG, "onCreate:loggable " + Logger.isLoggable());
Logger.e(TAG, "onCreate:onlySelectedClass " + Logger.getParams().isOnlySelectedClass());
Logger.e(TAG, "onCreate:database " + Logger.getParams().getDatabase());
Logger.e(TAG, "onCreate:insertToDb " + Logger.getParams().isInsertToDb());
Logger.e(TAG, "onCreate:getDbHelper " + Logger.getParams().getDbHelper());
```

* LoggerDb is a class that is used to insert logs in the database.
you can get a list of all logs from its static method as shown below.

```Android
ArrayList<LoggerDb> list = LoggerDb.readAllFromDatabase(Logger.getDatabase());
```


## Author
[Amit Solanki](https://github.com/Amit2211) is the primary author and manager of this library.

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

[![](https://jitpack.io/v/Amit2211/Logger.svg)](https://jitpack.io/#Amit2211/Logger)